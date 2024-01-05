package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServiceDriverUserClient;
import com.mashibing.remote.ServiceOrderClient;
import com.mashibing.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ApiDriverOrderInfoService {
    @Autowired
    ServiceOrderClient serviceOrderClient;

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult toPickUpPassenger(OrderRequest orderRequest){
        return serviceOrderClient.toPickUpPassenger(orderRequest);
    }

    /**
     * 到达乘客起点
     * @param orderRequest
     * @return
     */
    public ResponseResult arrivedDeparture(OrderRequest orderRequest){
        return serviceOrderClient.arrivedDeparture(orderRequest);
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest){
        return  serviceOrderClient.pickUpPassenger(orderRequest);
    }

    /**
     * 乘客下车
     * @param orderRequest
     * @return
     */
    public ResponseResult passengerGetoff(OrderRequest orderRequest){
        return serviceOrderClient.passengerGetoff(orderRequest);
    }

}
