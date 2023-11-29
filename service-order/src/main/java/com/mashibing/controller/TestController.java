package com.mashibing.controller;

import com.mashibing.dto.OrderInfo;
import com.mashibing.mapper.OrderInfoMapper;
import com.mashibing.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    OrderInfoService orderInfoService;
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @GetMapping("/test-real-time-order/{orderId}")
    public String dispatchRealTimeOrder(@PathVariable("orderId") long orderId){
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfoService.dispatchRealTimeOrder(orderInfo);
        return "test real-time success";
    }
}
