package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * @author: whua
 * @create: 2019/04/30 15:34
 */
public interface IShippingService {

    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse<String> deleteByShippingId(Integer userId, Integer shippingId);

    ServerResponse<String> update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> selectByShippingId(Integer userId, Integer shippingId);

    ServerResponse list(Integer userId, int pageNum, int pageSize);
}
