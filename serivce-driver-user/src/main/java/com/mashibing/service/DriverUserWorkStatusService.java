package com.mashibing.service;

import com.mashibing.dto.DriverUserWorkStatus;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.DriverUserWorkStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rick zhou
 * @since 2023-10-08
 */
@Service
public class DriverUserWorkStatusService {
    @Autowired
    private DriverUserWorkStatusMapper driverUserWorkStatusMapper;
    public ResponseResult changeWorkStatus(Long driverId, Integer workStatus){
        HashMap<String, Object> map = new HashMap<>();
        map.put("driver_id", driverId);
        List<DriverUserWorkStatus> list = driverUserWorkStatusMapper.selectByMap(map);
        if (list.isEmpty()){
          return ResponseResult.fail("");
        }
        DriverUserWorkStatus driverUserWorkStatus = list.get(0);
        driverUserWorkStatus.setWorkStatus(workStatus);
        driverUserWorkStatusMapper.updateById(driverUserWorkStatus);
        return ResponseResult.success();
    }
}
