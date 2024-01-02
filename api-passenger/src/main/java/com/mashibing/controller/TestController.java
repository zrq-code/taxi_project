package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServiceOrderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "test api passenger";
    }
    @GetMapping("/authTest")
    public ResponseResult authTest(){
        return ResponseResult.success("auth test");
    }
    @GetMapping("/noAuthTest")
    public ResponseResult noAuthTest(){
        return ResponseResult.success("no auth test");
    }
    @Autowired
    ServiceOrderClient serviceOrderClient;
    @GetMapping("/test-real-time-order/{orderId}")
    public String dispatchRealTimeOrder(@PathVariable("orderId") long orderId){
        System.out.println("并发测试： orderId: "+orderId);
        serviceOrderClient.dispatchRealTimeOrder(orderId);
        return "test real-time success";
    }
}
