package com.example.bookshare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookshare.common.ResultCode;
import com.example.bookshare.dto.LoginDTO;
import com.example.bookshare.dto.RegisterDTO;
import com.example.bookshare.dto.UpdateUserDTO;
import com.example.bookshare.entity.User;
import com.example.bookshare.exception.BusinessException;
import com.example.bookshare.mapper.UserMapper;
import com.example.bookshare.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现类
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 实现用户相关的业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    /**
     * 密码编码器（使用BCrypt）
     */
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 是否注册成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(RegisterDTO registerDTO) {
        // 验证两次密码是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "两次输入的密码不一致");
        }
        
        // 检查用户名是否已存在
        User existUser = getByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException(ResultCode.USERNAME_ALREADY_EXISTS);
        }
        
        // 检查学号是否已存在
        User existStudent = getByStudentId(registerDTO.getStudentId());
        if (existStudent != null) {
            throw new BusinessException(ResultCode.STUDENT_ID_ALREADY_EXISTS);
        }
        
        // 检查手机号是否已存在
        if (registerDTO.getPhone() != null) {
            User existPhone = getByPhone(registerDTO.getPhone());
            if (existPhone != null) {
                throw new BusinessException(ResultCode.PHONE_ALREADY_EXISTS);
            }
        }
        
        // 创建用户对象
        User user = User.builder()
                .username(registerDTO.getUsername())
                .studentId(registerDTO.getStudentId())
                .realName(registerDTO.getRealName())
                .phone(registerDTO.getPhone())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .status(1)
                .build();
        
        // 保存用户
        boolean result = save(user);
        
        if (result) {
            log.info("用户 {} 注册成功", registerDTO.getUsername());
        }
        
        return result;
    }
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录成功返回用户信息，失败返回null
     */
    @Override
    public User login(LoginDTO loginDTO) {
        // 查询用户
        User user = getByUsername(loginDTO.getUsername());
        
        if (user == null) {
            log.warn("用户 {} 登录失败：用户不存在", loginDTO.getUsername());
            return null;
        }
        
        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.warn("用户 {} 登录失败：密码错误", loginDTO.getUsername());
            return null;
        }
        
        // 隐藏密码
        user.setPassword(null);
        
        log.info("用户 {} 登录成功", loginDTO.getUsername());
        
        return user;
    }
    
    /**
     * 更新用户信息
     *
     * @param userId 用户ID
     * @param updateUserDTO 更新信息
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(Long userId, UpdateUserDTO updateUserDTO) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 检查手机号是否被其他用户使用
        if (updateUserDTO.getPhone() != null && !updateUserDTO.getPhone().equals(user.getPhone())) {
            User existPhone = getByPhone(updateUserDTO.getPhone());
            if (existPhone != null && !existPhone.getId().equals(userId)) {
                throw new BusinessException(ResultCode.PHONE_ALREADY_EXISTS);
            }
        }
        
        // 更新用户信息
        user.setRealName(updateUserDTO.getRealName());
        user.setPhone(updateUserDTO.getPhone());
        user.setEmail(updateUserDTO.getEmail());
        user.setAvatar(updateUserDTO.getAvatar());
        user.setGender(updateUserDTO.getGender());
        user.setMajor(updateUserDTO.getMajor());
        user.setGrade(updateUserDTO.getGrade());
        user.setAddress(updateUserDTO.getAddress());
        
        return updateById(user);
    }
    
    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 验证原密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("用户 {} 修改密码失败：原密码错误", user.getUsername());
            return false;
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        
        return updateById(user);
    }
    
    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeUserStatus(Long userId, Integer status) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        
        user.setStatus(status);
        
        return updateById(user);
    }
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper);
    }
    
    /**
     * 根据学号查询用户
     *
     * @param studentId 学号
     * @return 用户信息
     */
    @Override
    public User getByStudentId(String studentId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId);
        return getOne(queryWrapper);
    }
    
    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Override
    public User getByPhone(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        return getOne(queryWrapper);
    }
}
