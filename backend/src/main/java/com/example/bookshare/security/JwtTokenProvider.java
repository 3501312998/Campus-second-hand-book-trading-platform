package com.example.bookshare.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT Token提供者
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 负责JWT Token的生成、验证和解析
 */
@Slf4j
@Component
public class JwtTokenProvider {
    
    /**
     * JWT密钥
     */
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    /**
     * Token有效期（毫秒）
     */
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    /**
     * SecretKey对象
     */
    private SecretKey secretKey;
    
    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        // 将密钥转换为SecretKey对象
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * 生成Token
     *
     * @param username 用户名
     * @param userId 用户ID
     * @return JWT Token字符串
     */
    public String generateToken(String username, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * 从Token中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }
    
    /**
     * 从Token中获取用户ID
     *
     * @param token JWT Token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * 验证Token是否过期
     *
     * @param token JWT Token
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiry = claims.getExpiration();
            return expiry.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * 验证Token是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("JWT Token格式错误：{}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT Token已过期：{}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT Token不支持：{}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT Token为空：{}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT Token验证失败：{}", e.getMessage());
        }
        return false;
    }
    
    /**
     * 解析Token
     *
     * @param token JWT Token
     * @return Claims对象
     */
    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 获取Token剩余有效期
     *
     * @param token JWT Token
     * @return 剩余有效期（秒）
     */
    public Long getExpirationFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            return (expiration.getTime() - System.currentTimeMillis()) / 1000;
        } catch (Exception e) {
            return 0L;
        }
    }
}
