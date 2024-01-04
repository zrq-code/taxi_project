package com.mashibing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.constant.IdentityConstants;
import com.mashibing.constant.OrderConstants;
import com.mashibing.dto.Car;
import com.mashibing.dto.OrderInfo;
import com.mashibing.dto.PriceRule;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.OrderInfoMapper;
import com.mashibing.remote.ServiceDriverUserClient;
import com.mashibing.remote.ServiceMapClient;
import com.mashibing.remote.ServicePriceClient;
import com.mashibing.remote.ServiceSsePushClient;
import com.mashibing.request.OrderRequest;
import com.mashibing.response.OrderDriverResponse;
import com.mashibing.response.TerminalResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mashibing.constant.CommonStatusEnum.*;
import static com.mashibing.constant.OrderConstants.ORDER_START;
import static com.mashibing.util.RedisPrefixUtils.blackDeviceCodePrefix;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rick zhou
 * @since 2023-10-25
 */
@Service
@Slf4j
public class OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ServicePriceClient servicePriceClient;
    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;
    @Autowired
    private ServiceMapClient serviceMapClient;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ServiceSsePushClient serviceSsePushClient;


    public ResponseResult add(OrderRequest orderRequest) {
        //测试当前城市是否有可用的司机
        ResponseResult<Boolean> result = serviceDriverUserClient.isAvailableDriver(orderRequest.getAddress());
        log.info("测试城市是否有司机结果：" + result.getData());
        if (!result.getData()) {
            return ResponseResult.fail(CITY_DRIVER_EMPTY.getCode(), CITY_DRIVER_EMPTY.getValue());
        }

        //需要判断 下单的设备是否是黑名单设备
        String deviceCode = orderRequest.getDeviceCode();
        //生成key
        String deviceCodeKey = blackDeviceCodePrefix + deviceCode;
        //设置key看有没有原来的key
        if (isBlackDevice(deviceCodeKey)) {
            return ResponseResult.fail(DEVICE_IS_BLACK.getCode(), DEVICE_IS_BLACK.getValue());
        }

        // 判断乘客 是否有进行中的订单
        if (isPassengerOrderGoingon(orderRequest.getPassengerId()) > 0) {
            return ResponseResult.fail(ORDER_GOING_ON.getCode(), ORDER_GOING_ON.getValue());
        }

        if (!isPriceRuleExist(orderRequest)) {
            return ResponseResult.fail(CITY_SERVICE_NOT_SERVICE.getCode(), CITY_SERVICE_NOT_SERVICE.getValue());
        }


        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderRequest, orderInfo);
        orderInfo.setOrderStatus(ORDER_START);

        LocalDateTime now = LocalDateTime.now();
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);
        orderInfoMapper.insert(orderInfo);
        //派单dispatchRealTimeOrder
        dispatchRealTimeOrder(orderInfo);
        return ResponseResult.success();
    }

    /**
     * 实时订单派单逻辑
     *
     * @param orderInfo
     */
    public void dispatchRealTimeOrder(OrderInfo orderInfo) {

        //2km
        String depLatitude = orderInfo.getDepLatitude();
        String depLongitude = orderInfo.getDepLongitude();
        String center = depLatitude + "," + depLongitude;

        ArrayList<Integer> radiusList = new ArrayList<>();
        radiusList.add(2000);
        radiusList.add(4000);
        radiusList.add(5000);
        //搜索结果
        ResponseResult<List<TerminalResponse>> result;
        for (int i = 0; i < radiusList.size(); i++) {
            Integer radius = radiusList.get(i);
            result = serviceMapClient.aroundSearch(center, radius);
            log.info("在半径为 " + radius + "m 的范围内，寻找车辆" + JSONObject.fromObject(result.getData().get(0)));
            //获得终端{"carId":1716724829761400833,"tid":"775745186"}

            //解析终端
            List<TerminalResponse> data1 = result.getData();
            for (TerminalResponse terminalResponse : data1) {
                Long carId = terminalResponse.getCarId();
                String longitude = terminalResponse.getLongitude();
                String latitude = terminalResponse.getLatitude();

                //查询是否有多余的可派单司机
                ResponseResult<OrderDriverResponse> availableDriver = serviceDriverUserClient.getAvailableDriver(carId);
                if (availableDriver.getCode() == AVAILABLE_DRIVER_EMPTY.getCode()) {
                    log.info("没有车辆ID:" + carId + "对应的司机");
                    continue;
                } else {
                    log.info("车辆ID:" + carId + "找到了正在出车的司机");
                    OrderDriverResponse data = availableDriver.getData();
                    Long driverId = data.getDriverId();
                    String driverPhone = data.getDriverPhone();
                    String licenseId = data.getLicenseId();
                    String vehicleNo = data.getVehicleNo();

                    String lockKey = (String.valueOf(driverId)).intern();
                    RLock lock = redissonClient.getLock(lockKey);

                    lock.lock();
                    // 判断司机 是否有进行中的订单
                    if (isDriverOrderGoingon(driverId) > 0) {
                        lock.unlock();
                        continue;
                    }
                    //订单直接匹配司机
                    //查询当前车辆信息
                    QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
                    carQueryWrapper.eq("id", carId);
                    //查询当前司机信息
                    orderInfo.setDriverId(driverId);
                    orderInfo.setDriverPhone(driverPhone);
                    orderInfo.setCarId(carId);
                    //从地图中来
                    orderInfo.setReceiveOrderCarLongitude(longitude);
                    orderInfo.setReceiveOrderCarLatitude(latitude);
                    orderInfo.setReceiveOrderTime(LocalDateTime.now());
                    orderInfo.setLicenseId(licenseId);
                    orderInfo.setVehicleNo(vehicleNo);
                    orderInfo.setOrderStatus(OrderConstants.DRIVER_RECEIVE_ORDER);
                    orderInfoMapper.updateById(orderInfo);

                    //通知司机
                    JSONObject driverContent = new JSONObject();
                    driverContent.put("orderId", orderInfo.getId());
                    driverContent.put("passengerId", orderInfo.getPassengerId());
                    driverContent.put("passengerPhone", orderInfo.getPassengerPhone());
                    driverContent.put("departure", orderInfo.getDeparture());
                    driverContent.put("depLongitude", orderInfo.getDepLongitude());
                    driverContent.put("depLatitude", orderInfo.getDepLatitude());
                    driverContent.put("destination", orderInfo.getDestination());
                    driverContent.put("destLongitude", orderInfo.getDestLongitude());
                    driverContent.put("destLatitude", orderInfo.getDestLatitude());
                    serviceSsePushClient.push(driverId, IdentityConstants.DRIVER_IDENTITY, driverContent.toString());

                    // 通知乘客
                    JSONObject passengerContent = new JSONObject();
                    passengerContent.put("orderId", orderInfo.getId());
                    passengerContent.put("driverId", orderInfo.getDriverId());
                    passengerContent.put("driverPhone", orderInfo.getDriverPhone());
                    passengerContent.put("vehicleNo", orderInfo.getVehicleNo());
                    // 车辆信息，调用车辆服务
                    ResponseResult<Car> carById = serviceDriverUserClient.getCarById(carId);
                    Car carRemote = carById.getData();
                    passengerContent.put("brand", carRemote.getBrand());
                    passengerContent.put("model", carRemote.getModel());
                    passengerContent.put("vehicleColor", carRemote.getVehicleColor());
                    passengerContent.put("receiveOrderCarLongitude", orderInfo.getReceiveOrderCarLongitude());
                    passengerContent.put("receiveOrderCarLatitude", orderInfo.getReceiveOrderCarLatitude());
                    serviceSsePushClient.push(orderInfo.getPassengerId(), IdentityConstants.PASSENGER_IDENTITY, passengerContent.toString());
                    lock.unlock();

                    //退出 不在进行司机的查找
                    break;
                }

            }

        }

    }


    private boolean isPriceRuleExist(OrderRequest orderRequest) {
        String fareType = orderRequest.getFareType();
        int index = fareType.indexOf("$");
        String cityCode = fareType.substring(0, index);
        String vehicleType = fareType.substring(index + 1);

        PriceRule priceRule = new PriceRule();
        priceRule.setCityCode(cityCode);
        priceRule.setVehicleType(vehicleType);

        return servicePriceClient.isPriceRuleExist(priceRule).getData();
    }

    private boolean isBlackDevice(String deviceCodeKey) {
        Boolean aBoolean = stringRedisTemplate.hasKey(deviceCodeKey);
        if (aBoolean) {
            String s = stringRedisTemplate.opsForValue().get(deviceCodeKey);
            int i = Integer.parseInt(s);
            if (i >= 2) {
                //当前设备超过下单次数
                return true;
            } else {
                stringRedisTemplate.opsForValue().increment(deviceCodeKey);
            }

        } else {
            stringRedisTemplate.opsForValue().setIfAbsent(deviceCodeKey, "1", 1L, TimeUnit.HOURS);
        }
        return false;
    }

    /**
     * 判断是否有 业务中的订单
     *
     * @param passengerId
     * @return
     */
    private int isPassengerOrderGoingon(Long passengerId) {
        // 判断有正在进行的订单不允许下单
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("passenger_id", passengerId);
        queryWrapper.and(wrapper -> wrapper.eq("order_status", OrderConstants.ORDER_START)
                .or().eq("order_status", OrderConstants.DRIVER_RECEIVE_ORDER)
                .or().eq("order_status", OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status", OrderConstants.PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstants.PASSENGER_GETOFF)
                .or().eq("order_status", OrderConstants.TO_START_PAY)
        );


        Integer validOrderNumber = orderInfoMapper.selectCount(queryWrapper);

        return validOrderNumber;

    }

    /**
     * 判断是否有 业务中的订单
     *
     * @param driverId
     * @return
     */
    private int isDriverOrderGoingon(Long driverId) {
        // 判断有正在进行的订单不允许下单
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id", driverId);
        queryWrapper.and(wrapper -> wrapper
                .eq("order_status", OrderConstants.DRIVER_RECEIVE_ORDER)
                .or().eq("order_status", OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status", OrderConstants.PICK_UP_PASSENGER)

        );


        Integer validOrderNumber = orderInfoMapper.selectCount(queryWrapper);
        log.info("司机Id：" + driverId + ",正在进行的订单的数量：" + validOrderNumber);

        return validOrderNumber;

    }

    public ResponseResult testMapper() {
        OrderInfo order = new OrderInfo();
        order.setAddress("10001");
        orderInfoMapper.insert(order);
        return ResponseResult.success();
    }
}
