package com.mashibing.remote;

import com.mashibing.dto.Car;
import com.mashibing.dto.DriverCarBindingRelationship;
import com.mashibing.dto.DriverUser;
import com.mashibing.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-driver-user")
public interface ServiceDriverUserClient {

    @PostMapping("/user")
    public ResponseResult addDriverUser(@RequestBody DriverUser driverUser);

    @PutMapping("/user")
    public ResponseResult updateDriverUser(@RequestBody DriverUser driverUser);

    @PostMapping("/car")
    public ResponseResult addCar(@RequestBody Car car);

    @PostMapping("/driver-car-relationship/bind")
    public ResponseResult bind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship);
    @PostMapping("/driver-car-relationship/unbind")
    public ResponseResult unbind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship);
}
