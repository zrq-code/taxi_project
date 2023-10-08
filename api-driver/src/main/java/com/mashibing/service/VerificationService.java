package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VerificationService {
    public ResponseResult checkAndSendVerificationCode(String driverPhone){
        //查询service-driver-user,该手机号是否存在
        //获取验证码
        //调用第三方发送验证码
        //存入redis
        return null;
    }
}
