package com.example.bookshare.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应结果状态码枚举类
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 定义系统中所有接口的响应状态码和默认消息
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    
    // ========== 成功相关状态码 ==========
    /** 操作成功 */
    SUCCESS(200, "操作成功"),
    
    // ========== 客户端错误相关状态码 ==========
    /** 请求参数错误 */
    BAD_REQUEST(400, "请求参数错误"),
    /** 未授权 - 需要登录 */
    UNAUTHORIZED(401, "未授权，请先登录"),
    /** 禁止访问 - 无权限 */
    FORBIDDEN(403, "禁止访问，您没有权限"),
    /** 资源不存在 */
    NOT_FOUND(404, "请求的资源不存在"),
    /** 请求方法不支持 */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    
    // ========== 服务器错误相关状态码 ==========
    /** 服务器内部错误 */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误，请稍后再试"),
    
    // ========== 业务相关状态码 ==========
    /** 用户相关错误 */
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USERNAME_OR_PASSWORD_ERROR(1003, "用户名或密码错误"),
    USERNAME_ALREADY_EXISTS(1004, "用户名已被注册"),
    STUDENT_ID_ALREADY_EXISTS(1005, "学号已被注册"),
    PHONE_ALREADY_EXISTS(1006, "手机号已被注册"),
    ACCOUNT_DISABLED(1007, "账号已被禁用"),
    CAPTCHA_ERROR(1008, "验证码错误"),
    CAPTCHA_EXPIRED(1009, "验证码已过期"),
    PASSWORD_ERROR(1010, "原密码错误"),
    
    /** 书籍相关错误 */
    BOOK_NOT_FOUND(2001, "书籍不存在"),
    BOOK_PUBLISH_FAILED(2002, "书籍发布失败"),
    BOOK_EDIT_FAILED(2003, "书籍编辑失败"),
    BOOK_DELETE_FAILED(2004, "书籍删除失败"),
    BOOK_AUDIT_FAILED(2005, "书籍审核失败"),
    BOOK_STATUS_ERROR(2006, "书籍状态异常"),
    
    /** 订单相关错误 */
    ORDER_NOT_FOUND(3001, "订单不存在"),
    ORDER_CREATE_FAILED(3002, "订单创建失败"),
    ORDER_CANCEL_FAILED(3003, "订单取消失败"),
    ORDER_PAYMENT_FAILED(3004, "订单支付失败"),
    ORDER_SHIP_FAILED(3005, "订单发货失败"),
    ORDER_RECEIVE_FAILED(3006, "订单收货失败"),
    ORDER_STATUS_ERROR(3007, "订单状态异常，无法执行此操作"),
    
    /** 购物车相关错误 */
    CART_EMPTY(4001, "购物车为空"),
    CART_ITEM_NOT_FOUND(4002, "购物车项不存在"),
    CART_ADD_FAILED(4003, "加入购物车失败"),
    CART_ITEM_EXISTS(4004, "该书籍已在购物车中"),
    
    /** 分类相关错误 */
    CATEGORY_NOT_FOUND(5001, "分类不存在"),
    CATEGORY_HAS_BOOKS(5002, "该分类下存在书籍，无法删除"),
    
    /** 文件上传相关错误 */
    FILE_UPLOAD_FAILED(6001, "文件上传失败"),
    FILE_TYPE_NOT_ALLOWED(6002, "不支持的文件类型"),
    FILE_SIZE_EXCEED(6003, "文件大小超过限制"),
    
    /** 权限相关错误 */
    PERMISSION_DENIED(7001, "权限不足"),
    TOKEN_INVALID(7002, "Token无效或已过期"),
    TOKEN_EXPIRED(7003, "Token已过期"),
    
    // ========== 其他状态码 ==========
    /** 操作过于频繁 */
    TOO_MANY_REQUESTS(8001, "操作过于频繁，请稍后再试"),
    /** 系统维护中 */
    SYSTEM_MAINTENANCE(8002, "系统维护中，请稍后再试"),
    /** 数据校验失败 */
    VALIDATION_FAILED(8003, "数据校验失败");
    
    /**
     * 状态码
     */
    private final Integer code;
    
    /**
     * 默认消息
     */
    private final String message;
}
