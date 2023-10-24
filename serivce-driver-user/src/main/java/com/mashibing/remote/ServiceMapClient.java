package com.mashibing.remote;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.TerminalResponse;
import com.mashibing.response.TrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-map")
public interface ServiceMapClient {
    @PostMapping("/terminal/add")
    public ResponseResult<TerminalResponse> addTerminal(@RequestParam String name);

    @PostMapping("/track/add")
    public ResponseResult<TrackResponse> addTrack(@RequestParam String tid);
}
