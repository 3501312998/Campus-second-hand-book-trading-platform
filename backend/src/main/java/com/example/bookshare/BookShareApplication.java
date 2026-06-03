package com.example.bookshare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 校园二手书交易平台 - Spring Boot应用主类
 * 
 * @author 课程设计生成器
 * @date 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.example.bookshare.mapper")
public class BookShareApplication {
    
    /**
     * 应用主入口
     * 
     * @param args 命令行参数
     * @description 启动Spring Boot应用，自动配置组件扫描
     */
    public static void main(String[] args) {
        SpringApplication.run(BookShareApplication.class, args);
        System.out.println("===========================================");
        System.out.println("校园二手书交易平台启动成功！");
        System.out.println("API文档地址：http://localhost:8080/api/swagger-ui/index.html");
        System.out.println("===========================================");
    }
}
