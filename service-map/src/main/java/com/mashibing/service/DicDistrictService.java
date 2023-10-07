package com.mashibing.service;

import com.mashibing.constant.AmapConfigConstants;
import com.mashibing.dto.DicDistrict;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.DicDistrictMapper;
import com.mashibing.remote.MapDicDistrictClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mashibing.constant.AmapConfigConstants.*;
import static com.mashibing.constant.CommonStatusEnum.MAP_DISTRICT_ERROR;

@Service
public class DicDistrictService {
    @Autowired
    private MapDicDistrictClient mapDicDistrictClient;
    @Autowired
    private DicDistrictMapper dicDistrictMapper;

    public ResponseResult initDicDistrict(String keywords) {
        //?keywords=北京&subdistrict=2&key=<用户的key>
        String dicDistrictResult = mapDicDistrictClient.initDicDistrict(keywords);
        //解析结果
        JSONObject dicDistrictJson = JSONObject.fromObject(dicDistrictResult);
        int status = dicDistrictJson.getInt(AmapConfigConstants.STATUS);
        if (status != 1) {
            return ResponseResult.fail(MAP_DISTRICT_ERROR.getCode(), MAP_DISTRICT_ERROR.getValue());
        }
        JSONArray countryJsonArray = dicDistrictJson.getJSONArray(DISTRICTS);
        for (int i = 0; i < countryJsonArray.size(); i++) {
            JSONObject jsonObject = countryJsonArray.getJSONObject(i);
            String addressCode = jsonObject.getString(ADCODE);
            String name = jsonObject.getString(NAME);
            String parentAddressCode = "0";
            String level = jsonObject.getString(LEVEL);
            insertDicDistrict(addressCode, name, level, parentAddressCode);
            JSONArray proviceJsonArray = jsonObject.getJSONArray(DISTRICTS);
            for (int j = 0; j < proviceJsonArray.size(); j++) {
                JSONObject provinceJsonObject = proviceJsonArray.getJSONObject(j);
                String provinceAddressCode = provinceJsonObject.getString(ADCODE);
                String provinceName = provinceJsonObject.getString(NAME);
                String provinceParentAddressCode = addressCode;
                String provinceLevel = provinceJsonObject.getString(LEVEL);
                insertDicDistrict(provinceAddressCode, provinceName, provinceLevel, provinceParentAddressCode);

                JSONArray cityArray = provinceJsonObject.getJSONArray(DISTRICTS);
                for (int c = 0; c < cityArray.size(); c++) {
                    JSONObject cityJsonObject = cityArray.getJSONObject(c);
                    String cityAddressCode = cityJsonObject.getString(ADCODE);
                    String cityName = cityJsonObject.getString(NAME);
                    String cityParentAddressCode = provinceAddressCode;
                    String cityLevel = cityJsonObject.getString(LEVEL);
                    if (cityLevel.equals(STREET)){
                        continue;
                    }
                    insertDicDistrict(cityAddressCode, cityName, cityLevel, cityParentAddressCode);

                    JSONArray districtArray = cityJsonObject.getJSONArray(DISTRICTS);
                    for (int d = 0; d < districtArray.size(); d++) {
                        JSONObject districtJsonObject = districtArray.getJSONObject(d);
                        String districtAddressCode = districtJsonObject.getString(ADCODE);
                        String districtName = districtJsonObject.getString(NAME);
                        String districtParentAddressCode = cityAddressCode;
                        String districtLevel = districtJsonObject.getString(LEVEL);
                        if (districtLevel.equals(STREET)){
                            continue;
                        }
                        insertDicDistrict(districtAddressCode, districtName, districtLevel, districtParentAddressCode);
                    }
                }
            }
        }
        //插入数据库
        return ResponseResult.success();
    }

    public void insertDicDistrict(String addressCode, String name, String level, String parentAddressCode) {
        DicDistrict dicDistrict = new DicDistrict();
        dicDistrict.setAddressCode(addressCode);
        dicDistrict.setAddressName(name);
        dicDistrict.setLevel(generateLevel(level));
        dicDistrict.setParentAddressCode(parentAddressCode);
        dicDistrictMapper.insert(dicDistrict);
    }

    public int generateLevel(String level) {
        int levelInt = 0;
        if (level.trim().equals("country")) {
            levelInt = 0;
        } else if (level.trim().equals("province")) {
            levelInt = 1;
        } else if (level.trim().equals("city")) {
            levelInt = 2;
        } else if (level.trim().equals("district")) {
            levelInt = 3;
        }
        return levelInt;
    }
}
