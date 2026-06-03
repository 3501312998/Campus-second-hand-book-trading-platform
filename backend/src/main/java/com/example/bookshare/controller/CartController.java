package com.example.bookshare.controller;

import com.example.bookshare.dto.CartItemDTO;
import com.example.bookshare.service.CartService;
import com.example.bookshare.common.Result;
import com.example.bookshare.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    
    private Long getCurrentUserId() {
        UsernamePasswordAuthenticationToken authentication = 
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser)) {
            throw new RuntimeException("请先登录");
        }
        
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return securityUser.getUserId();
    }
    
    @GetMapping("/list")
    public Result<List<CartItemDTO>> list() {
        Long userId = getCurrentUserId();
        List<CartItemDTO> carts = cartService.getCartList(userId);
        return Result.success(carts);
    }
    
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Map<String, Long> request) {
        Long bookId = request.get("bookId");
        Long userId = getCurrentUserId();
        cartService.addToCart(userId, bookId);
        return Result.success();
    }
    
    @DeleteMapping("/{cartId}")
    public Result<Void> remove(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return Result.success();
    }
}