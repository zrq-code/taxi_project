package com.mashibing.service;

import com.mashibing.dto.Car;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rick zhou
 * @since 2023-10-07
 */
@Service
public class CarService {
    @Autowired
    private CarMapper carMapper;
    public ResponseResult addCar(Car car){
        LocalDateTime now = LocalDateTime.now();
        car.setGmtCreate(now);
        car.setGmtModified(now);
        carMapper.insert(car);
        return ResponseResult.success();
    }
}
