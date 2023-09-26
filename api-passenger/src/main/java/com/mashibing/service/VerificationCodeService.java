package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.ServicePassengerUserClient;
import com.mashibing.remote.ServiceVerificationClient;
import com.mashibing.request.VerificationCodeDTO;
import com.mashibing.response.NumberCodeResponse;
import com.mashibing.response.TokenResponse;
import com.mashibing.util.JwtUtils;
import com.mashibing.util.RedisPrefixUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.mashibing.constant.CommonStatusEnum.*;
import static com.mashibing.constant.IdentityConstant.*;
import static com.mashibing.constant.TokenConstant.*;


@Service
public class VerificationCodeService {

    @Autowired
    private ServiceVerificationClient serviceVerificationClient;
    @Autowired
    private ServicePassengerUserClient servicePassengerUserClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成验证码
     *
     * @param passengerPhone
     * @return
     */
    public ResponseResult generatorCode(String passengerPhone) {
        //调用验证码服务，获取验证码
        System.out.println("调用验证码，获取验证码");
        ResponseResult<NumberCodeResponse> numberCode = serviceVerificationClient.getNumberCode(6);
        int code = numberCode.getData().getNumberCode();
        System.out.println("remote number code" + code);

        System.out.println("存入redis");
        //key,value,过期时间
        String key = RedisPrefixUtils.generateKeyByPhone(passengerPhone);
        //存入redis
        stringRedisTemplate.opsForValue().set(key, code + "", 2, TimeUnit.MINUTES);
        //通过短信服务商，将对应的验证码发到手机上，阿里腾讯信息
        return ResponseResult.success("");
    }

    /**
     * 校验验证码
     *
     * @param passengerPhone
     * @param verificationCode
     * @return
     */
    public ResponseResult checkCode(String passengerPhone, String verificationCode) {
        //根据手机号，去redis读取验证码
        System.out.println("根据手机号，去redis读取验证码");
        //生成key
        String key = RedisPrefixUtils.generateKeyByPhone(passengerPhone);
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
        //判断原来是否有用户，并进行处理
        VerificationCodeDTO dto = new VerificationCodeDTO();
        dto.setPassengerPhone(passengerPhone);
        servicePassengerUserClient.loginOrRegister(dto);
        System.out.println("判断原来是否有用户，并进行处理");
        //颁发令牌
        System.out.println("颁发令牌");
        String accessToken = JwtUtils.generatorToken(passengerPhone, PASSENGER_IDENTITY, ACCESS_TOKEN_TYPE);
        String refreshToken = JwtUtils.generatorToken(passengerPhone, PASSENGER_IDENTITY, REFRESH_TOKEN_TYPE);
        //token存到redis中
        String accessTokenKey = RedisPrefixUtils.generateTokenKey(passengerPhone, PASSENGER_IDENTITY, ACCESS_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, 30, TimeUnit.DAYS);
        String refreshTokenKey = RedisPrefixUtils.generateTokenKey(passengerPhone, PASSENGER_IDENTITY, REFRESH_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(refreshTokenKey, refreshToken, 31, TimeUnit.DAYS);
        //响应
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);
        return ResponseResult.success(tokenResponse);

    }
}
