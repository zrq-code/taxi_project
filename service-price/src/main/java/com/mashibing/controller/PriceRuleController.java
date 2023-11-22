package com.mashibing.controller;


import com.mashibing.dto.PriceRule;
import com.mashibing.dto.ResponseResult;
import com.mashibing.service.PriceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rick zhou
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/price-rule")
public class PriceRuleController {
    @Autowired
    private PriceRuleService priceRuleService;
    @PostMapping("/add")
    public ResponseResult add(@RequestBody PriceRule priceRule){
        return priceRuleService.add(priceRule);
    }

    @PostMapping("/edit")
    public ResponseResult edit(@RequestBody PriceRule priceRule){
        return priceRuleService.edit(priceRule);
    }

    /**
     * 判断该城市和对应车型的计价规则是否存在
     * @param priceRule
     * @return
     */
    @PostMapping("/is-exist")
    public ResponseResult<Boolean> isExist(@RequestBody PriceRule priceRule){return priceRuleService.isExist(priceRule);}
}
