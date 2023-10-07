package com.mashibing.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.mashibing.constant.AmapConfigConstants.DISTRICT_URL;

@Service
public class MapDicDistrictClient {
    @Value("${amap.key}")
    private String amapKey;
    @Autowired
    private RestTemplate restTemplate;

    public String initDicDistrict(String keywords) {
        //?keywords=北京&subdistrict=2&key=<用户的key>
        StringBuilder url = new StringBuilder();
        url.append(DISTRICT_URL);
        url.append("?");
        url.append("keywords=").append(keywords);
        url.append("&");
        url.append("subdistrict=3");
        url.append("&");
        url.append("key=").append(amapKey);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toString(), String.class);
        return forEntity.getBody();
    }
}
