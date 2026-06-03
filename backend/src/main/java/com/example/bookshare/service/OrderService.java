package com.example.bookshare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.bookshare.entity.Order;

public interface OrderService {
    
    Order createOrder(Long userId, Long[] bookIds);
    
    IPage<Order> getOrderList(Long userId, Integer status, Integer pageNum, Integer pageSize);
    
    Order getOrderById(Long orderId);
    
    void cancelOrder(Long orderId);
    
    void payOrder(Long orderId);
    
    void confirmOrder(Long orderId);
}