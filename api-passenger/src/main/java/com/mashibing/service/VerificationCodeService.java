package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServiceVerificationClient;
import com.mashibing.response.NumberCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    @Autowired
    private ServiceVerificationClient serviceVerificationClient;
    //乘客验证码前缀
    private static final String verificationCodePrefix = "passenger-verification-code-";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult generatorCode(String passengerPhone) {
        //调用验证码服务，获取验证码
        System.out.println("调用验证码，获取验证码");
        ResponseResult<NumberCodeResponse> numberCode = serviceVerificationClient.getNumberCode(6);
        int code = numberCode.getData().getNumberCode();
        System.out.println("remote number code" + code);

        System.out.println("存入redis");
        //key,value,过期时间
        String key = verificationCodePrefix + passengerPhone;
        //存入redis
        stringRedisTemplate.opsForValue().set(key, code + "", 2, TimeUnit.MINUTES);
        //通过短信服务商，将对应的验证码发到手机上，阿里腾讯信息
        return ResponseResult.success("");
    }
}
