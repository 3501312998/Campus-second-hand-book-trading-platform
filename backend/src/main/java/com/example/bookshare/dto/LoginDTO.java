package com.example.bookshare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录DTO
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 用户登录时传递的数据对象
 */
@Data
@Schema(description = "用户登录DTO")
public class LoginDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    
    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String captcha;
    
    /**
     * 验证码Key
     */
    @Schema(description = "验证码Key")
    private String captchaKey;
}
