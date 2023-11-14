package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServiceOrderClient;
import com.mashibing.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ServiceOrderService {
    @Autowired
    private ServiceOrderClient serviceOrderClient;
    public ResponseResult add(@RequestBody OrderRequest orderRequest){
        return serviceOrderClient.add(orderRequest);
    }
}
