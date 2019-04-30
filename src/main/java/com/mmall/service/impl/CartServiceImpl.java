package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVO;
import com.mmall.vo.CartVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: whua
 * @create: 2019/04/30 08:33
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse<CartVO> add(Integer userId, Integer productId, Integer count) {
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            //产品不在购物车中，需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            int rowCount = cartMapper.insert(cartItem);
            if (rowCount <= 0) {
                ServerResponse.createByErrorMessage("产品加入购物车失败");
            }
        } else {
            //这个产品已经在购物车里了，数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            int rowCount = cartMapper.updateByPrimaryKeySelective(cart);
            if (rowCount <= 0) {
                ServerResponse.createByErrorMessage("产品加入购物车失败");
            }
        }
        return this.list(userId);
    }

    private CartVO getCartVOLimit(Integer userId) {
        CartVO cartVO = new CartVO();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVO> cartProductVOList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cartItem.getId());
                cartProductVO.setUserId(userId);
                cartProductVO.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStock(product.getStock());

                    //判断库存
                    int buyLimitCount = 0;//能买的数量
                    if (product.getStock() >= cartItem.getQuantity()) {
                        //库存充足
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        // 库存不够
                        buyLimitCount = product.getStock();
                        cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVO.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVO.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVO.getQuantity()));
                    cartProductVO.setProductChecked(cartItem.getChecked());
                }

                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    // 如果已经勾选，添加到整个购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setAllChecked(this.getAllCheckStatus(userId));
        cartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return cartVO;
    }

    private boolean getAllCheckStatus(Integer userId) {
        if (userId == null)
            return false;
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }

    @Override
    public ServerResponse<CartVO> update(Integer userId, Integer productId, Integer count) {
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        } else {
            return ServerResponse.createByErrorMessage("未找到更新的购物车产品，更新失败");
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVO> deleteProduct(Integer userId, String productIds) {
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int rowCount = cartMapper.deleteByUserIdProductIds(userId, productList);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("购物车中不存在这些产品，删除失败");
        }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVO> list(Integer userId) {
        CartVO cartVO = this.getCartVOLimit(userId);
        return ServerResponse.creatBySuccess(cartVO);
    }

    @Override
    public ServerResponse<CartVO> selectOrUnSelect(Integer userId, Integer productId, Integer checked) {
        int rowCount = cartMapper.checkedOrUncheckedProduct(userId, productId, checked);
        if (rowCount > 0) {
            return this.list(userId);
        }
        return ServerResponse.createByErrorMessage("更新全（反）失败");
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if (userId == null)
            return ServerResponse.creatBySuccess(0);
        return ServerResponse.creatBySuccess(cartMapper.selectCartProductCount(userId));
    }
}
