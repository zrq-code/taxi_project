package com.mashibing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.dto.PriceRule;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.PriceRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.mashibing.constant.CommonStatusEnum.PRICE_RULE_EXISTS;
import static com.mashibing.constant.CommonStatusEnum.PRICE_RULE_NOT_EDIT;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rick zhou
 * @since 2023-11-14
 */
@Service
public class PriceRuleService {
    @Autowired
    PriceRuleMapper priceRuleMapper;

    public ResponseResult add(PriceRule priceRule) {
        //拼接fareType
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = getFareType(cityCode, vehicleType);
        priceRule.setFareType(fareType);

        //添加版本号
        //问题1:增加了版本号，前面的两个字段无法唯一确定一条记录，问题2:找最大的版本号，需要排序
        QueryWrapper<PriceRule> priceRuleQueryWrapper = new QueryWrapper<>();
        priceRuleQueryWrapper.eq("city_code", cityCode);
        priceRuleQueryWrapper.eq("vehicle_type", vehicleType);
        priceRuleQueryWrapper.orderByDesc("fare_version");

        //是否有计价规则



        List<PriceRule> priceRules = priceRuleMapper.selectList(priceRuleQueryWrapper);
        Integer fareVersion = 0;
        if (priceRules.size() > 0) {
            return ResponseResult.fail(PRICE_RULE_EXISTS.getCode(), PRICE_RULE_EXISTS.getValue());
        }
        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);
        return ResponseResult.success();
    }

    private static String getFareType(String cityCode, String vehicleType) {
        String fareType = cityCode + "$" + vehicleType;
        return fareType;
    }

    public ResponseResult edit(PriceRule priceRule) {
        //拼接fareType
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = getFareType(cityCode, vehicleType);
        priceRule.setFareType(fareType);

        //添加版本号
        //问题1:增加了版本号，前面的两个字段无法唯一确定一条记录，问题2:找最大的版本号，需要排序
        QueryWrapper<PriceRule> priceRuleQueryWrapper = new QueryWrapper<>();
        priceRuleQueryWrapper.eq("city_code", cityCode);
        priceRuleQueryWrapper.eq("vehicle_type", vehicleType);
        priceRuleQueryWrapper.orderByDesc("fare_version");


        List<PriceRule> priceRules = priceRuleMapper.selectList(priceRuleQueryWrapper);
        Integer fareVersion = 0;
        if (priceRules.size() > 0) {
            PriceRule lastePriceRule = priceRules.get(0);
            Double unitPricePerMile = lastePriceRule.getUnitPricePerMile();
            Double unitPricePerMinute = lastePriceRule.getUnitPricePerMinute();
            Double startFare = lastePriceRule.getStartFare();
            Integer startMile = lastePriceRule.getStartMile();
            if (unitPricePerMile.doubleValue() == priceRule.getUnitPricePerMile()
                    && unitPricePerMinute.doubleValue() == priceRule.getUnitPricePerMinute()
                    && startFare.doubleValue() == priceRule.getStartFare()
                    && startMile.intValue() == priceRule.getStartMile()) {
                return ResponseResult.fail(PRICE_RULE_NOT_EDIT.getCode(), PRICE_RULE_NOT_EDIT.getValue());
            }
            fareVersion = lastePriceRule.getFareVersion();
        }
        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);
        return ResponseResult.success();
    }


    public ResponseResult isExist(PriceRule priceRule) {
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code", cityCode).eq("vehicle_type", vehicleType);
        queryWrapper.orderByDesc("fare_version");
        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(priceRules)){
            return ResponseResult.success(true);
        }else {
            return ResponseResult.success(false);
        }
    }
}
