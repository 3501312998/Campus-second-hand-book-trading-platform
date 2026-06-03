package com.example.bookshare.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 拦截所有请求，验证JWT Token并设置认证信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    /**
     * JWT Token提供者
     */
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * 用户详情服务
     */
    private final UserDetailsService userDetailsService;
    
    /**
     * 请求头名称
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";
    
    /**
     * Token前缀
     */
    private static final String BEARER_PREFIX = "Bearer ";
    
    /**
     * 过滤器执行方法
     *
     * @param request HTTP请求
     * @param response HTTP响应
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 从请求头中获取JWT Token
            String jwt = getJwtFromRequest(request);
            
            // 如果Token存在且有效
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // 从Token中获取用户名
                String username = jwtTokenProvider.getUsernameFromToken(jwt);
                
                // 加载用户详情
                var userDetails = userDetailsService.loadUserByUsername(username);
                
                // 创建认证令牌
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                userDetails.getAuthorities()
                        );
                
                // 设置认证详情
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 将认证信息放入安全上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("用户 {} 认证成功", username);
            }
        } catch (Exception e) {
            log.error("无法设置用户认证信息：{}", e.getMessage());
        }
        
        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }
    
    /**
     * 从请求头中获取JWT Token
     *
     * @param request HTTP请求
     * @return JWT Token
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        // 先从请求头获取
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        
        // 如果请求头没有，尝试从请求参数获取
        String jwt = request.getParameter("token");
        if (StringUtils.hasText(jwt)) {
            return jwt;
        }
        
        return null;
    }
}
