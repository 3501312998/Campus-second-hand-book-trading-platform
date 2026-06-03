package com.example.bookshare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookshare.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 用户表的数据访问层接口，继承BaseMapper提供基本的CRUD操作
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
}
