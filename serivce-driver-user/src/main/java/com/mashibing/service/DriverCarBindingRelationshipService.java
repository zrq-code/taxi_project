package com.mashibing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.dto.DriverCarBindingRelationship;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.DriverCarBindingRelationshipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.mashibing.constant.CommonStatusEnum.*;
import static com.mashibing.constant.DriverCarConstants.DRIVER_CAR_BIND;
import static com.mashibing.constant.DriverCarConstants.DRIVER_CAR_UNBIND;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rick zhou
 * @since 2023-10-08
 */
@Service
public class DriverCarBindingRelationshipService {
    @Autowired
    private DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;
    public ResponseResult bind(DriverCarBindingRelationship driverCarBindingRelationship) {
        //判断如果参数中的车辆和司机，已经做过绑定，则不允许再次绑定
        QueryWrapper<DriverCarBindingRelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id", driverCarBindingRelationship.getDriverId());
        queryWrapper.eq("car_id", driverCarBindingRelationship.getCarId());
        queryWrapper.eq("bind_state", DRIVER_CAR_BIND);
        Integer integer = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if (integer>0){
            return ResponseResult.fail(DRIVER_CAR_BIND_EXIST.getCode(), DRIVER_CAR_BIND_EXIST.getValue());
        }
        //司机被绑定
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id", driverCarBindingRelationship.getDriverId());
        queryWrapper.eq("bind_state", DRIVER_CAR_BIND);
        integer = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if (integer>0){
            return ResponseResult.fail(DRIVER_BIND_EXIST.getCode(), DRIVER_BIND_EXIST.getValue());
        }

        //司机被绑定
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_id", driverCarBindingRelationship.getCarId());
        queryWrapper.eq("bind_state", DRIVER_CAR_BIND);
        integer = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if (integer>0){
            return ResponseResult.fail(CAR_BIND_EXIST.getCode(), CAR_BIND_EXIST.getValue());
        }

        LocalDateTime now = LocalDateTime.now();
        driverCarBindingRelationship.setBindingTime(now);
        driverCarBindingRelationship.setBindState(DRIVER_CAR_BIND);
        driverCarBindingRelationshipMapper.insert(driverCarBindingRelationship);
        return ResponseResult.success("");
    }

    public ResponseResult unbind(DriverCarBindingRelationship driverCarBindingRelationship) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("driver_id", driverCarBindingRelationship.getDriverId());
        map.put("car_id", driverCarBindingRelationship.getCarId());
        map.put("bind_state", DRIVER_CAR_BIND);
        List<DriverCarBindingRelationship> ships = driverCarBindingRelationshipMapper.selectByMap(map);
        if (ships.isEmpty()){
            return ResponseResult.fail(DRIVER_CAR_BIND_NOT_EXIST.getCode(), DRIVER_CAR_BIND_NOT_EXIST.getValue());
        }
        DriverCarBindingRelationship ship = ships.get(0);
        ship.setBindState(DRIVER_CAR_UNBIND);
        ship.setUnBindingTime(LocalDateTime.now());
        driverCarBindingRelationshipMapper.updateById(ship);
        return ResponseResult.success("");
    }
}
