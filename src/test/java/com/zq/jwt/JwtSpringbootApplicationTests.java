package com.zq.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;

@SpringBootTest
class JwtSpringbootApplicationTests {

    /**
     * 上下文方法
     */
    @Test
    void contextLoads() {
        testAuth0();
    }

    public void testAuth0() {
        try {
            // 生成令牌的密钥，不可泄露
            String secret = "secret";
            // 执行签名的加密算法
            com.auth0.jwt.algorithms.Algorithm algorithm = com.auth0.jwt.algorithms.Algorithm.HMAC256(secret);

            // 1、生成token
            // 创建令牌到期时间，如60秒后到期
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, 60);
            // 生成令牌
            HashMap<String, Object> headMap = new HashMap<>();
            String token = com.auth0.jwt.JWT.create()
                    // head，可不显示指定，头信息中会默认指定使用HMAC SHA256算法加密
                    //.withHeader(headMap)
                    // payload（有效载荷）
                    .withClaim("userId", "111")
                    .withClaim("username", "zhangsan")
                    // 设置令牌过期时间
                    .withExpiresAt(cal.getTime())
                    // 指定加密算法进行签名
                    .sign(algorithm);
            System.out.println("token=" + token);

            // 2、解析token
            String[] parts = token.split("\\.");
            Base64.Decoder decoder = Base64.getDecoder();
            // head={"typ":"JWT","alg":"HS256"}
            System.out.println("head=" + new String(decoder.decode(parts[0])));
            // payload={"exp":1636556514,"userId":"111","username":"zhangsan"}
            System.out.println("payload=" + new String(decoder.decode(parts[1])));

            // 3、验证token
            // 创建验证对象
            com.auth0.jwt.interfaces.Verification require = com.auth0.jwt.JWT.require(algorithm);
            com.auth0.jwt.JWTVerifier jwtVerifier = require.build();
            // 验证token，验证成功则不会出现异常
            com.auth0.jwt.interfaces.DecodedJWT verify = jwtVerifier.verify(token);
            System.out.println("过期时间=" + verify.getExpiresAt());
            // 注意：取数据时，as的数据类型必须跟设置的类型一样，否则取出null
            System.out.println("验证成功，获取荷载信息userId=" + verify.getClaim("userId").asString());
            System.out.println("验证成功，获取荷载信息username=" + verify.getClaim("username").asString());
        } catch (com.auth0.jwt.exceptions.SignatureGenerationException e1) {
            System.out.println("1、签名不一致异常");
            e1.printStackTrace();
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e2) {
            System.out.println("2、令牌过期异常");
            e2.printStackTrace();
        } catch (com.auth0.jwt.exceptions.AlgorithmMismatchException e3) {
            System.out.println("3、算法不匹配异常");
            e3.printStackTrace();
        } catch (com.auth0.jwt.exceptions.InvalidClaimException e4) {
            System.out.println("4、payload失效异常");
            e4.printStackTrace();
        }

    }

}