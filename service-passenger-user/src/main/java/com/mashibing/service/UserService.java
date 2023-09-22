package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public ResponseResult loginOrRegister(String passengerPhone) {
        System.out.println("user service is called, passengerPhone: " + passengerPhone);
        //根据手机号查询用户信息
        //判断用户信息是否存在
        //如果不存在，插入用户信息
        return ResponseResult.success();
    }
}
