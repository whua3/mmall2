package com.mmall.controller.backend;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: whua
 * @create: 2019/05/07 15:06
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            return iOrderService.manageList(pageNum, pageSize);
//        }
//        return ServerResponse.createByErrorMessage("无权限操作");
        //全部通过拦截器验证是否登录以及权限
        return iOrderService.manageList(pageNum, pageSize);
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse orderDetail(Long orderNo) {
        //全部通过拦截器验证是否登录以及权限
        return iOrderService.manageDetail(orderNo);
    }

    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse orderSearch(Long orderNo,
                                      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        //全部通过拦截器验证是否登录以及权限
        return iOrderService.manageSearch(orderNo, pageNum, pageSize);
    }

    @RequestMapping(value = "send_goods.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse orderSendGoods(Long orderNo) {
        //全部通过拦截器验证是否登录以及权限
        return iOrderService.manageSendGoods(orderNo);
    }

}
