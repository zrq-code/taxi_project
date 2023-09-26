package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/users")
    public ResponseResult getUser(HttpServletRequest request){
        //从http请求中获取accessToken
        String authorization = request.getHeader("Authorization");
        //根据accessToken查询
        return userService.getUserByAccessToken(authorization);
    }
}
