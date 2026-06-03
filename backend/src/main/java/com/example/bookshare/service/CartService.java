package com.example.bookshare.service;

import com.example.bookshare.dto.CartItemDTO;
import com.example.bookshare.entity.Cart;

import java.util.List;

public interface CartService {
    
    List<CartItemDTO> getCartList(Long userId);
    
    void addToCart(Long userId, Long bookId);
    
    void removeFromCart(Long cartId);
}