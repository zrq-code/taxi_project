package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServiceDriverUserClient;
import com.mashibing.remote.ServiceVerificationCodeClient;
import com.mashibing.response.DriverUserExistsResponse;
import com.mashibing.response.NumberCodeResponse;
import com.mashibing.response.TokenResponse;
import com.mashibing.util.JwtUtils;
import com.mashibing.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.mashibing.constant.CommonStatusEnum.DRIVER_NOT_EXIST;
import static com.mashibing.constant.CommonStatusEnum.VERIFICATION_CODE_ERROR;
import static com.mashibing.constant.DriverCarConstants.DRIVER_NOT_EXISTS;
import static com.mashibing.constant.IdentityConstant.DRIVER_IDENTITY;
import static com.mashibing.constant.TokenConstant.ACCESS_TOKEN_TYPE;
import static com.mashibing.constant.TokenConstant.REFRESH_TOKEN_TYPE;

@Service
@Slf4j
public class VerificationCodeService {
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
        stringRedisTemplate.opsForValue().set(key, code + "", 2, TimeUnit.MINUTES);

        return ResponseResult.success("");
    }

    /**
     * 校验验证码
     *
     * @param driverPhone
     * @param verificationCode
     * @return
     */
    public ResponseResult checkCode(String driverPhone, String verificationCode) {
        //根据手机号，去redis读取验证码
        System.out.println("根据手机号，去redis读取验证码");
        //生成key
        String key = RedisPrefixUtils.generateKeyByPhone(driverPhone, DRIVER_IDENTITY);
        //根据key生成value
        String codeRedis = stringRedisTemplate.opsForValue().get(key);
        System.out.println("redis中的value: " + codeRedis);
        //校验验证码
        if (StringUtils.isBlank(codeRedis)) {
            return ResponseResult.fail(VERIFICATION_CODE_ERROR.getCode(), VERIFICATION_CODE_ERROR.getValue());
        }
        if (!verificationCode.trim().equals(codeRedis)) {
            return ResponseResult.fail(VERIFICATION_CODE_ERROR.getCode(), VERIFICATION_CODE_ERROR.getValue());
        }

        System.out.println("校验验证码");

        //颁发令牌
        System.out.println("颁发令牌");
        String accessToken = JwtUtils.generatorToken(driverPhone, DRIVER_IDENTITY, ACCESS_TOKEN_TYPE);
        String refreshToken = JwtUtils.generatorToken(driverPhone, DRIVER_IDENTITY, REFRESH_TOKEN_TYPE);
        //token存到redis中
        String accessTokenKey = RedisPrefixUtils.generateTokenKey(driverPhone, DRIVER_IDENTITY, ACCESS_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, 30, TimeUnit.DAYS);
        String refreshTokenKey = RedisPrefixUtils.generateTokenKey(driverPhone, DRIVER_IDENTITY, REFRESH_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(refreshTokenKey, refreshToken, 31, TimeUnit.DAYS);
        //响应
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);
        return ResponseResult.success(tokenResponse);

    }
}
