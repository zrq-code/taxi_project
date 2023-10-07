package com.mashibing.service;

import com.mashibing.dto.DriverUser;
import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServiceDriverUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSerivce {
    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;
    public ResponseResult updateUser(DriverUser driverUser){
        return serviceDriverUserClient.updateDriverUser(driverUser);
    }
}
