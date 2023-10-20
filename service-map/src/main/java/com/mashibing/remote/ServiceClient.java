package com.mashibing.remote;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.ServiceResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.mashibing.constant.AmapConfigConstants.*;

@Service
public class ServiceClient {
    @Value("${amap.key}")
    private String amapKey;
    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult add(String name){
        StringBuilder url = new StringBuilder();
        url.append(SERVICE_ADD_URL);
        url.append("?");
        url.append("key=").append(amapKey);
        url.append("&");
        url.append("name=").append(name);
        //调用高德接口
        ResponseEntity<String> directionEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        //解析接口
        String body = directionEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        String sid = data.getString("sid");
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setSid(sid);
        return ResponseResult.success(serviceResponse);
    }
}
