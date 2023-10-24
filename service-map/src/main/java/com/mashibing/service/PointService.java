package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.PointClient;
import com.mashibing.request.PointRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    @Autowired
    private PointClient pointClient;
    public ResponseResult upload(PointRequestDTO pointRequestDTO){
        return pointClient.upload(pointRequestDTO);
    }
}
