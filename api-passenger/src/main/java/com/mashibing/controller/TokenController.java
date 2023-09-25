package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.TokenResponse;
import com.mashibing.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @PostMapping("/token-refresh")
    public ResponseResult refreshToken(@RequestBody TokenResponse tokenResponse) {
        String refreshToken = tokenResponse.getRefreshToken();
        System.out.println("原来的refreshToken: " + refreshToken);
        return tokenService.refreshToken(refreshToken);
    }
}
