package com.zq.jwt.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zq.jwt.common.service.TokenService;
import com.zq.jwt.consts.HttpState;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT拦截器
 *
 * @author laozhou
 * @description 执行顺序：
 * 1、多个拦截器时，按照添加拦截器时定义的顺序执行所有拦截器的preHandle，直至遇到false或全部执行成功；
 * 2、再执行目标处理器（Controller）的方法，若中间抛出异常，则不再执行postHandle，而是跳过再倒序执行afterCompletion；
 * 3、若执行目标处理器成功（页面还未渲染数据时），按倒序执行postHandle，后置位遇到false则继续执行前置位的postHandle；
 * 4、postHandle全部执行完毕，则继续倒序执行afterCompletion。
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private TokenService tokenService;

    /**
     * 处理器预处理回调方法
     *
     * @param handler 响应的处理器，目标Controller
     * @return true表示继续流程（如调用下一个拦截器或处理器）；
     * false表示流程中断，不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String token = tokenService.getToken(request);
            tokenService.verifyToken(token);
        } catch (Exception e) {
            Map back = new HashMap<>();
            back.put("state", HttpState.ERROR);
            back.put("msg", "验证失败");
            // 将返回结构转JSON
            //String jsonBack = JSONUtil.toJsonStr(back);
            // jackson，spring-boot-starter-web包下已引入jackson
            String jsonBack = new ObjectMapper().writeValueAsString(back);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonBack);
            return false;
        }
        return true;
    }

    /**
     * 处理器后处理回调方法（视图渲染之前）
     * 此时可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null。
     *
     * @param handler      响应的处理器，目标Controller
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 处理器处理请求完毕回调方法（视图渲染完毕时）
     * 如：性能监控中可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理，类似于try-catch-finally中的finally，但仅在处理器执行链中调用
     *
     * @param handler 响应的处理器，目标Controller
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}