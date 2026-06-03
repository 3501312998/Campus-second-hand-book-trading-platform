package com.example.bookshare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.bookshare.entity.Order;
import com.example.bookshare.service.OrderService;
import com.example.bookshare.common.Result;
import com.example.bookshare.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    private Long getCurrentUserId() {
        UsernamePasswordAuthenticationToken authentication = 
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser)) {
            throw new RuntimeException("请先登录");
        }
        
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return securityUser.getUserId();
    }
    
    @PostMapping("/create")
    public Result<Order> create(@RequestBody Map<String, Long[]> request) {
        Long[] bookIds = request.get("bookIds");
        Long userId = getCurrentUserId();
        Order order = orderService.createOrder(userId, bookIds);
        return Result.success(order);
    }
    
    @GetMapping("/list")
    public Result<IPage<Order>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status) {
        Long userId = getCurrentUserId();
        IPage<Order> orders = orderService.getOrderList(userId, status, pageNum, pageSize);
        return Result.success(orders);
    }
    
    @GetMapping("/{orderId}")
    public Result<Order> getById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return Result.success(order);
    }
    
    @PutMapping("/{orderId}/cancel")
    public Result<Void> cancel(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return Result.success();
    }
    
    @PutMapping("/{orderId}/pay")
    public Result<Void> pay(@PathVariable Long orderId) {
        orderService.payOrder(orderId);
        return Result.success();
    }
    
    @PutMapping("/{orderId}/confirm")
    public Result<Void> confirm(@PathVariable Long orderId) {
        orderService.confirmOrder(orderId);
        return Result.success();
    }
}