package com.example.bookshare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookshare.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 *
 * @author 课程设计生成器
 * @date 2024-01-01
 */
public interface CategoryService extends IService<Category> {
    
    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    List<Category> getAllCategories();
    
    /**
     * 根据父分类ID获取子分类
     *
     * @param parentId 父分类ID
     * @return 分类列表
     */
    List<Category> getCategoriesByParentId(Long parentId);
}
