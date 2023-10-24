package com.mashibing.service;

import com.mashibing.dto.Car;
import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServiceDriverUserClient;
import com.mashibing.remote.ServiceMapClient;
import com.mashibing.request.ApiDriverPointRequest;
import com.mashibing.request.PointRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    @Autowired
    private ServiceMapClient serviceMapClient;
    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;
    public ResponseResult upload(ApiDriverPointRequest apiDriverPointRequest){
        //获取carid
        Long carId = apiDriverPointRequest.getCarId();
        //通过carId,获取tid, trid, 调用service-driver-user接口
        ResponseResult<Car> carById = serviceDriverUserClient.getCarById(carId);
        Car car = carById.getData();
        String tid = car.getTid();
        String trid = car.getTrid();
        PointRequestDTO requestDTO = PointRequestDTO.builder()
                .tid(tid)
                .trid(trid)
                .points(apiDriverPointRequest.getPoints())
                .build();
        //调用地图去上传
        return serviceMapClient.upload(requestDTO);
    }
}
