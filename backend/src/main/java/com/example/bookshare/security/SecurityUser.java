package com.example.bookshare.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Security用户详情
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 实现UserDetails接口，用于Spring Security认证
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityUser implements UserDetails {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 权限集合
     */
    private Collection<? extends GrantedAuthority> authorities;
    
    /**
     * 账号是否未过期
     */
    private boolean accountNonExpired;
    
    /**
     * 账号是否未锁定
     */
    private boolean accountNonLocked;
    
    /**
     * 密码是否未过期
     */
    private boolean credentialsNonExpired;
    
    /**
     * 账号是否启用
     */
    private boolean enabled;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 真实姓名
     */
    private String realName;
}
