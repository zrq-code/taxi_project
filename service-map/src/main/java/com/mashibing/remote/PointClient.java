package com.mashibing.remote;

import com.mashibing.dto.ResponseResult;
import com.mashibing.request.PointDTO;
import com.mashibing.request.PointRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.mashibing.constant.AmapConfigConstants.POINT_UPLOAD_URL;

@Service
public class PointClient {
    @Value("${amap.key}")
    private String amapKey;
    @Value("${amap.sid}")
    private String amapSid;
    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult upload(PointRequestDTO pointRequestDTO) {
        StringBuilder url = new StringBuilder();
        url.append(POINT_UPLOAD_URL);
        url.append("?");
        url.append("key=").append(amapKey);
        url.append("&").append("sid=").append(amapSid);
        url.append("&").append("tid=").append(pointRequestDTO.getTid());
        url.append("&").append("trid=").append(pointRequestDTO.getTrid());
        url.append("&").append("points=");
        PointDTO[] points = pointRequestDTO.getPoints();
        url.append("%5B");
        for (PointDTO p : points) {
            url.append("%7B");
            String location = p.getLocation();
            String locatetime = p.getLocatetime();
            url.append("%22location%22").append("%3A").append("%22"+location+"%22").append("%2C");
            url.append("%22locatetime%22").append("%3A").append(locatetime);
            url.append("%7D");
        }
        url.append("%5D");
        System.out.println("URL"+url);
        //调用高德接口
        ResponseEntity<String> pointsEntity = restTemplate.postForEntity(URI.create(url.toString()), null, String.class);
        //解析接口
        String body = pointsEntity.getBody();

        return ResponseResult.success();
    }
}
