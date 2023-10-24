package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.PointRequestDTO;
import com.mashibing.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointController {
    @Autowired
    private PointService pointService;
    @PostMapping("/upload")
    public ResponseResult upload(@RequestBody PointRequestDTO pointRequestDTO){
        return pointService.upload(pointRequestDTO);
    }
}
