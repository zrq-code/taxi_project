package com.mashibing.service;

import com.mashibing.dto.DriverCarBindingRelationship;
import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServiceDriverUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverCarBindingRelationshipService {
    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;
    public ResponseResult bind (DriverCarBindingRelationship driverCarBindingRelationship){
        serviceDriverUserClient.bind(driverCarBindingRelationship);
        return ResponseResult.success("");
    }

    public ResponseResult unbind(DriverCarBindingRelationship driverCarBindingRelationship){
        serviceDriverUserClient.unbind(driverCarBindingRelationship);
        return ResponseResult.success("");
    }
}
