package com.example.bookshare.dto;

import lombok.Data;

/**
 * 更新用户信息DTO
 *
 * @author 课程设计生成器
 * @date 2024-01-01
 */
@Data
public class UpdateUserDTO {
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 头像
     */
    private String avatar;
    
    /**
     * 性别：0女，1男
     */
    private Integer gender;
    
    /**
     * 专业
     */
    private String major;
    
    /**
     * 年级
     */
    private String grade;
    
    /**
     * 地址
     */
    private String address;
}
