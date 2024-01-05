package com.mashibing.remote;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.TerminalResponse;
import com.mashibing.response.TrsearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("service-map")
public interface ServiceMapClient {
    @PostMapping("/terminal/aroundsearch")
    public ResponseResult<List<TerminalResponse>> aroundSearch(@RequestParam String center, @RequestParam Integer radius);

    @PostMapping("/terminal/trsearch")
    public ResponseResult<TrsearchResponse> trsearch(@RequestParam String tid , @RequestParam Long starttime , @RequestParam Long endtime);
}
