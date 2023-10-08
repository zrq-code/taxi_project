package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.VerificationCodeDTO;
import com.mashibing.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationCodeController {
    @Autowired
    private VerificationService verificationService;
    @GetMapping("/verification-code")
    public ResponseResult verificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){
        return verificationService.checkAndSendVerificationCode(verificationCodeDTO.getDriverPhone());
    }
}
