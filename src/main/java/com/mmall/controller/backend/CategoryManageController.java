package com.mmall.controller.backend;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
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
 * @create: 2019/04/28 17:31
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * @description: 添加品类名
     * @param: [session, categoryName, parentId]
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     */
    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        //全部通过拦截器验证是否登录以及权限
        return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * @description: 修改品类名
     * @param: [session, categoryId, categoryName]
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     */
    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setCategoryName(Integer categoryId, String categoryName) {
        //全部通过拦截器验证是否登录以及权限
        return iCategoryService.setCategoryName(categoryId, categoryName);
    }

    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory
            (@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        }
//        //校验是否为管理员
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            // 是管理员，查询子节点的category信息，并且不递归，保持平级
//            return iCategoryService.getChildrenParallelCategory(categoryId);
//        } else {
//            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//        }

        //全部通过拦截器验证是否登录以及权限
        // 是管理员，查询子节点的category信息，并且不递归，保持平级
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * @description: 获取本节点和其递归孩子节点的id
     * @param: [session, categoryId]
     * @return: com.mmall.common.ServerResponse
     */
    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        //全部通过拦截器验证是否登录以及权限
        // 是管理员，查询当前节点的id和递归子节点的id
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }

}
