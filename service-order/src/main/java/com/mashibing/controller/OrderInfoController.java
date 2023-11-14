package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.OrderRequest;
import com.mashibing.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;
    @PostMapping("/add")
    public ResponseResult add(@RequestBody OrderRequest orderRequest){
        return orderInfoService.add(orderRequest);
    }
    @GetMapping("/test")
    public ResponseResult getTest(){
        return orderInfoService.testMapper();
    }
    
}
