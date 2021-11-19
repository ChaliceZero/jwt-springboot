package com.zq.jwt.common.service;

import com.zq.jwt.consts.CommonConst;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author laozhou
 */
@Component
public class UserLoginService {

    @Resource
    private TokenService tokenService;

    /**
     * 用户登录
     *
     * @param username 用户名 必须唯一（注册时，一定要校验用户名重复，否则会造成很多应用问题）
     * @param password 密钥
     * @param userKey  用户标识，可用于关联其他的用户信息
     * @return
     */
    public String login(String username, String password, String userKey) {
        // 1、验证码校验……
        // 2、使用认证框架进行用户认证……
        // 3、生成Token
        String token = tokenService.createToken(userKey);


        return token;
    }

    /**
     * 获取用户登录信息
     *
     * @param token
     * @return
     */
    public String getLoginInfo(String token) {
        if (null == token || token.trim().length() <= 0) {
            throw new RuntimeException("用户验证失败");
        }
        Claims claims = tokenService.verifyToken(token);
        String userKey = claims.get(CommonConst.USER_KEY, String.class);
        // 根据用户标识获取用户信息 TODO

        return userKey;
    }

    /**
     * 获取用户登录信息
     *
     * @return
     */
    public String getLoginInfo() {
        String token = tokenService.getToken();
        return getLoginInfo(token);
    }
}