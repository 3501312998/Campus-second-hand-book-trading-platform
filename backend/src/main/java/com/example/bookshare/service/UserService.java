package com.example.bookshare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookshare.dto.LoginDTO;
import com.example.bookshare.dto.RegisterDTO;
import com.example.bookshare.dto.UpdateUserDTO;
import com.example.bookshare.entity.User;

/**
 * 用户服务接口
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 定义用户相关的业务方法
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 是否注册成功
     * @description 验证学号唯一性，密码使用BCrypt加密后存储
     */
    boolean register(RegisterDTO registerDTO);
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录信息（用户名、密码）
     * @return 登录成功返回用户信息，失败返回null
     * @description 验证用户凭证，返回用户信息（不包含密码）
     */
    User login(LoginDTO loginDTO);
    
    /**
     * 更新用户信息
     *
     * @param userId 用户ID
     * @param updateUserDTO 更新信息
     * @return 是否更新成功
     */
    boolean updateUserInfo(Long userId, UpdateUserDTO updateUserDTO);
    
    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 修改用户状态（管理员）
     *
     * @param userId 用户ID
     * @param status 状态：0禁用，1启用
     * @return 是否修改成功
     */
    boolean changeUserStatus(Long userId, Integer status);
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);
    
    /**
     * 根据学号查询用户
     *
     * @param studentId 学号
     * @return 用户信息
     */
    User getByStudentId(String studentId);
    
    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User getByPhone(String phone);
}
