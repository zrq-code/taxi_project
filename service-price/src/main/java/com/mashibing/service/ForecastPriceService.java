package com.mashibing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.constant.CommonStatusEnum;
import com.mashibing.dto.PriceRule;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.PriceRuleMapper;
import com.mashibing.remote.ServiceMapClient;
import com.mashibing.request.ForecastPriceDTO;
import com.mashibing.response.DirectionResponse;
import com.mashibing.response.ForecastPriceResponse;
import com.mashibing.util.BigDecimalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ForecastPriceService {
    @Autowired
    private ServiceMapClient serviceMapClient;
    @Autowired
    private PriceRuleMapper priceRuleMapper;

    public ResponseResult forecastPrice(ForecastPriceDTO forecastPriceDTO) {

        log.info("调用地图服务，查询距离和时长");
        forecastPriceDTO.setDepLongitude(forecastPriceDTO.getDepLongitude());
        forecastPriceDTO.setDepLatitude(forecastPriceDTO.getDepLatitude());
        forecastPriceDTO.setDestLongitude(forecastPriceDTO.getDestLongitude());
        forecastPriceDTO.setDestLatitude(forecastPriceDTO.getDestLatitude());
        ResponseResult<DirectionResponse> direction = serviceMapClient.direction(forecastPriceDTO);
        Integer distance = direction.getData().getDistance();
        Integer duration = direction.getData().getDuration();
        log.info(("距离："+distance+",时长："+duration));

        log.info("读取计价规则");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("city_code",forecastPriceDTO.getCityCode());
        queryWrapper.eq("vehicle_type",forecastPriceDTO.getVehicleType());
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        if (priceRules.size() == 0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }

        PriceRule priceRule = priceRules.get(0);

        log.info("根据距离、时长和计价规则，计算价格");

        double price = getPrice(distance, duration, priceRule);


        ForecastPriceResponse forecastPriceResponse = new ForecastPriceResponse();
        forecastPriceResponse.setPrice(price);
        forecastPriceResponse.setCityCode(forecastPriceDTO.getCityCode());
        forecastPriceResponse.setVehicleType(forecastPriceDTO.getVehicleType());
        forecastPriceResponse.setFareType(priceRule.getFareType());
        forecastPriceResponse.setFareVersion(priceRule.getFareVersion());

        return ResponseResult.success(forecastPriceResponse);
    }

    /**
     * 根据距离、时长 和计价规则，计算最终价格
     * @param distance  距离
     * @param duration  时长
     * @param priceRule 计价规则
     * @return
     */
    public double getPrice(Integer distance , Integer duration,PriceRule priceRule){
        double price = 0;

        // 起步价
        double startFare = priceRule.getStartFare();
        price = BigDecimalUtils.add(price,startFare);

        // 里程费
        // 总里程 m
        double distanceMile = BigDecimalUtils.divide(distance,1000);
        // 起步里程
        double startMile = (double)priceRule.getStartMile();
        double distanceSubtract = BigDecimalUtils.substract(distanceMile,startMile);
        // 最终收费的里程数 km
        double mile = distanceSubtract<0?0:distanceSubtract;
        // 计程单价 元/km
        double unitPricePerMile = priceRule.getUnitPricePerMile();
        // 里程价格
        double mileFare = BigDecimalUtils.multiply(mile,unitPricePerMile);
        price = BigDecimalUtils.add(price,mileFare);

        // 时长费
        // 时长的分钟数
        double timeMinute = BigDecimalUtils.divide(duration,60);
        // 计时单价
        double unitPricePerMinute = priceRule.getUnitPricePerMinute();

        // 时长费用
        double timeFare = BigDecimalUtils.multiply(timeMinute,unitPricePerMinute);
        price = BigDecimalUtils.add(price,timeFare);

        BigDecimal priceBigDecimal = new BigDecimal(price);
        priceBigDecimal = priceBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);

        return priceBigDecimal.doubleValue();
    }

/*    public  void main(String[] args) {
        PriceRule priceRule = new PriceRule();
        priceRule.setUnitPricePerMile(1.8);
        priceRule.setUnitPricePerMinute(0.5);
        priceRule.setStartFare(10.0);
        priceRule.setStartMile(3);
        System.out.println(getPrice(6500, 1800, priceRule));
    }*/
    /**
     * 计算实际价格
     * @param distance
     * @param duration
     * @param cityCode
     * @param vehicleType
     * @return
     */
    public ResponseResult<Double> calculatePrice( Integer distance ,  Integer duration, String cityCode, String vehicleType){
        // 查询计价规则
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        if (priceRules.size() == 0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }

        PriceRule priceRule = priceRules.get(0);

        log.info("根据距离、时长和计价规则，计算价格");

        double price = getPrice(distance, duration, priceRule);
        return ResponseResult.success(price);
    }
}
