package com.zq.jwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * JWT封装工具类
 *
 * @author laozhou
 * @date 2021/11/10
 */
public class Auth0JwtUtil {

    /**
     * Token生成与验签的签名算法
     */
    private static Algorithm algorithm;

    /**
     * 初始化
     *
     * @param secret 签名密钥
     */
    public static void init(String secret) {
        if (null == algorithm) {
            algorithm = Algorithm.HMAC256(secret);
        }
    }

    /**
     * 生成Token
     *
     * @param payloadMap 荷载键值对
     * @param expireTime 过期时间
     * @param unit       时间单位
     *                   {@link ChronoUnit#DAYS}
     *                   {@link ChronoUnit#MINUTES}
     * @return
     */
    public static String genToken(Map<String, String> payloadMap, long expireTime, ChronoUnit unit) {
        try {
            // 处理过期时间
            LocalDateTime now = LocalDateTime.now();
            now.plus(expireTime, unit);
            Date expireDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            // 设置荷载
            JWTCreator.Builder builder = JWT.create();
            payloadMap.forEach((k, v) -> {
                builder.withClaim(k, v);
            });
            // 生成令牌
            String token = builder
                    .withExpiresAt(expireDate)
                    .sign(algorithm);
            return token;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Token验签
     *
     * @param token
     * @return
     */
    public static DecodedJWT verify(String token) {
        try {
            // 创建验证对象
            Verification require = JWT.require(algorithm);
            JWTVerifier jwtVerifier = require.build();
            // 验证token，验证成功则不会抛出异常
            DecodedJWT verify = jwtVerifier.verify(token);
            return verify;
        } catch (SignatureGenerationException e) {
            throw new RuntimeException("签名无效");
        } catch (TokenExpiredException e) {
            throw new RuntimeException("Token过期");
        } catch (AlgorithmMismatchException e) {
            throw new RuntimeException("算法不一致");
        } catch (InvalidClaimException e) {
            throw new RuntimeException("Payload无效");
        } catch (Exception e) {
            throw new RuntimeException("Token无效");
        }
    }

    private Auth0JwtUtil() {
    }
}