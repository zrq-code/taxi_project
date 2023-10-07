package com.mashibing.service;

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
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import static com.mashibing.constant.CommonStatusEnum.PRICE_RULE_EMPTY;

@Service
@Slf4j
public class ForecastPriceService {
    @Autowired
    private ServiceMapClient serviceMapClient;
    @Autowired
    private PriceRuleMapper priceRuleMapper;

    public ResponseResult forecastPrice(ForecastPriceDTO forecastPriceDTO) {
        log.info("调用地图服务，查询距离和时长");
        ResponseResult<DirectionResponse> direction = serviceMapClient.direction(forecastPriceDTO);
        Integer distance = direction.getData().getDistance();
        Integer duration = direction.getData().getDuration();

        log.info("读取计价规则");
        HashMap<String, Object> query = new HashMap<>();
        query.put("city_code", "110000");
        query.put("vehicle_type", "1");

        List<PriceRule> priceRules = priceRuleMapper.selectByMap(query);
        if (priceRules.size() == 0) {
            return ResponseResult.fail(PRICE_RULE_EMPTY.getCode(), PRICE_RULE_EMPTY.getValue());
        }
        PriceRule priceRule = priceRules.get(0);

        log.info("根据距离、时长、计价规则，计算价格");
        double price = getPrice(distance, duration, priceRule);
        ForecastPriceResponse forecastPriceResponse = new ForecastPriceResponse();
        forecastPriceResponse.setPrice(price);
        return ResponseResult.success(forecastPriceResponse);
    }

    /**
     * 根据距离、时长、计算最终价格
     *
     * @param distance
     * @param duration
     * @param priceRule
     * @return
     */
    private static double getPrice(Integer distance, Integer duration, PriceRule priceRule) {
        //起步价
        double price = 0;
        price = BigDecimalUtils.add(price, priceRule.getStartFare());
        //总里程km
        double distanceMile = BigDecimalUtils.divide(distance, 1000);
        //起步里程
        double startMile = (double) priceRule.getStartMile();
        double subtract = BigDecimalUtils.subtract(distanceMile, startMile);
        //最终收费里程数
        double mile = subtract < 0 ? 0 : subtract;
        //计程单价
        //里程价
        double mileFare = BigDecimalUtils.multiply(mile, priceRule.getUnitPricePerMile());
        price = BigDecimalUtils.add(price, mileFare);
        //时长费
        //时长分钟数
        double timeMinute = BigDecimalUtils.divide(duration, 60);
        //计时单价
        BigDecimal unitPricePerMinute = BigDecimal.valueOf(priceRule.getUnitPricePerMinute());
        //时长单价
        double timeFare = BigDecimalUtils.multiply(timeMinute, priceRule.getUnitPricePerMinute());
        price = BigDecimalUtils.add(price, timeFare);
        BigDecimal priceDecimal = BigDecimal.valueOf(price);
        priceDecimal = priceDecimal.setScale(2, RoundingMode.HALF_UP);
        return priceDecimal.doubleValue();
    }

/*    public  void main(String[] args) {
        PriceRule priceRule = new PriceRule();
        priceRule.setUnitPricePerMile(1.8);
        priceRule.setUnitPricePerMinute(0.5);
        priceRule.setStartFare(10.0);
        priceRule.setStartMile(3);
        System.out.println(getPrice(6500, 1800, priceRule));
    }*/
}
