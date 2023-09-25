package com.mashibing.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mashibing.dto.TokenResult;
import lombok.experimental.UtilityClass;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class JwtUtils {
    //sign
    public static final String SIGN = "RICzz!@_@";
    public static final String JWT_KEY_PHONE = "passengerPhone";
    public static final String JWT_KEY_IDENTITY = "identity";
    public static final String JWT_TOKEN_TYPE= "tokenType";

    //生成token
    public static String generatorToken(String passengerPhone, String identity, String tokenType) {
        Map<String, String> map = new HashMap<>();
        map.put(JWT_KEY_PHONE, passengerPhone);
        map.put(JWT_KEY_IDENTITY, identity);
        map.put(JWT_TOKEN_TYPE, tokenType);
        //token过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date time = calendar.getTime();
        //整合map
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        //整合过期时间
        //builder.withExpiresAt(time);
        return builder.sign(Algorithm.HMAC256(SIGN));
    }

    //解析token
    public static TokenResult parseToken(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        return TokenResult.builder()
                .phone(verify.getClaim(JWT_KEY_PHONE).asString())
                .identity(verify.getClaim(JWT_KEY_IDENTITY).asString())
                .build();
    }

    public static void main(String[] args) {
        String res = generatorToken("13914041548", "passenger", "accessToken");
        System.out.println("生成的token: " + res);
        System.out.println("解析的token: " + parseToken(res));
    }
}
