package com.example.bookshare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    
    private Long id;
    
    private Long bookId;
    
    private BookDTO book;
    
    private String sellerUsername;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDTO {
        private String title;
        private String author;
        private BigDecimal price;
        private String images;
    }
}