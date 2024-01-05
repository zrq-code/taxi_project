package com.mashibing.remote;

import com.mashibing.constant.AmapConfigConstants;
import com.mashibing.dto.ResponseResult;
import com.mashibing.response.TerminalResponse;
import com.mashibing.response.TrsearchResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.mashibing.constant.AmapConfigConstants.TERMINAL_ADD_URL;
import static com.mashibing.constant.AmapConfigConstants.TERMINAL_AROUND_SEARCH_URL;

@Service
public class TerminalClient {
    @Value("${amap.key}")
    private String amapKey;
    @Value("${amap.sid}")
    private String amapSid;
    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult add(String name, String desc) {
        StringBuilder url = new StringBuilder();
        url.append(TERMINAL_ADD_URL);
        url.append("?").append("key=").append(amapKey);
        url.append("&").append("sid=").append(amapSid);
        url.append("&").append("name=").append(name);
        url.append("&").append("desc=").append(desc);
        //调用高德接口
        System.out.println("新增终端 " + url);
        ResponseEntity<String> terminalEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        //解析接口
        String body = terminalEntity.getBody();
        System.out.println("body " + body);
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        String tid = data.getString("tid");
        TerminalResponse terminalResponse = new TerminalResponse();
        terminalResponse.setTid(tid);
        return ResponseResult.success(terminalResponse);
    }

    public ResponseResult<List<TerminalResponse>> aroundsearch(String center, Integer radius) {
        StringBuilder url = new StringBuilder();
        url.append(TERMINAL_AROUND_SEARCH_URL);
        url.append("?").append("key=").append(amapKey);
        url.append("&").append("sid=").append(amapSid);
        url.append("&").append("center=").append(center);
        url.append("&").append("radius=").append(radius);
        //调用高德接口
        System.out.println("查询周边终端 " + url);
        ResponseEntity<String> aroundsearchEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        //解析接口
        String body = aroundsearchEntity.getBody();
        System.out.println("body " + body);
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        JSONArray jsonArray = data.getJSONArray("results");
        List<TerminalResponse> terminalResponses = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            TerminalResponse terminalResponse = new TerminalResponse();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Long carId = Long.parseLong(jsonObject.getString("desc"));
            String tid = jsonObject.getString("tid");
            JSONObject location = jsonObject.getJSONObject("location");
            String longitude = location.getString("longitude");
            String latitude = location.getString("latitude");
            terminalResponse.setCarId(carId);
            terminalResponse.setTid(tid);
            terminalResponse.setLongitude(longitude);
            terminalResponse.setLatitude(latitude);
            terminalResponses.add(terminalResponse);
        }
        return ResponseResult.success(terminalResponses);
    }

    public ResponseResult<TrsearchResponse> trsearch(String tid, Long starttime , Long endtime){
        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_TRSEARCH);
        url.append("?");
        url.append("key="+amapKey);
        url.append("&");
        url.append("sid="+amapSid);
        url.append("&");
        url.append("tid="+tid);
        url.append("&");
        url.append("starttime="+starttime);
        url.append("&");
        url.append("endtime="+endtime);

        System.out.println("高德地图查询轨迹结果请求："+url.toString());
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toString(), String.class);
        System.out.println("高德地图查询轨迹结果响应："+forEntity.getBody());

        JSONObject result = JSONObject.fromObject(forEntity.getBody());
        JSONObject data = result.getJSONObject("data");
        int counts = data.getInt("counts");
        if (counts == 0){
            return null;
        }
        JSONArray tracks = data.getJSONArray("tracks");
        long driveMile = 0L;
        long driveTime = 0L;
        for (int i=0;i<tracks.size();i++){
            JSONObject jsonObject = tracks.getJSONObject(i);

            long distance = jsonObject.getLong("distance");
            driveMile += distance;

            long time = jsonObject.getLong("time") / (1000 * 60);
            driveTime += time;
        }
        TrsearchResponse trsearchResponse = new TrsearchResponse();
        trsearchResponse.setDriveMile(driveMile);
        trsearchResponse.setDriveTime(driveTime);
        return ResponseResult.success(trsearchResponse);

    }
}
