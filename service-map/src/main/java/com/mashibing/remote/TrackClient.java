package com.mashibing.remote;

import com.mashibing.dto.ResponseResult;
import com.mashibing.response.TrackResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.mashibing.constant.AmapConfigConstants.TRACE_ADD_URL;

@Service
public class TrackClient {
    @Value("${amap.key}")
    private String amapKey;
    @Value("${amap.sid}")
    private String amapSid;
    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult<TrackResponse> add(String tid) {
        StringBuilder url = new StringBuilder();
        url.append(TRACE_ADD_URL);
        url.append("?");
        url.append("key=").append(amapKey);
        url.append("&").append("sid=").append(amapSid);
        url.append("&").append("tid=").append(tid);
        //调用高德接口
        System.out.println("新增track " + url);
        ResponseEntity<String> terminalEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        //解析接口
        String body = terminalEntity.getBody();
        System.out.println("body " + body);
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        String trid = data.getString("trid");
        String trname = "";
        if (data.has("trname")) {
            trname = StringUtils.isBlank(data.getString("trname")) ? "" : data.getString("trname");
        }
        TrackResponse trackResponse = new TrackResponse();
        trackResponse.setTrid(trid);
        trackResponse.setTrname(trname);
        return ResponseResult.success(trackResponse);
    }
}
