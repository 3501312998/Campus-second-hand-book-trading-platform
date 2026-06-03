package com.example.bookshare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookshare.entity.Book;
import com.example.bookshare.entity.Order;
import com.example.bookshare.entity.User;
import com.example.bookshare.mapper.BookMapper;
import com.example.bookshare.mapper.OrderMapper;
import com.example.bookshare.mapper.UserMapper;
import com.example.bookshare.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderMapper orderMapper;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    
    @Override
    @Transactional
    public Order createOrder(Long userId, Long[] bookIds) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (Long bookId : bookIds) {
            Book book = bookMapper.selectById(bookId);
            if (book != null) {
                totalAmount = totalAmount.add(book.getPrice());
            }
        }
        
        User user = userMapper.selectById(userId);
        
        Order order = Order.builder()
                .orderNo(generateOrderNo())
                .userId(userId)
                .totalAmount(totalAmount)
                .actualAmount(totalAmount)
                .status(Order.Status.PENDING_PAYMENT)
                .receiverName(user.getRealName())
                .receiverPhone(user.getPhone())
                .receiverAddress(user.getAddress())
                .build();
        
        orderMapper.insert(order);
        return order;
    }
    
    @Override
    public IPage<Order> getOrderList(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        if (status != null) {
            queryWrapper.eq(Order::getStatus, status);
        }
        queryWrapper.orderByDesc(Order::getCreateTime);
        return orderMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public Order getOrderById(Long orderId) {
        return orderMapper.selectById(orderId);
    }
    
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null && order.getStatus() == Order.Status.PENDING_PAYMENT) {
            order.setStatus(Order.Status.CANCELLED);
            order.setCancelTime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }
    
    @Override
    public void payOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null && order.getStatus() == Order.Status.PENDING_PAYMENT) {
            order.setStatus(Order.Status.PENDING_SHIPMENT);
            order.setPayTime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }
    
    @Override
    public void confirmOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null && order.getStatus() == Order.Status.PENDING_RECEIPT) {
            order.setStatus(Order.Status.COMPLETED);
            order.setReceiveTime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }
    
    private String generateOrderNo() {
        return "BS" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}