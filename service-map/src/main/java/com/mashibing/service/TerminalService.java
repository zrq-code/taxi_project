package com.mashibing.service;

import com.mashibing.dto.ResponseResult;
import com.mashibing.remote.TerminalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalService {
    @Autowired
    private TerminalClient terminalClient;
    public ResponseResult add(String name){
        return terminalClient.add(name);
    }
}
