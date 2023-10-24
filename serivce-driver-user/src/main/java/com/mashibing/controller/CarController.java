package com.mashibing.controller;


import com.mashibing.dto.Car;
import com.mashibing.dto.ResponseResult;
import com.mashibing.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rick zhou
 * @since 2023-10-07
 */
@RestController
public class CarController {
    @Autowired
    private CarService carService;
    @PostMapping("/car")
    public ResponseResult addCar(@RequestBody Car car){
        return carService.addCar(car);
    }

    @GetMapping("/car")
    public ResponseResult getCarById(Long carId){
        return carService.getCarById(carId);
    }
}
