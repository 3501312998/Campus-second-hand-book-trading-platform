package com.example.bookshare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 书籍DTO
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 书籍发布/编辑时传递的数据对象
 */
@Data
@Schema(description = "书籍DTO")
public class BookDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 书籍ID（编辑时使用）
     */
    @Schema(description = "书籍ID")
    private Long id;
    
    /**
     * 书名
     */
    @Schema(description = "书名")
    @NotBlank(message = "书名不能为空")
    @Size(max = 200, message = "书名长度不能超过200个字符")
    private String title;
    
    /**
     * 作者
     */
    @Schema(description = "作者")
    @Size(max = 100, message = "作者长度不能超过100个字符")
    private String author;
    
    /**
     * 出版社
     */
    @Schema(description = "出版社")
    @Size(max = 100, message = "出版社长度不能超过100个字符")
    private String publisher;
    
    /**
     * 出版日期
     */
    @Schema(description = "出版日期")
    private LocalDate publishDate;
    
    /**
     * ISBN
     */
    @Schema(description = "ISBN")
    @Size(max = 50, message = "ISBN长度不能超过50个字符")
    private String isbn;
    
    /**
     * 售价
     */
    @Schema(description = "售价")
    @NotNull(message = "售价不能为空")
    @DecimalMin(value = "0.01", message = "售价必须大于0")
    @DecimalMax(value = "99999.99", message = "售价不能超过99999.99")
    private BigDecimal price;
    
    /**
     * 原价
     */
    @Schema(description = "原价")
    @DecimalMin(value = "0.01", message = "原价必须大于0")
    @DecimalMax(value = "99999.99", message = "原价不能超过99999.99")
    private BigDecimal originalPrice;
    
    /**
     * 成色
     */
    @Schema(description = "成色")
    private Integer condition;
    
    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    @NotNull(message = "请选择书籍分类")
    private Long categoryId;
    
    /**
     * 书籍描述
     */
    @Schema(description = "书籍描述")
    @Size(max = 2000, message = "描述长度不能超过2000个字符")
    private String description;
    
    /**
     * 书籍图片URL，多个用逗号分隔
     */
    @Schema(description = "书籍图片URL")
    private String images;
    
    /**
     * 联系人QQ
     */
    @Schema(description = "联系人QQ")
    private String contactQq;
    
    /**
     * 联系人微信
     */
    @Schema(description = "联系人微信")
    private String contactWechat;
}
