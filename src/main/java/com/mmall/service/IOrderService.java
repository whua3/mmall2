package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * @author: whua
 * @create: 2019/05/06 11:39
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
}
