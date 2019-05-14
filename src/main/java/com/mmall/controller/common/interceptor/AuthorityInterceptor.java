package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

/**
 * @author: whua
 * @create: 2019/05/14 17:38
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        log.info("preHandle");
        //请求Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //解析handlerMethod
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数，具体参数key以及value是什么，打印日志
        StringBuilder requestParamBuffer = new StringBuilder();
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = StringUtils.EMPTY;

            Object obj = entry.getValue();
            if (obj != null) {
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        // 如果是UserManageController的login请求
        if (StringUtils.equals(className, "UserManageController") && StringUtils.equals(methodName, "login")) {
            log.info("权限拦截器拦截到请求, className:{}, methodName:{}", className, methodName);
            //如果是拦截到登录请求，不打印参数，因为参数里面有密码。防止日志泄露
            return true;
        }

        log.info("权限拦截器拦截到请求, className:{}, methodName:{}, param:{}", className, methodName, requestParamBuffer.toString());

        User user = null;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr, User.class);
        }

        if (user == null || (user.getRole() != Const.Role.ROLE_ADMIN)) {
            //返回false，即不会调用controller里的方法
            httpServletResponse.reset();//这里要添加reset，否则会报异常 getWriter() has already been called for this response.
            httpServletResponse.setCharacterEncoding("UTF-8");//这里要设置编码，否则会乱码
            httpServletResponse.setContentType("application/json;charset=UTF-8");//这里要设置返回值的类型，因为全部是json接口

            PrintWriter writer = httpServletResponse.getWriter();

            //上传由于富文本的控件要求，要特殊处理返回值，这里区分是否登录以及是否有权限
            if (user == null) {
                if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richTextImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "请登录管理员");
                    writer.print(JsonUtil.obj2String(resultMap));
                } else {
                    writer.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
            } else {
                if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richTextImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "无权限操作");
                    writer.print(JsonUtil.obj2String(resultMap));
                } else {
                    writer.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
                }
            }
            writer.flush();
            writer.close(); // 这里要关闭
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
