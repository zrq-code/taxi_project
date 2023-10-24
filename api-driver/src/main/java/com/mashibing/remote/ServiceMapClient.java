package com.mashibing.remote;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.PointRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-map")
public interface ServiceMapClient {
    @PostMapping("/point/upload")
    public ResponseResult upload(@RequestBody PointRequestDTO pointRequestDTO);
}
