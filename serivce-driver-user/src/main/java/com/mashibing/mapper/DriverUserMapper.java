package com.mashibing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mashibing.dto.DriverUser;
import feign.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverUserMapper extends BaseMapper<DriverUser> {
    public int selectDriverUserCountByCityCode(@Param("cityCode") String cityCode);
    public int select1(String arg);
}
