package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.service.DicDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DicDistrictController {
    @Autowired
    private DicDistrictService dicDistrictService;
    @GetMapping("/dic-district")
    public ResponseResult initDistrict(String keywords){
        return dicDistrictService.initDicDistrict(keywords);
    }
}
