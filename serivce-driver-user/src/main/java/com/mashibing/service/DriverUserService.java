package com.mashibing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.dto.DriverCarBindingRelationship;
import com.mashibing.dto.DriverUser;
import com.mashibing.dto.DriverUserWorkStatus;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.DriverCarBindingRelationshipMapper;
import com.mashibing.mapper.DriverUserMapper;
import com.mashibing.mapper.DriverUserWorkStatusMapper;
import com.mashibing.response.OrderDriverResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.mashibing.constant.CommonStatusEnum.AVAILABLE_DRIVER_EMPTY;
import static com.mashibing.constant.CommonStatusEnum.DRIVER_NOT_EXITST;
import static com.mashibing.constant.DriverCarConstants.*;

@Service
public class DriverUserService {
    @Autowired
    private DriverUserMapper driverUserMapper;
    @Autowired
    private DriverUserWorkStatusMapper driverUserWorkStatusMapper;
    @Autowired
    private DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    public ResponseResult getDriverUser() {
        DriverUser driverUser = driverUserMapper.selectById(1);
        return ResponseResult.success(driverUser);
    }

    public ResponseResult<DriverUser> getDriverUserByPhone(String driverPhone) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("driver_phone", driverPhone);
        map.put("state", DRIVER_STATE_VALID);
        List<DriverUser> driverUsers = driverUserMapper.selectByMap(map);
        if (driverUsers.isEmpty()) {
            return ResponseResult.fail(DRIVER_NOT_EXITST.getCode(), DRIVER_NOT_EXITST.getValue());
        }
        return ResponseResult.success(driverUsers.get(0));
    }

    public ResponseResult addDriverUser(DriverUser driverUser) {
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtCreate(now);
        driverUser.setGmtModified(now);
        driverUserMapper.insert(driverUser);
        //初始化司机工作表
        DriverUserWorkStatus status = new DriverUserWorkStatus();
        status.setDriverId(driverUser.getId());
        status.setWorkStatus(DRIVER_WORK_STOP);
        status.setGmtCreate(now);
        status.setGmtModified(now);
        driverUserWorkStatusMapper.insert(status);
        return ResponseResult.success();
    }

    public ResponseResult updateDriverUser(DriverUser driverUser) {
        driverUser.setGmtModified(LocalDateTime.now());
        driverUserMapper.updateById(driverUser);
        return ResponseResult.success();
    }

    public ResponseResult<OrderDriverResponse> getAvailableDriver(@PathVariable("carId") Long carId) {
        QueryWrapper<DriverCarBindingRelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_id", carId);
        queryWrapper.eq("bind_state", DRIVER_CAR_BIND);
        DriverCarBindingRelationship relationship = driverCarBindingRelationshipMapper.selectOne(queryWrapper);
        Long driverId = relationship.getDriverId();
        QueryWrapper<DriverUserWorkStatus> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("driver_id", driverId);
        queryWrapper2.eq("work_status", DRIVER_WORK_START);

        DriverUserWorkStatus driverUserWorkStatus = driverUserWorkStatusMapper.selectOne(queryWrapper2);
        if (null == driverUserWorkStatus) {
            return ResponseResult.fail(AVAILABLE_DRIVER_EMPTY.getCode(), AVAILABLE_DRIVER_EMPTY.getValue());
        }else {
            QueryWrapper<DriverUser> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("id", driverId);
            DriverUser driverUser = driverUserMapper.selectOne(queryWrapper3);
            OrderDriverResponse orderDriverResponse = new OrderDriverResponse();
            orderDriverResponse.setCarId(carId);
            orderDriverResponse.setDriverId(driverId);
            orderDriverResponse.setDriverPhone(driverUser.getDriverPhone());
            return ResponseResult.success(orderDriverResponse);
        }
    }
}
