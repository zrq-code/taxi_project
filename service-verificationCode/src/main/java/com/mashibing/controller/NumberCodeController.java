package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberCodeController {
    @GetMapping("/numberCode/{size}")
    public ResponseResult numberCode(@PathVariable("size") int size) {

        System.out.println("size" + size);
        //生成验证码
        int resultInt = generateRandom(size);
        //自定义返回值
        NumberCodeResponse response = new NumberCodeResponse();
        response.setNumberCode(resultInt);
        return ResponseResult.success(response);
    }

    private static int generateRandom(int size) {
        double mathRandom = (Math.random() * 9 + 1) * Math.pow(10, size - 1);
        int resultInt = (int) mathRandom;
        System.out.println(resultInt);
        return resultInt;
    }

}
