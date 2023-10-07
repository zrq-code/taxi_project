package com.mashibing.controller;

import com.mashibing.dto.DriverUser;
import com.mashibing.dto.ResponseResult;
import com.mashibing.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserSerivce userSerivce;
    @PutMapping("/user")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser){
        return userSerivce.updateUser(driverUser);
    }
}
