package com.mashibing.service;

import com.mashibing.dto.Car;
import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServiceDriverUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;
    public ResponseResult addCar(Car car) {
        return serviceDriverUserClient.addCar(car);
    }
}
