package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author: whua
 * @create: 2019/04/30 15:35
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            HashMap<String, Integer> result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.creatBySuccess("新建地址成功", result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> deleteByShippingId(Integer userId, Integer shippingId) {
        if (shippingId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int rowCount = shippingMapper.deleteByUserIdAndId(userId, shippingId);
        if (rowCount > 0) {
            return ServerResponse.creatBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse<String> update(Integer userId, Shipping shipping) {
        if (shipping == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByUserIdAndIdSelective(shipping);
        if (rowCount > 0) {
            return ServerResponse.creatBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> selectByShippingId(Integer userId, Integer shippingId) {
        if (shippingId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
        if (shipping == null)
            return ServerResponse.createByErrorMessage("查询不到该地址Id");
        return ServerResponse.creatBySuccess(shipping);
    }

    @Override
    public ServerResponse list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageResult = new PageInfo(shippingList);
        return ServerResponse.creatBySuccess(pageResult);
    }
}
