package com.mashibing.controller;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.TerminalResponse;
import com.mashibing.response.TrsearchResponse;
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

    /**
     * 轨迹查询
     * @param tid
     * @param starttime
     * @param endtime
     * @return
     */
    @PostMapping("/trsearch")
    public ResponseResult<TrsearchResponse> trsearch(String tid, Long starttime , Long endtime){

        return terminalService.trsearch(tid,starttime,endtime);
    }
}
