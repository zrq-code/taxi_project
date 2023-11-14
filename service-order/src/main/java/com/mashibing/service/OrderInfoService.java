package com.mashibing.service;

import com.mashibing.dto.OrderInfo;
import com.mashibing.dto.ResponseResult;
import com.mashibing.mapper.OrderInfoMapper;
import com.mashibing.request.OrderRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rick zhou
 * @since 2023-10-25
 */
@Service
public class OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    public ResponseResult add(OrderRequest orderRequest) {
        OrderInfo order = new OrderInfo();
        BeanUtils.copyProperties(orderRequest, order);
        orderInfoMapper.insert(order);
        return ResponseResult.success();
    }
    public ResponseResult testMapper() {
        OrderInfo order = new OrderInfo();
        order.setAddress("10001");
        orderInfoMapper.insert(order);
        return ResponseResult.success();
    }
}
