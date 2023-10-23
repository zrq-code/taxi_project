package com.mashibing.remote;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.TerminalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-map")
public interface ServiceMapClient {
    @PostMapping("/terminal/add")
    public ResponseResult<TerminalResponse> addTerminal(@RequestParam String name);

}
