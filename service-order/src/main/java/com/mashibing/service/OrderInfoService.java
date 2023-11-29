package com.mashibing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.dto.OrderInfo;
import com.mashibing.dto.PriceRule;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.OrderInfoMapper;
import com.mashibing.remote.ServiceDriverUserClient;
import com.mashibing.remote.ServiceMapClient;
import com.mashibing.remote.ServicePriceClient;
import com.mashibing.request.OrderRequest;
import com.mashibing.response.TerminalResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mashibing.constant.CommonStatusEnum.*;
import static com.mashibing.constant.OrderConstants.*;
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

        //判断正在进行的订单不允许下单
        if (extracted(orderRequest)) {
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
        ResponseResult<List<TerminalResponse>> result = null;
        for (int i = 0; i < radiusList.size(); i++) {
            Integer radius = radiusList.get(i);
            result = serviceMapClient.aroundSearch(center, radius);
            log.info("在半径为 "+radius+"m 的范围内，寻找车辆" + JSONObject.fromObject(result.getData().get(0)));
            //获得终端
            //解析终端
            //根据解析出来的终端，查询车辆
            //找到符合的车辆，进行派单
            //如果派单成功，退出循环
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
     * 是否有业务中的订单
     *
     * @param orderRequest
     * @return
     */
    private boolean extracted(OrderRequest orderRequest) {
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("passenger_id", orderRequest.getPassengerId());
        wrapper.and(w -> w.eq("order_status", ORDER_START))
                .or(w -> w.eq("order_status", DRIVER_RECEIVE_ORDER))
                .or(w -> w.eq("order_status", DRIVER_TO_PICK_UP_PASSENGER))
                .or(w -> w.eq("order_status", DRIVER_ARRIVED_DEPARTURE))
                .or(w -> w.eq("order_status", PICK_UP_PASSENGER))
                .or(w -> w.eq("order_status", TO_START_PAY)
                );
        Integer count = orderInfoMapper.selectCount(wrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }

    public ResponseResult testMapper() {
        OrderInfo order = new OrderInfo();
        order.setAddress("10001");
        orderInfoMapper.insert(order);
        return ResponseResult.success();
    }
}
