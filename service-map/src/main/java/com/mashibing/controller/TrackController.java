package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.TrackResponse;
import com.mashibing.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/track")
public class TrackController {
    @Autowired
    private TrackService trackService;

    @PostMapping("/add")
    public ResponseResult<TrackResponse> add(String tid){
        return trackService.add(tid);
    }
}
