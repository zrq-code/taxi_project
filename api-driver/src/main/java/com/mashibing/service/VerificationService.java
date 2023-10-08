package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServiceDriverUserClient;
import com.mashibing.remote.ServiceVerificationCodeClient;
import com.mashibing.response.DriverUserExistsResponse;
import com.mashibing.response.NumberCodeResponse;
import com.mashibing.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.mashibing.constant.CommonStatusEnum.DRIVER_NOT_EXIST;
import static com.mashibing.constant.DriverCarConstants.DRIVER_NOT_EXISTS;
import static com.mashibing.constant.IdentityConstant.DRIVER_IDENTITY;

@Service
@Slf4j
public class VerificationService {
    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;
    @Autowired
    private ServiceVerificationCodeClient serviceVerificationCodeClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult checkAndSendVerificationCode(String driverPhone) {
        //查询service-driver-user,该手机号是否存在
        ResponseResult<DriverUserExistsResponse> responseResult = serviceDriverUserClient.checkDriver(driverPhone);
        DriverUserExistsResponse data = responseResult.getData();
        if (data.getIfExists() == DRIVER_NOT_EXISTS) {
            return ResponseResult.fail(DRIVER_NOT_EXIST.getCode(), DRIVER_NOT_EXIST.getValue());
        }
        log.info(driverPhone + " 司机存在");
        //获取验证码
        ResponseResult<NumberCodeResponse> numberCode = serviceVerificationCodeClient.getNumberCode(6);
        NumberCodeResponse numberCodeResponse = numberCode.getData();
        int code = numberCodeResponse.getNumberCode();
        log.info("验证码" + code);
        //调用第三方发送验证码

        //存入redis 1:key 2:存入value
        String key = RedisPrefixUtils.generateKeyByPhone(driverPhone, DRIVER_IDENTITY);
        stringRedisTemplate.opsForValue().set(key, code+"", 2, TimeUnit.MINUTES);

        return ResponseResult.success("");
    }
}
