package com.mashibing.controller;

import com.mashibing.dto.DriverUser;
import com.mashibing.dto.ResponseResult;
import com.mashibing.response.DriverUserExistsResponse;
import com.mashibing.response.OrderDriverResponse;
import com.mashibing.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.mashibing.constant.DriverCarConstants.DRIVER_EXISTS;
import static com.mashibing.constant.DriverCarConstants.DRIVER_NOT_EXISTS;

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
    public ResponseResult<DriverUserExistsResponse> getUser(@PathVariable("driverPhone") String driverPhone) {
        ResponseResult<DriverUser> driverUserByPhone = driverUserService.getDriverUserByPhone(driverPhone);
        DriverUser data = driverUserByPhone.getData();
        DriverUserExistsResponse driverUserExistsResponse = new DriverUserExistsResponse();
        int ifExists = DRIVER_EXISTS;
        if (data == null) {
            ifExists = DRIVER_NOT_EXISTS;
            driverUserExistsResponse.setDriverPhone(driverPhone);
            driverUserExistsResponse.setIfExists(ifExists);
        }else {
            driverUserExistsResponse.setDriverPhone(data.getDriverPhone());
            driverUserExistsResponse.setIfExists(ifExists);
        }
        return ResponseResult.success(driverUserExistsResponse);
    }

    /**
     * 根据车辆Id查询订单需要的司机信息
     *
     * @param carId
     * @return
     */
    @GetMapping("/get-available-driver/{carId}")
    public ResponseResult<OrderDriverResponse> getAvailableDriver(@PathVariable("carId") Long carId){
        return driverUserService.getAvailableDriver(carId);
    }
}
