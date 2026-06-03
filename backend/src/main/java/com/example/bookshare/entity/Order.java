package com.example.bookshare.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 订单实体，对应数据库t_order表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_order")
public class Order implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单号，唯一
     */
    private String orderNo;
    
    /**
     * 买家用户ID
     */
    private Long userId;
    
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 实际支付金额
     */
    private BigDecimal actualAmount;
    
    /**
     * 订单状态：0待支付，1待发货，2待收货，3已完成，4已取消，5已退款
     */
    private Integer status;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人电话
     */
    private String receiverPhone;
    
    /**
     * 收货地址
     */
    private String receiverAddress;
    
    /**
     * 订单备注
     */
    private String remark;
    
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    
    /**
     * 发货时间
     */
    private LocalDateTime shipTime;
    
    /**
     * 收货时间
     */
    private LocalDateTime receiveTime;
    
    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;
    
    /**
     * 取消原因
     */
    private String cancelReason;
    
    /**
     * 下单时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除标记：0未删除，1已删除
     */
    @TableLogic
    private Integer deleted;
    
    /**
     * 订单状态常量
     */
    public static final class Status {
        /** 待支付 */
        public static final Integer PENDING_PAYMENT = 0;
        /** 待发货 */
        public static final Integer PENDING_SHIPMENT = 1;
        /** 待收货 */
        public static final Integer PENDING_RECEIPT = 2;
        /** 已完成 */
        public static final Integer COMPLETED = 3;
        /** 已取消 */
        public static final Integer CANCELLED = 4;
        /** 已退款 */
        public static final Integer REFUNDED = 5;
    }
}
