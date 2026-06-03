package com.example.bookshare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookshare.dto.BookDTO;
import com.example.bookshare.dto.BookQueryDTO;
import com.example.bookshare.entity.Book;

import java.util.List;

/**
 * 书籍服务接口
 *
 * @author 课程设计生成器
 * @date 2024-01-01
 */
public interface BookService extends IService<Book> {
    
    /**
     * 发布书籍
     *
     * @param bookDTO 书籍信息
     * @param userId 用户ID
     * @return 是否发布成功
     */
    boolean publishBook(BookDTO bookDTO, Long userId);
    
    /**
     * 编辑书籍
     *
     * @param bookId 书籍ID
     * @param bookDTO 书籍信息
     * @param userId 用户ID
     * @return 是否编辑成功
     */
    boolean editBook(Long bookId, BookDTO bookDTO, Long userId);
    
    /**
     * 删除书籍
     *
     * @param bookId 书籍ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteBook(Long bookId, Long userId);
    
    /**
     * 查询书籍列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<Book> queryBooks(BookQueryDTO queryDTO);
    
    /**
     * 查询书籍详情
     *
     * @param bookId 书籍ID
     * @return 书籍信息
     */
    Book getBookDetail(Long bookId);
    
    /**
     * 审核书籍（管理员）
     *
     * @param bookId 书籍ID
     * @param status 状态
     * @param rejectReason 拒绝原因
     * @return 是否审核成功
     */
    boolean auditBook(Long bookId, Integer status, String rejectReason);
    
    /**
     * 获取我发布的书籍
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param status 状态
     * @return 分页结果
     */
    Page<Book> getMyBooks(Long userId, Integer pageNum, Integer pageSize, Integer status);
    
    /**
     * 获取待审核书籍
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<Book> getPendingBooks(Integer pageNum, Integer pageSize);
    
    /**
     * 获取热门书籍
     *
     * @param limit 数量限制
     * @return 书籍列表
     */
    List<Book> getHotBooks(Integer limit);
    
    /**
     * 获取最新书籍
     *
     * @param limit 数量限制
     * @return 书籍列表
     */
    List<Book> getLatestBooks(Integer limit);
}
