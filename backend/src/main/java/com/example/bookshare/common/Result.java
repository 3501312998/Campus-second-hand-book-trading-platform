package com.example.bookshare.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应结果封装类
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 用于封装所有接口的返回值，统一响应格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应状态码
     * 200：成功
     * 400：参数错误
     * 401：未认证
     * 403：禁止访问
     * 404：资源不存在
     * 500：服务器错误
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 创建成功结果（带数据）
     *
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, System.currentTimeMillis());
    }
    
    /**
     * 创建成功结果（无数据）
     *
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }
    
    /**
     * 创建成功结果（自定义消息）
     *
     * @param message 成功消息
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, null, System.currentTimeMillis());
    }
    
    /**
     * 创建成功结果（自定义消息和数据）
     *
     * @param message 成功消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data, System.currentTimeMillis());
    }
    
    /**
     * 创建失败结果
     *
     * @param resultCode 结果码枚举
     * @param <T> 数据类型
     * @return 失败响应结果
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null, System.currentTimeMillis());
    }
    
    /**
     * 创建失败结果（自定义消息）
     *
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败响应结果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ResultCode.INTERNAL_SERVER_ERROR.getCode(), message, null, System.currentTimeMillis());
    }
    
    /**
     * 创建失败结果（自定义状态码和消息）
     *
     * @param code 状态码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败响应结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null, System.currentTimeMillis());
    }
    
    /**
     * 创建参数错误结果
     *
     * @param message 参数错误消息
     * @param <T> 数据类型
     * @return 参数错误响应结果
     */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(ResultCode.BAD_REQUEST.getCode(), message, null, System.currentTimeMillis());
    }
    
    /**
     * 创建未认证结果
     *
     * @param message 未认证消息
     * @param <T> 数据类型
     * @return 未认证响应结果
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(ResultCode.UNAUTHORIZED.getCode(), message, null, System.currentTimeMillis());
    }
    
    /**
     * 创建禁止访问结果
     *
     * @param message 禁止访问消息
     * @param <T> 数据类型
     * @return 禁止访问响应结果
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(ResultCode.FORBIDDEN.getCode(), message, null, System.currentTimeMillis());
    }
    
    /**
     * 创建资源不存在结果
     *
     * @param message 资源不存在消息
     * @param <T> 数据类型
     * @return 资源不存在响应结果
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(ResultCode.NOT_FOUND.getCode(), message, null, System.currentTimeMillis());
    }
}
