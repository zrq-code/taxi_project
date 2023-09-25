package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.dto.TokenResult;
import com.mashibing.response.TokenResponse;
import com.mashibing.util.JwtUtils;
import com.mashibing.util.RedisPrefixUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.mashibing.constant.CommonStatusEnum.TOKEN_ERROR;
import static com.mashibing.constant.TokenConstant.ACCESS_TOKEN_TYPE;
import static com.mashibing.constant.TokenConstant.REFRESH_TOKEN_TYPE;

@Service
public class TokenService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult refreshToken(String refreshToken) {
        //解析refreshToken
        TokenResult tokenResult = JwtUtils.checkToken(refreshToken);
        if (tokenResult == null) {
            return ResponseResult.fail(TOKEN_ERROR.getCode(), TOKEN_ERROR.getValue());
        }
        String phone = tokenResult.getPhone();
        String identity = tokenResult.getIdentity();
        //读取redis中refreshToken
        String refreshTokenKey = RedisPrefixUtils.generateTokenKey(phone, identity, REFRESH_TOKEN_TYPE);
        String refreshTokenRedis = stringRedisTemplate.opsForValue().get(refreshTokenKey);

        //校验refreshToken
        if (StringUtils.isBlank(refreshTokenRedis) || !refreshToken.trim().equals(refreshTokenRedis.trim())) {
            return ResponseResult.fail(TOKEN_ERROR.getCode(), TOKEN_ERROR.getValue());
        }
        //生成双Token
        String newRefreshToken = JwtUtils.generatorToken(phone, identity, REFRESH_TOKEN_TYPE);
        String newAccessToken = JwtUtils.generatorToken(phone, identity, ACCESS_TOKEN_TYPE);
        String newAccessTokenKey = RedisPrefixUtils.generateTokenKey(phone, identity, ACCESS_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(newAccessTokenKey, newAccessToken, 30, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(refreshTokenKey, newRefreshToken, 31, TimeUnit.DAYS);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setRefreshToken(newRefreshToken);
        tokenResponse.setAccessToken(newAccessToken);
        return ResponseResult.success(tokenResponse);

    }
}
