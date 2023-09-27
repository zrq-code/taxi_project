package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.MapDirectionClient;
import com.mashibing.request.ForecastPriceDTO;
import com.mashibing.response.DirectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class DirectionService {
    @Autowired
    private MapDirectionClient mapDirectionClient;
    public ResponseResult driving(@RequestBody ForecastPriceDTO forecastPriceDTO){
        //调用三方地图接口
        DirectionResponse direction = mapDirectionClient.direction(forecastPriceDTO);
        return ResponseResult.success(direction);
    }
}
