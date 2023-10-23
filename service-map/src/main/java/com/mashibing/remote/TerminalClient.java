package com.mashibing.remote;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.TerminalResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.mashibing.constant.AmapConfigConstants.TERMINAL_ADD_URL;

@Service
public class TerminalClient {
    @Value("${amap.key}")
    private String amapKey;
    @Value("${amap.sid}")
    private String amapSid;
    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult add(String name){
        StringBuilder url = new StringBuilder();
        url.append(TERMINAL_ADD_URL);
        url.append("?");
        url.append("key=").append(amapKey);
        url.append("&").append("sid=").append(amapSid);
        url.append("&").append("name=").append(name);
        //调用高德接口
        ResponseEntity<String> terminalEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        //解析接口
        String body = terminalEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        String tid = data.getString("tid");
        TerminalResponse terminalResponse = new TerminalResponse();
        terminalResponse.setTid(tid);
        return ResponseResult.success(terminalResponse);
    }

}
