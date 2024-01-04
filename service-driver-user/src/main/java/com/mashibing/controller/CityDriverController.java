package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.DriverUserMapper;
import com.mashibing.service.CityDriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city-driver")
public class CityDriverController {
    @Autowired
    private CityDriverUserService cityDriverUserService;
    @Autowired
    private DriverUserMapper driverUserMapper;

    /**
     * 根据城市码查询当前城市是否有可用司机
     * @param cityCode
     * @return
     */
    @GetMapping("/is-available-driver")
    public ResponseResult isAvailableDriver(String cityCode){
        return cityDriverUserService.isAvailableDriver(cityCode);
    }

}
