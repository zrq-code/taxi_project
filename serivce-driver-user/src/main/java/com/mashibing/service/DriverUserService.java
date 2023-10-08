package com.mashibing.service;

import com.mashibing.dto.DriverUser;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.DriverUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.mashibing.constant.CommonStatusEnum.DRIVER_NOT_EXIST;
import static com.mashibing.constant.DriverCarConstants.DRIVER_STATE_VALID;

@Service
public class DriverUserService {
    @Autowired
    private DriverUserMapper driverUserMapper;

    public ResponseResult getDriverUser() {
        DriverUser driverUser = driverUserMapper.selectById(1);
        return ResponseResult.success(driverUser);
    }

    public ResponseResult<DriverUser> getDriverUserByPhone(String driverPhone){
        HashMap<String, Object> map = new HashMap<>();
        map.put("driver_phone", driverPhone);
        map.put("state", DRIVER_STATE_VALID);
        List<DriverUser> driverUsers = driverUserMapper.selectByMap(map);
        if (driverUsers.isEmpty()){
            return ResponseResult.fail(DRIVER_NOT_EXIST.getCode(), DRIVER_NOT_EXIST.getValue());
        }
        return ResponseResult.success(driverUsers.get(0));
    }

    public ResponseResult addDriverUser(DriverUser driverUser) {
        driverUser.setGmtCreate(LocalDateTime.now());
        driverUser.setGmtModified(LocalDateTime.now());
        driverUserMapper.insert(driverUser);
        return ResponseResult.success();
    }

    public ResponseResult updateDriverUser(DriverUser driverUser) {
        driverUser.setGmtModified(LocalDateTime.now());
        driverUserMapper.updateById(driverUser);
        return ResponseResult.success();
    }
}
