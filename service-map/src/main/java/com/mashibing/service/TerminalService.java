package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.TerminalClient;
import com.mashibing.response.TerminalResponse;
import com.mashibing.response.TrsearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService {
    @Autowired
    private TerminalClient terminalClient;
    public ResponseResult add(String name, String desc){
        return terminalClient.add(name, desc);
    }

    public ResponseResult<List<TerminalResponse>> aroundsearch(String center, Integer radius){
        return terminalClient.aroundsearch(center, radius);
    }

    public ResponseResult<TrsearchResponse> trsearch(String tid , Long starttime , Long endtime){

        return terminalClient.trsearch(tid,starttime,endtime);
    }
}
