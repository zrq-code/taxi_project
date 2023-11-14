package com.mashibing.remote;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-order")
public interface ServiceOrderClient {
    @PostMapping("/order/add")
    public ResponseResult add(@RequestBody OrderRequest orderRequest);
}
