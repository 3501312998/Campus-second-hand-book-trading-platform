package com.example.bookshare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 书籍查询DTO
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 书籍列表查询条件
 */
@Data
@Schema(description = "书籍查询DTO")
public class BookQueryDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    private Integer pageNum;
    
    /**
     * 每页数量
     */
    @Schema(description = "每页数量")
    private Integer pageSize;
    
    /**
     * 搜索关键字（书名、作者）
     */
    @Schema(description = "搜索关键字")
    private String keyword;
    
    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    private Long categoryId;
    
    /**
     * 成色
     */
    @Schema(description = "成色")
    private Integer condition;
    
    /**
     * 最低价格
     */
    @Schema(description = "最低价格")
    private BigDecimal minPrice;
    
    /**
     * 最高价格
     */
    @Schema(description = "最高价格")
    private BigDecimal maxPrice;
    
    /**
     * 排序字段：create_time, price, view_count
     */
    @Schema(description = "排序字段")
    private String sortBy;
    
    /**
     * 排序方式：asc, desc
     */
    @Schema(description = "排序方式")
    private String sortOrder;
}
