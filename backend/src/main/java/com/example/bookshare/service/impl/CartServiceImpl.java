package com.example.bookshare.service.impl;

import com.example.bookshare.dto.CartItemDTO;
import com.example.bookshare.entity.Cart;
import com.example.bookshare.mapper.CartMapper;
import com.example.bookshare.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    
    private final CartMapper cartMapper;
    
    @Override
    public List<CartItemDTO> getCartList(Long userId) {
        List<Cart> carts = cartMapper.selectCartWithBookInfo(userId);
        return carts.stream()
                .map(cart -> CartItemDTO.builder()
                        .id(cart.getId())
                        .bookId(cart.getBookId())
                        .book(CartItemDTO.BookDTO.builder()
                                .title(cart.getTitle())
                                .author(cart.getAuthor())
                                .price(cart.getPrice())
                                .images(cart.getImages())
                                .build())
                        .sellerUsername(cart.getSellerUsername())
                        .build())
                .collect(Collectors.toList());
    }
    
    @Override
    public void addToCart(Long userId, Long bookId) {
        Cart cart = Cart.builder()
                .userId(userId)
                .bookId(bookId)
                .build();
        cartMapper.insert(cart);
    }
    
    @Override
    public void removeFromCart(Long cartId) {
        cartMapper.deleteById(cartId);
    }
}