package com.mashibing.service;

import com.mashibing.dto.Car;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.CarMapper;
import com.mashibing.remote.ServiceMapClient;
import com.mashibing.response.TerminalResponse;
import com.mashibing.response.TrackResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rick zhou
 * @since 2023-10-07
 */
@Service
public class CarService {
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private ServiceMapClient serviceMapClient;
    public ResponseResult addCar(Car car){
        LocalDateTime now = LocalDateTime.now();
        car.setGmtCreate(now);
        car.setGmtModified(now);
        //获得此车辆对应tid
        ResponseResult<TerminalResponse> result = serviceMapClient.addTerminal(car.getVehicleNo());
        String tid = result.getData().getTid();
        car.setTid(tid);
        //获得此车辆轨迹id
        ResponseResult<TrackResponse> trackResponse = serviceMapClient.addTrack(tid);
        String trid = trackResponse.getData().getTrid();
        String trname = trackResponse.getData().getTrname();
        car.setTrid(trid);
        car.setTrname(trname);
        carMapper.insert(car);
        return ResponseResult.success();
    }

    public ResponseResult<Car> getCarById(Long id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        List<Car> cars = carMapper.selectByMap(map);
        return ResponseResult.success(cars.get(0));
    }
}
