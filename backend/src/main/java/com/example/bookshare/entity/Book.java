package com.example.bookshare.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 书籍实体类
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 书籍实体，对应数据库t_book表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_book")
public class Book implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 书籍ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 书名
     */
    private String title;
    
    /**
     * 作者
     */
    private String author;
    
    /**
     * 出版社
     */
    private String publisher;
    
    /**
     * 出版日期
     */
    private LocalDate publishDate;
    
    /**
     * ISBN国际标准书号
     */
    private String isbn;
    
    /**
     * 售价
     */
    private BigDecimal price;
    
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    
    /**
     * 成色（1-5）
     */
    @TableField("`condition`")
    private Integer condition;
    
    /**
     * 所属分类ID
     */
    private Long categoryId;
    
    /**
     * 发布者用户ID
     */
    private Long userId;
    
    /**
     * 书籍详细描述
     */
    private String description;
    
    /**
     * 书籍图片URL，多个用逗号分隔
     */
    private String images;
    
    /**
     * 浏览次数
     */
    private Integer viewCount;
    
    /**
     * 收藏次数
     */
    private Integer favoriteCount;
    
    /**
     * 联系人QQ
     */
    private String contactQq;
    
    /**
     * 联系人微信
     */
    private String contactWechat;
    
    /**
     * 状态：0待审核，1已上架，2已下架，3审核拒绝
     */
    private Integer status;
    
    /**
     * 审核拒绝原因
     */
    private String rejectReason;
    
    /**
     * 发布时间
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
     * 书籍状态常量
     */
    public static final class Status {
        /** 待审核 */
        public static final Integer PENDING = 0;
        /** 已上架 */
        public static final Integer APPROVED = 1;
        /** 已下架 */
        public static final Integer OFFLINE = 2;
        /** 审核拒绝 */
        public static final Integer REJECTED = 3;
    }
}
