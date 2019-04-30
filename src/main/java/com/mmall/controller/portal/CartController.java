package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: whua
 * @create: 2019/04/30 08:30
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    /**
     * @description: 往购物车添加商品
     * @param: [session, productId, count]
     * @return: com.mmall.common.ServerResponse
     */
    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<CartVO> add(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.add(user.getId(), productId, count);
    }

    /**
     * @description: 更新购物车中产品的数量
     * @param: [session, productId, count]
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVO>
     */
    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<CartVO> update(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.update(user.getId(), productId, count);
    }

    /**
     * @description: 删除购物车中的产品
     * @param: [session, productIds]
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVO>
     */
    @RequestMapping(value = "delete_product.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<CartVO> deleteProduct(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (StringUtils.isBlank(productIds)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    /**
     * @description: 展示用户购物车内容
     * @param: [session]
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVO>
     */
    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<CartVO> list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    /**
     * @description: 购物车全选
     * @param: [session]
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVO>
     */
    @RequestMapping(value = "select_all.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<CartVO> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
    }

    /**
     * @description: 购物车全反选
     * @param: [session]
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVO>
     */
    @RequestMapping(value = "un_select_all.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<CartVO> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    /**
     * @description: 单独选
     * @param: [session, productId]
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVO>
     */
    @RequestMapping(value = "select.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<CartVO> select(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
    }

    /**
     * @description: 单独反选
     * @param: [session, productId]
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVO>
     */
    @RequestMapping(value = "un_select.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<CartVO> unSelect(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

    /**
     * @description: 获取用户购物车产品总数
     * @param: [session]
     * @return: com.mmall.common.ServerResponse<java.lang.Integer>
     */
    @RequestMapping(value = "get_cart_product_count.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<Integer> getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.creatBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }

}
