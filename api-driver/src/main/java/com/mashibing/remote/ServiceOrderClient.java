package com.mashibing.remote;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-order")
public interface ServiceOrderClient {
    @PostMapping("/order/to-pick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest);

    @PostMapping("/order/arrived-departure")
    public ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest);

    @PostMapping("/order/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest);

    @PostMapping("/order/passenger-getoff")
    public ResponseResult passengerGetoff(@RequestBody OrderRequest orderRequest);

}
