package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.TrackClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {
    @Autowired
    private TrackClient trackClient;
    public ResponseResult add(String tid){
        return trackClient.add(tid);
    }
}
