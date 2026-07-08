package org.rc.controller;

import org.rc.model.Item;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索控制器 - 演示查询参数绑定
 * 
 * 演示内容：
 * 1. @RequestParam - 查询参数
 * 2. required 属性 - 参数是否必须
 * 3. defaultValue 属性 - 默认值
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {
    
    // 模拟数据
    private final List<Item> items = new ArrayList<>();
    
    public SearchController() {
        items.add(new Item(1L, "Java 编程思想", "Java 经典书籍", 108.0));
        items.add(new Item(2L, "Spring Boot 实战", "Spring Boot 入门教程", 79.0));
        items.add(new Item(3L, "深入理解 JVM", "JVM 原理详解", 99.0));
        items.add(new Item(4L, "MySQL 必知必会", "数据库入门", 59.0));
        items.add(new Item(5L, "Redis 设计与实现", "Redis 原理", 89.0));
    }
    
    /**
     * GET /api/search?keyword=java
     * 根据关键词搜索
     * 
     * @param keyword 搜索关键词 (必须参数)
     * @return 匹配的项目列表
     */
    @GetMapping
    public List<Item> search(@RequestParam String keyword) {
        return items.stream()
                .filter(item -> item.getTitle().contains(keyword) 
                        || item.getDescription().contains(keyword))
                .collect(Collectors.toList());
    }
    
    /**
     * GET /api/search/filter?keyword=spring&page=0&size=10
     * 带分页的搜索
     * 
     * @param keyword 搜索关键词 (必须参数)
     * @param page 页码，默认 0
     * @param size 每页大小，默认 10
     * @return 匹配的项目列表
     */
    @GetMapping("/filter")
    public List<Item> searchWithPagination(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return items.stream()
                .filter(item -> item.getTitle().contains(keyword) 
                        || item.getDescription().contains(keyword))
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }
    
    /**
     * GET /api/search/category?category=book&minPrice=50&maxPrice=100
     * 按分类和价格范围搜索
     * 
     * @param category 分类 (可选)
     * @param minPrice 最低价格 (可选，默认为 0)
     * @param maxPrice 最高价格 (可选，默认为 Double.MAX_VALUE)
     * @return 匹配的项目列表
     */
    @GetMapping("/category")
    public List<Item> searchByCategory(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") double minPrice,
            @RequestParam(defaultValue = "999999") double maxPrice) {
        
        return items.stream()
                .filter(item -> item.getPrice() >= minPrice && item.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
}
