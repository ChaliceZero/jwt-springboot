package com.zq.jwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author laozhou
 */
public class JwtUtil {

    /**
     * 签名算法
     */
    private static SignatureAlgorithm algorithm;

    /**
     * 签名密钥
     */
    private static String secret;

    /**
     * 初始化
     *
     * @param secret 签名密钥
     */
    public static void init(String secret) {
        JwtUtil.algorithm = SignatureAlgorithm.HS512;
        JwtUtil.secret = null == secret ? "secret-laozhou" : secret;
    }

    /**
     * 生成token
     *
     * @param payloadMap 荷载
     * @param expireDate 过期时间
     * @param issuer     签发者
     * @param subject    面向用户
     * @return
     */
    public static String genToken(Map<String, Object> payloadMap, Date expireDate, String issuer, String subject) {
        Calendar cal = Calendar.getInstance();
        String token = Jwts.builder()
                // 设置荷载数据
                .setClaims(payloadMap)
                // 设置签发时间
                .setIssuedAt(cal.getTime())
                // 设置过期时间
                .setExpiration(expireDate)
                // 设置签发者
                .setIssuer(issuer)
                // 设置面向用户
                .setSubject(subject)
                .signWith(algorithm, secret).compact();
        return token;
    }

    /**
     * token验签
     *
     * @param token
     */
    public static Claims verify(String token) {
        Claims payload = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return payload;
    }

    private JwtUtil() {
    }
}