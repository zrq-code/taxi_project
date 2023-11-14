package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.OrderRequest;
import com.mashibing.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private ServiceOrderService serviceOrderService;

    /**
     * 创建订单
     * @param orderRequest
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody OrderRequest orderRequest){
        return serviceOrderService.add(orderRequest);
    }
}
