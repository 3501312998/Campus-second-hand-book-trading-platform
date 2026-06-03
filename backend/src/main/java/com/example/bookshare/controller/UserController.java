package com.example.bookshare.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookshare.common.Result;
import com.example.bookshare.common.ResultCode;
import com.example.bookshare.dto.LoginDTO;
import com.example.bookshare.dto.RegisterDTO;
import com.example.bookshare.dto.UpdateUserDTO;
import com.example.bookshare.entity.User;
import com.example.bookshare.security.JwtTokenProvider;
import com.example.bookshare.security.SecurityUser;
import com.example.bookshare.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 处理用户注册、登录、信息管理等接口
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户注册、登录、信息管理相关接口")
public class UserController {
    
    /**
     * 用户服务
     */
    private final UserService userService;
    
    /**
     * JWT Token提供者
     */
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * 用户注册接口
     *
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public Result<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        log.info("用户注册请求：{}", registerDTO.getUsername());
        
        // 调用服务层注册
        boolean success = userService.register(registerDTO);
        
        if (success) {
            return Result.success("注册成功！");
        } else {
            return Result.error("注册失败，请稍后再试");
        }
    }
    
    /**
     * 用户登录接口
     *
     * @param loginDTO 登录信息
     * @return 登录结果（包含Token）
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户使用用户名密码登录")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("用户登录请求：{}", loginDTO.getUsername());
        
        // 调用服务层登录
        User user = userService.login(loginDTO);
        
        if (user == null) {
            return Result.error(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        
        // 检查账号状态
        if (user.getStatus() == 0) {
            return Result.error(ResultCode.ACCOUNT_DISABLED);
        }
        
        // 生成JWT Token
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getId());
        
        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userInfo", user);
        
        log.info("用户登录成功：{}", user.getUsername());
        return Result.success("登录成功！", data);
    }
    
    /**
     * 获取当前登录用户信息
     *
     * @return 当前用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的信息")
    public Result<User> getUserInfo() {
        // 从安全上下文中获取当前用户
        UsernamePasswordAuthenticationToken authentication = 
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser)) {
            return Result.unauthorized("请先登录");
        }
        
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long userId = securityUser.getUserId();
        
        // 查询用户信息
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_FOUND);
        }
        
        // 隐藏密码
        user.setPassword(null);
        
        return Result.success(user);
    }
    
    /**
     * 更新用户信息
     *
     * @param updateUserDTO 更新信息
     * @return 更新结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新用户信息", description = "更新当前登录用户的信息")
    public Result<?> updateUserInfo(@Valid @RequestBody UpdateUserDTO updateUserDTO) {
        // 从安全上下文中获取当前用户ID
        UsernamePasswordAuthenticationToken authentication = 
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser)) {
            return Result.unauthorized("请先登录");
        }
        
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long userId = securityUser.getUserId();
        
        // 更新用户信息
        boolean success = userService.updateUserInfo(userId, updateUserDTO);
        
        if (success) {
            return Result.success("更新成功！");
        } else {
            return Result.error("更新失败，请稍后再试");
        }
    }
    
    /**
     * 修改密码
     *
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    @PutMapping("/password")
    @Operation(summary = "修改密码", description = "修改当前登录用户的密码")
    public Result<?> changePassword(
            @Parameter(description = "原密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        
        // 从安全上下文中获取当前用户ID
        UsernamePasswordAuthenticationToken authentication = 
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser)) {
            return Result.unauthorized("请先登录");
        }
        
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long userId = securityUser.getUserId();
        
        // 修改密码
        boolean success = userService.changePassword(userId, oldPassword, newPassword);
        
        if (success) {
            return Result.success("密码修改成功！");
        } else {
            return Result.error("原密码错误");
        }
    }
    
    /**
     * 分页查询用户列表（管理员接口）
     *
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @param keyword 搜索关键字
     * @return 用户列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "分页查询用户", description = "管理员分页查询用户列表")
    public Result<Page<User>> listUsers(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "搜索关键字") @RequestParam(required = false) String keyword) {
        
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like("username", keyword)
                       .or().like("real_name", keyword)
                       .or().like("student_id", keyword);
        }
        queryWrapper.orderByDesc("create_time");
        
        // 分页查询
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> result = userService.page(page, queryWrapper);
        
        // 隐藏密码
        result.getRecords().forEach(user -> user.setPassword(null));
        
        return Result.success(result);
    }
    
    /**
     * 禁用/启用用户（管理员接口）
     *
     * @param userId 用户ID
     * @param status 状态：0禁用，1启用
     * @return 操作结果
     */
    @PutMapping("/status/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "修改用户状态", description = "管理员禁用或启用用户账号")
    public Result<?> changeUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "状态") @RequestParam Integer status) {
        
        boolean success = userService.changeUserStatus(userId, status);
        
        if (success) {
            return Result.success(status == 0 ? "禁用成功！" : "启用成功！");
        } else {
            return Result.error("操作失败，用户不存在");
        }
    }
}
