package com.example.bookshare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookshare.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    
    @Select("SELECT c.*, b.title, b.author, b.price, b.images, u.username as sellerUsername " +
            "FROM t_cart c " +
            "LEFT JOIN t_book b ON c.book_id = b.id " +
            "LEFT JOIN t_user u ON b.user_id = u.id " +
            "WHERE c.user_id = #{userId} AND c.deleted = 0")
    List<Cart> selectCartWithBookInfo(@Param("userId") Long userId);
}