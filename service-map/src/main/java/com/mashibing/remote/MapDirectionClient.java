package com.mashibing.remote;

import com.mashibing.request.ForecastPriceDTO;
import com.mashibing.response.DirectionResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.mashibing.constant.AmapConfigConstants.*;

@Service
@Slf4j
public class MapDirectionClient {
    @Value("${amap.key}")
    private String amapKey;
    @Autowired
    private RestTemplate restTemplate;

    public DirectionResponse direction(ForecastPriceDTO forecastPriceDTO) {
        //组装请求url
        /**
         * https://restapi.amap.com/v3/direction/driving?origin=116.481028,39.989643&destination=116.465302,40.004717
         * &extensions=base&output=json&key=5f924dd011e7020bb9c558c743539457
         */
        StringBuilder url = new StringBuilder();
        url.append(DIRECTION_URL);
        url.append("?").append("origin=").append(forecastPriceDTO.getDepLongitude()).append(",").append(forecastPriceDTO.getDepLatitude());
        url.append("&");
        url.append("destination=").append(forecastPriceDTO.getDestLongitude()).append(",").append(forecastPriceDTO.getDestLatitude());
        url.append("&");
        url.append("extensions=base");
        url.append("&");
        url.append("output=json");
        url.append("&");
        url.append("key=").append(amapKey);
        log.info(url.toString());
        //调用高德接口
        ResponseEntity<String> directionEntity = restTemplate.getForEntity(url.toString(), String.class);
        //解析接口
        return parseDirectionEntity(directionEntity.getBody());
    }

    private DirectionResponse parseDirectionEntity(String body) {
        DirectionResponse directionResponse = null;
        try {
            JSONObject jsonObject = JSONObject.fromObject(body);
            if (jsonObject.has(STATUS)) {
                int status = jsonObject.getInt(STATUS);
                if (status == 1) {
                    if (jsonObject.has(ROUTE)) {
                        JSONObject route = jsonObject.getJSONObject(ROUTE);
                        JSONArray pathArray = route.getJSONArray(PATHS);
                        JSONObject path = pathArray.getJSONObject(0);
                        directionResponse = new DirectionResponse();
                        if (path.has(DISTANCE)) {
                            directionResponse.setDistance(path.getInt(DISTANCE));
                        }
                        if (path.has(DURATION)) {
                            directionResponse.setDuration(path.getInt(DURATION));
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return directionResponse;
    }
}
