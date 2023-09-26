package com.mashibing.service;

import com.mashibing.dto.PassengerUser;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.PassengerUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.mashibing.constant.CommonStatusEnum.USER_NOT_EXISTS;

@Service
public class UserService {
    @Autowired
    private PassengerUserMapper passengerUserMapper;

    public ResponseResult loginOrRegister(String passengerPhone) {
        System.out.println("user service is called, passengerPhone: " + passengerPhone);
        //根据手机号查询用户信息
        HashMap<String, Object> map = new HashMap<>();
        map.put("passenger_phone", passengerPhone);
        List<PassengerUser> passengerUsers = passengerUserMapper.selectByMap(map);
        //判断用户信息是否存在
        if (passengerUsers.size() == 0) {
            //如果不存在，插入用户信息
            PassengerUser user = new PassengerUser();
            user.setPassengerName("奇奇");
            user.setPassengerGender(0);
            user.setPassengerPhone(passengerPhone);
            user.setState(0);
            user.setGmtCreate(LocalDateTime.now());
            user.setGmtModified(LocalDateTime.now());
            passengerUserMapper.insert(user);
        }
        return ResponseResult.success();
    }

    public ResponseResult getUSerByPhone(String passengerPhone){
        //根据手机号查询用户信息
        HashMap<String, Object> map = new HashMap<>();
        map.put("passenger_phone", passengerPhone);
        List<PassengerUser> passengerUsers = passengerUserMapper.selectByMap(map);
        if (passengerUsers.size() == 0){
            return ResponseResult.fail(USER_NOT_EXISTS.getCode(), USER_NOT_EXISTS.getValue());
        }else {
            return ResponseResult.success(passengerUsers.get(0));
        }
    }
}
