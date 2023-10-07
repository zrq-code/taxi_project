package com.mashibing.controller;

import com.mashibing.dto.DicDistrict;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.DicDistrictMapper;
import com.mashibing.request.ForecastPriceDTO;
import com.mashibing.service.DirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/direction")
public class DirectionController {

    @Autowired
    private DirectionService directionService;
    @Autowired
    DicDistrictMapper dicDistrictMapper;
    @GetMapping("/driving")
    public ResponseResult driving(@RequestBody ForecastPriceDTO forecastPriceDTO){
        return directionService.driving(forecastPriceDTO);
    }

    @GetMapping("/test-map")
    public ResponseResult testMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("address_code", "110000");
        List<DicDistrict> dicDistricts = dicDistrictMapper.selectByMap(map);
        System.out.println(dicDistricts);
        return ResponseResult.success(dicDistricts);
    }
}
