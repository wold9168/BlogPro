package com.hgd.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {
    private static final String secret = "hgd";

    public static String getToken(String userName) {
        return getToken(userName, 30 * 60 * 1000);//半小时后过期
    }

    public static String getToken(String subject, long expMillis) {
        return Jwts.builder().
                setSubject(subject).//JWT 主题，保存的信息，不要包含私密信息，比如密码
                        setIssuer(secret). //JWT 签发方。
                        setIssuedAt(new Date()).//JWT 签发时间。
                        signWith(SignatureAlgorithm.HS256, getSecretKey()).//设置算法及密钥
                        setExpiration(new Date(System.currentTimeMillis() + expMillis)).// 设置过期时间
                        compact();
    }

    /**
     * 从Token获取信息
     */
    public static String getSubjectFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 判断token是否已经失效
     */
    public static boolean isTokenExpired(String token) {
        try {
            Date expiredDate = getClaimsFromToken(token).getExpiration();
            return expiredDate.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 解析token获取Claims
     * Claims中保存了jwt中的信息
     */
    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser().
                setSigningKey(getSecretKey()).
                parseClaimsJws(token).
                getBody();
    }

    /**
     * 生成加密后的密钥
     * SecretKey：Java中密钥的抽象接口，其算法类型为对称加密算法
     * 对称加密算法的主要特点就是加密与解密用的是同一把密钥，对称加密算法主要有：DES,DESede,AES,Blowfish,RC2,RC4等
     */
    private static SecretKey getSecretKey() {
        String encodeToString = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
        byte[] decode = Base64.getDecoder().decode(encodeToString);
        return new SecretKeySpec(decode, 0, decode.length, "AES");
    }
}
