package com.mashibing.interceptor;

import com.mashibing.dto.ResponseResult;
import com.mashibing.dto.TokenResult;
import com.mashibing.util.JwtUtils;
import com.mashibing.util.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static com.mashibing.constant.TokenConstant.ACCESS_TOKEN_TYPE;

public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = true;
        String resultString = "";
        String token = request.getHeader("Authorization");
        //解析token
        TokenResult tokenResult = JwtUtils.checkToken(token);
        if (tokenResult == null) {
            resultString = "token invalid";
            result = false;
        } else {
            //拼接key
            String phone = tokenResult.getPhone();
            String identity = tokenResult.getIdentity();
            String tokenKey = RedisPrefixUtils.generateTokenKey(phone, identity, ACCESS_TOKEN_TYPE);
            //从redis中取出token
            String tokenRedis = stringRedisTemplate.opsForValue().get(tokenKey);
            //比价传入token和redis中token是否相等
            if (StringUtils.isBlank(tokenRedis) || !token.trim().equals(tokenRedis.trim())) {
                resultString = "token invalid";
                result = false;
            }
        }
        if (!result){
            PrintWriter out = response.getWriter();
            out.print(JSONObject.fromObject(ResponseResult.fail(resultString)).toString());
        }
        return true;
    }
}
