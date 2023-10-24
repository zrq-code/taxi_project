package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.TerminalResponse;
import com.mashibing.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/terminal")
public class TerminalController {
    @Autowired
    private TerminalService terminalService;

    @PostMapping("/add")
    public ResponseResult<TerminalResponse> add(String name, String desc){
        return terminalService.add(name,desc);
    }

    @PostMapping("/aroundsearch")
    public ResponseResult aroundSearch(String center, Integer radius){
        return terminalService.aroundsearch(center, radius);
    }
}
