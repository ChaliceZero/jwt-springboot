package com.zq.jwt.common.service;

import cn.hutool.core.util.StrUtil;
import com.zq.jwt.consts.CommonConst;
import com.zq.jwt.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laozhou
 */
@Component
public class TokenService {

    /**
     * 请求头：Authorization
     */
    @Value("${token.header}")
    private String header;

    /**
     * JWT签名密钥
     */
    @Value("${token.secret}")
    private String secret;

    /**
     * token过期时间，单位：分钟
     */
    @Value("${token.expireTime}")
    private int expireTime;

    /**
     * 创建用户认证后的token
     *
     * @param userKey 用户标识，可作为用户关联其他数据的key
     * @return token
     */
    public String createToken(String userKey) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expireTime);
        Map<String, Object> payloadMap = new HashMap<>(1);
        payloadMap.put(CommonConst.USER_KEY, userKey);
        JwtUtil.init(secret);
        String token = JwtUtil.genToken(payloadMap, cal.getTime(), null, null);
        return token;
    }

    /**
     * 获取请求体中的token
     *
     * @param request request
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StrUtil.isNotEmpty(token) && token.startsWith(CommonConst.TOKEN_PREFIX)) {
            token = token.replace(CommonConst.TOKEN_PREFIX, CommonConst.EMPTY);
        }
        return token;
    }

    /**
     * 获取请求体中的token
     *
     * @return token
     */
    public String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader(header);
        if (StrUtil.isNotEmpty(token) && token.startsWith(CommonConst.TOKEN_PREFIX)) {
            token = token.replace(CommonConst.TOKEN_PREFIX, CommonConst.EMPTY);
        }
        return token;
    }

    /**
     * 验证token
     *
     * @param token 用户token，不携带前缀
     *              {@link CommonConst#TOKEN_PREFIX}
     * @return
     */
    public Claims verifyToken(String token) {
        return JwtUtil.verify(token);
    }
}