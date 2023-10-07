package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private DriverUserService driverUserService;
    @GetMapping("/test")
    public String test(){
        return "service-driver-user success";
    }

    @GetMapping("/test-db")
    public ResponseResult testDB(){
        return driverUserService.getDriverUser();
    }
}
