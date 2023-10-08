package com.mashibing.controller;

import com.mashibing.dto.DriverUser;
import com.mashibing.dto.ResponseResult;
import com.mashibing.response.DriverUserExistsResponse;
import com.mashibing.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private DriverUserService driverUserService;

    @PostMapping("/user")
    public ResponseResult addUser(@RequestBody DriverUser driverUser) {
        return driverUserService.addDriverUser(driverUser);
    }

    @PutMapping("/user")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser) {
        return driverUserService.updateDriverUser(driverUser);
    }

    @GetMapping("/check-driver/{driverPhone}")
    public ResponseResult getUser(@PathVariable("driverPhone") String driverPhone) {
        ResponseResult<DriverUser> driverUserByPhone = driverUserService.getDriverUserByPhone(driverPhone);
        DriverUser data = driverUserByPhone.getData();
        DriverUserExistsResponse driverUserExistsResponse = new DriverUserExistsResponse();
        int ifExists = 1;
        if (data == null) {
            ifExists = 0;
            driverUserExistsResponse.setDriverPhone(driverPhone);
            driverUserExistsResponse.setIfExists(ifExists);
        }else {
            driverUserExistsResponse.setDriverPhone(data.getDriverPhone());
            driverUserExistsResponse.setIfExists(ifExists);
        }
        return ResponseResult.success(driverUserExistsResponse);
    }
}
