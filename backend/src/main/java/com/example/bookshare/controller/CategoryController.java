package com.example.bookshare.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookshare.common.Result;
import com.example.bookshare.entity.Category;
import com.example.bookshare.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 提供分类相关的RESTful API接口
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    
    /**
     * 分类服务
     */
    private final CategoryService categoryService;
    
    /**
     * 获取所有分类列表
     * 
     * @return 分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> list() {
        List<Category> categories = categoryService.list();
        return Result.success(categories);
    }
    
    /**
     * 根据ID获取分类详情
     *
     * @param id 分类ID
     * @return 分类信息
     */
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return Result.success(category);
    }
    
    /**
     * 根据父分类ID获取子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    @GetMapping("/{parentId}/children")
    public Result<List<Category>> getChildren(@PathVariable Long parentId) {
        List<Category> children = categoryService.getCategoriesByParentId(parentId);
        return Result.success(children);
    }
}
