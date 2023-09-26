package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.ForecastPriceDTO;
import com.mashibing.response.ForecastPriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForecastPriceService {
    /**
     * 根据出发地、目的地经纬度，计算预估价格
     * @param forecastPriceDTO
     * @return
     */
    public ResponseResult forecastPrice(ForecastPriceDTO forecastPriceDTO){
        log.info("出发地经度： "+forecastPriceDTO.getDepLongitude());
        log.info("出发地纬度： "+forecastPriceDTO.getDepLatitude());
        log.info("目的地经度： "+forecastPriceDTO.getDestLongitude());
        log.info("出发地纬度： "+forecastPriceDTO.getDestLatitude());
        log.info("调用计价服务，计算价格");

        ForecastPriceResponse forecastPriceResponse = new ForecastPriceResponse();
        forecastPriceResponse.setPrice(12.34);
        return ResponseResult.success(forecastPriceResponse);
    }
}
