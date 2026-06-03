package com.example.bookshare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookshare.entity.Category;
import com.example.bookshare.mapper.CategoryMapper;
import com.example.bookshare.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类服务实现类
 *
 * @author 课程设计生成器
 * @date 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    
    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    @Override
    public List<Category> getAllCategories() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        return list(queryWrapper);
    }
    
    /**
     * 根据父分类ID获取子分类
     *
     * @param parentId 父分类ID
     * @return 分类列表
     */
    @Override
    public List<Category> getCategoriesByParentId(Long parentId) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        queryWrapper.orderByAsc("sort");
        return list(queryWrapper);
    }
}
