package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.ForecastPriceDTO;
import com.mashibing.service.ForecastPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ForecastPriceController {
    @Autowired
    private ForecastPriceService forecastPriceService;
    @PostMapping("/forecast-price")
    public ResponseResult forecastPrice(@RequestBody ForecastPriceDTO forecastPriceDTO){
        return forecastPriceService.forecastPrice(forecastPriceDTO);
    }
}
