package com.example.bookshare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookshare.common.ResultCode;
import com.example.bookshare.dto.BookDTO;
import com.example.bookshare.dto.BookQueryDTO;
import com.example.bookshare.entity.Book;
import com.example.bookshare.exception.BusinessException;
import com.example.bookshare.mapper.BookMapper;
import com.example.bookshare.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 书籍服务实现类
 *
 * @author 课程设计生成器
 * @date 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishBook(BookDTO bookDTO, Long userId) {
        Book book = Book.builder()
                .userId(userId)
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .isbn(bookDTO.getIsbn())
                .publisher(bookDTO.getPublisher())
                .publishDate(bookDTO.getPublishDate())
                .categoryId(bookDTO.getCategoryId())
                .price(bookDTO.getPrice())
                .originalPrice(bookDTO.getOriginalPrice())
                .condition(bookDTO.getCondition())
                .description(bookDTO.getDescription())
                .images(bookDTO.getImages())
                .status(0)
                .viewCount(0)
                .build();
        
        boolean result = save(book);
        
        if (result) {
            log.info("用户 {} 发布书籍：{}", userId, bookDTO.getTitle());
        }
        
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editBook(Long bookId, BookDTO bookDTO, Long userId) {
        Book book = getById(bookId);
        
        if (book == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "书籍不存在");
        }
        
        if (!book.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权修改该书籍");
        }
        
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublishDate(bookDTO.getPublishDate());
        book.setCategoryId(bookDTO.getCategoryId());
        book.setPrice(bookDTO.getPrice());
        book.setOriginalPrice(bookDTO.getOriginalPrice());
        book.setCondition(bookDTO.getCondition());
        book.setDescription(bookDTO.getDescription());
        book.setImages(bookDTO.getImages());
        book.setStatus(0);
        
        return updateById(book);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBook(Long bookId, Long userId) {
        Book book = getById(bookId);
        
        if (book == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "书籍不存在");
        }
        
        if (!book.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权删除该书籍");
        }
        
        return removeById(bookId);
    }
    
    @Override
    public Page<Book> queryBooks(BookQueryDTO queryDTO) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        
        if (queryDTO.getCategoryId() != null) {
            queryWrapper.eq("category_id", queryDTO.getCategoryId());
        }
        
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                    .like("title", queryDTO.getKeyword())
                    .or()
                    .like("author", queryDTO.getKeyword())
                    .or()
                    .like("isbn", queryDTO.getKeyword()));
        }
        
        if (queryDTO.getCondition() != null) {
            queryWrapper.eq("condition", queryDTO.getCondition());
        }
        
        if (queryDTO.getMinPrice() != null) {
            queryWrapper.ge("price", queryDTO.getMinPrice());
        }
        
        if (queryDTO.getMaxPrice() != null) {
            queryWrapper.le("price", queryDTO.getMaxPrice());
        }
        
        queryWrapper.eq("status", 1);
        
        if ("price".equals(queryDTO.getSortBy())) {
            queryWrapper.orderBy(true, "asc".equals(queryDTO.getSortOrder()), "price");
        } else if ("time".equals(queryDTO.getSortBy()) || "create_time".equals(queryDTO.getSortBy())) {
            queryWrapper.orderBy(true, "asc".equals(queryDTO.getSortOrder()), "create_time");
        } else {
            queryWrapper.orderByDesc("create_time");
        }
        
        return page(new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
    }
    
    @Override
    public Book getBookDetail(Long bookId) {
        Book book = getById(bookId);
        
        if (book != null) {
            book.setViewCount(book.getViewCount() + 1);
            updateById(book);
        }
        
        return book;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditBook(Long bookId, Integer status, String rejectReason) {
        Book book = getById(bookId);
        
        if (book == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "书籍不存在");
        }
        
        book.setStatus(status);
        book.setRejectReason(rejectReason);
        
        return updateById(book);
    }
    
    @Override
    public Page<Book> getMyBooks(Long userId, Integer pageNum, Integer pageSize, Integer status) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }
    
    @Override
    public Page<Book> getPendingBooks(Integer pageNum, Integer pageSize) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0);
        queryWrapper.orderByDesc("create_time");
        
        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }
    
    @Override
    public List<Book> getHotBooks(Integer limit) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("LIMIT " + limit);
        
        return list(queryWrapper);
    }
    
    @Override
    public List<Book> getLatestBooks(Integer limit) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT " + limit);
        
        return list(queryWrapper);
    }
}
