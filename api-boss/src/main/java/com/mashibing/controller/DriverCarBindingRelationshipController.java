package com.mashibing.controller;

import com.mashibing.dto.DriverCarBindingRelationship;
import com.mashibing.dto.ResponseResult;
import com.mashibing.service.DriverCarBindingRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/driver-car-relationship")
public class DriverCarBindingRelationshipController {
    @Autowired
    private DriverCarBindingRelationshipService driverCarBindingRelationshipService;
    @PostMapping("/bind")
    public ResponseResult bind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship){
        return driverCarBindingRelationshipService.bind(driverCarBindingRelationship);
    }
    @PostMapping("/unbind")
    public ResponseResult unbind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship){
        return driverCarBindingRelationshipService.unbind(driverCarBindingRelationship);
    }

}
