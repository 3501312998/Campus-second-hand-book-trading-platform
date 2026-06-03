package com.example.bookshare.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_cart")
public class Cart implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long bookId;
    
    @TableField(exist = false)
    private String title;
    
    @TableField(exist = false)
    private String author;
    
    @TableField(exist = false)
    private java.math.BigDecimal price;
    
    @TableField(exist = false)
    private String images;
    
    @TableField(exist = false)
    private String sellerUsername;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}