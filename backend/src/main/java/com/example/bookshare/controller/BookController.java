package com.example.bookshare.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookshare.common.Result;
import com.example.bookshare.common.ResultCode;
import com.example.bookshare.dto.BookDTO;
import com.example.bookshare.dto.BookQueryDTO;
import com.example.bookshare.entity.Book;
import com.example.bookshare.entity.Category;
import com.example.bookshare.security.SecurityUser;
import com.example.bookshare.service.BookService;
import com.example.bookshare.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 书籍控制器
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 * @description 处理书籍发布、浏览、搜索、审核等接口
 */
@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Tag(name = "书籍管理", description = "书籍发布、浏览、搜索、审核相关接口")
public class BookController {
    
    /**
     * 书籍服务
     */
    private final BookService bookService;
    
    /**
     * 分类服务
     */
    private final CategoryService categoryService;
    
    /**
     * 分页查询书籍列表（公开接口）
     *
     * @param queryDTO 查询条件
     * @return 书籍分页列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询书籍", description = "分页查询已上架的书籍列表，支持筛选和搜索")
    public Result<Page<Book>> listBooks(BookQueryDTO queryDTO) {
        // 设置默认分页参数
        if (queryDTO.getPageNum() == null) {
            queryDTO.setPageNum(1);
        }
        if (queryDTO.getPageSize() == null) {
            queryDTO.setPageSize(10);
        }
        
        // 查询书籍列表
        Page<Book> page = bookService.queryBooks(queryDTO);
        
        return Result.success(page);
    }
    
    /**
     * 获取书籍详情（公开接口）
     *
     * @param id 书籍ID
     * @return 书籍详情
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "获取书籍详情", description = "根据ID获取书籍详细信息，同时增加浏览次数")
    public Result<Book> getBookDetail(@Parameter(description = "书籍ID") @PathVariable Long id) {
        // 查询书籍详情
        Book book = bookService.getBookDetail(id);
        
        if (book == null) {
            return Result.error(ResultCode.BOOK_NOT_FOUND);
        }
        
        return Result.success(book);
    }
    
    /**
     * 发布书籍
     *
     * @param bookDTO 书籍信息
     * @return 发布结果
     */
    @PostMapping("/publish")
    @Operation(summary = "发布书籍", description = "用户发布二手书信息，需要登录")
    public Result<?> publishBook(@Valid @RequestBody BookDTO bookDTO) {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        
        // 发布书籍
        boolean success = bookService.publishBook(bookDTO, userId);
        
        if (success) {
            return Result.success("发布成功，等待审核！");
        } else {
            return Result.error(ResultCode.BOOK_PUBLISH_FAILED);
        }
    }
    
    /**
     * 编辑书籍
     *
     * @param id 书籍ID
     * @param bookDTO 书籍信息
     * @return 编辑结果
     */
    @PutMapping("/edit/{id}")
    @Operation(summary = "编辑书籍", description = "编辑已发布的书籍信息")
    public Result<?> editBook(
            @Parameter(description = "书籍ID") @PathVariable Long id,
            @Valid @RequestBody BookDTO bookDTO) {
        
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        
        // 编辑书籍
        boolean success = bookService.editBook(id, bookDTO, userId);
        
        if (success) {
            return Result.success("编辑成功！");
        } else {
            return Result.error(ResultCode.BOOK_EDIT_FAILED);
        }
    }
    
    /**
     * 删除书籍
     *
     * @param id 书籍ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除书籍", description = "删除已发布的书籍")
    public Result<?> deleteBook(@Parameter(description = "书籍ID") @PathVariable Long id) {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        
        // 删除书籍
        boolean success = bookService.deleteBook(id, userId);
        
        if (success) {
            return Result.success("删除成功！");
        } else {
            return Result.error(ResultCode.BOOK_DELETE_FAILED);
        }
    }
    
    /**
     * 获取我发布的书籍列表
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param status 状态筛选
     * @return 我发布的书籍列表
     */
    @GetMapping("/my-books")
    @Operation(summary = "我的发布", description = "获取当前用户发布的书籍列表")
    public Result<Page<Book>> getMyBooks(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "状态筛选") @RequestParam(required = false) Integer status) {
        
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        
        // 查询我发布的书籍
        Page<Book> page = bookService.getMyBooks(userId, pageNum, pageSize, status);
        
        return Result.success(page);
    }
    
    /**
     * 审核书籍（管理员接口）
     *
     * @param id 书籍ID
     * @param status 审核状态：1通过，2拒绝
     * @param rejectReason 拒绝原因
     * @return 审核结果
     */
    @PutMapping("/audit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "审核书籍", description = "管理员审核用户发布的书籍")
    public Result<?> auditBook(
            @Parameter(description = "书籍ID") @PathVariable Long id,
            @Parameter(description = "审核状态") @RequestParam Integer status,
            @Parameter(description = "拒绝原因") @RequestParam(required = false) String rejectReason) {
        
        boolean success = bookService.auditBook(id, status, rejectReason);
        
        if (success) {
            return Result.success(status == 1 ? "审核通过！" : "审核拒绝！");
        } else {
            return Result.error(ResultCode.BOOK_AUDIT_FAILED);
        }
    }
    
    /**
     * 获取待审核书籍列表（管理员接口）
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 待审核书籍列表
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "待审核列表", description = "获取所有待审核的书籍列表")
    public Result<Page<Book>> getPendingBooks(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<Book> page = bookService.getPendingBooks(pageNum, pageSize);
        
        return Result.success(page);
    }
    
    /**
     * 获取热门书籍
     *
     * @param limit 数量限制
     * @return 热门书籍列表
     */
    @GetMapping("/hot")
    @Operation(summary = "热门书籍", description = "获取浏览量最高的书籍列表")
    public Result<List<Book>> getHotBooks(
            @Parameter(description = "数量限制") @RequestParam(defaultValue = "6") Integer limit) {
        
        List<Book> books = bookService.getHotBooks(limit);
        
        return Result.success(books);
    }
    
    /**
     * 获取最新书籍
     *
     * @param limit 数量限制
     * @return 最新书籍列表
     */
    @GetMapping("/latest")
    @Operation(summary = "最新书籍", description = "获取最新发布的书籍列表")
    public Result<List<Book>> getLatestBooks(
            @Parameter(description = "数量限制") @RequestParam(defaultValue = "6") Integer limit) {
        
        List<Book> books = bookService.getLatestBooks(limit);
        
        return Result.success(books);
    }
    
    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    private Long getCurrentUserId() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof SecurityUser) {
                return ((SecurityUser) principal).getUserId();
            }
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
        }
        return null;
    }
}
