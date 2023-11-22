package com.mashibing.remote;

import com.mashibing.dto.PriceRule;
import com.mashibing.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-price")
public interface ServicePriceClient {
    @PostMapping("/price-rule/is-exist")
    public ResponseResult<Boolean> isPriceRuleExist(@RequestBody PriceRule priceRule);
}
