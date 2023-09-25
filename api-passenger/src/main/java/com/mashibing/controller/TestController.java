package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "test api passenger";
    }
    @GetMapping("/authTest")
    public ResponseResult authTest(){
        return ResponseResult.success("auth test");
    }
    @GetMapping("/noAuthTest")
    public ResponseResult noAuthTest(){
        return ResponseResult.success("no auth test");

    }
}
