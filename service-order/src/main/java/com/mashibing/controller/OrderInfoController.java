package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.OrderRequest;
import com.mashibing.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;
    @PostMapping("/add")
    public ResponseResult add(@RequestBody OrderRequest orderRequest, HttpServletRequest httpServletRequest){
        /*String deviceCode = httpServletRequest.getHeader(DEVICE_CODE);
        orderRequest.setDeviceCode(deviceCode);*/
        return orderInfoService.add(orderRequest);
    }
    @GetMapping("/test")
    public ResponseResult getTest(){
        return orderInfoService.testMapper();
    }

    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/to-pick-up-passenger")
    public ResponseResult changeStatus(@RequestBody OrderRequest orderRequest){

        return orderInfoService.toPickUpPassenger(orderRequest);
    }

    /**
     * 到达乘客上车点
     * @param orderRequest
     * @return
     */
    @PostMapping("/arrived-departure")
    public ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest){
        return orderInfoService.arrivedDeparture(orderRequest);
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest){
        return orderInfoService.pickUpPassenger(orderRequest);
    }

    /**
     * 乘客到达目的地，行程终止
     * @param orderRequest
     * @return
     */
    @PostMapping("/passenger-getoff")
    public ResponseResult passengerGetoff(@RequestBody OrderRequest orderRequest){
        return orderInfoService.passengerGetoff(orderRequest);
    }
    
}
