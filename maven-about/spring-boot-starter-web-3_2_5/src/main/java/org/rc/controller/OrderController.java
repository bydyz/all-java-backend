package org.rc.controller;

import org.rc.model.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单控制器 - 演示复杂查询参数绑定
 * 
 * 演示内容：
 * 1. @DateTimeFormat - 日期时间格式化
 * 2. 多个可选查询参数组合
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    // 模拟数据库
    private final List<Order> orders = new ArrayList<>();
    
    public OrderController() {
        orders.add(new Order(1L, "ORD001", "pending", 199.0, LocalDateTime.of(2024, 1, 15, 10, 0)));
        orders.add(new Order(2L, "ORD002", "completed", 299.0, LocalDateTime.of(2024, 2, 20, 14, 30)));
        orders.add(new Order(3L, "ORD003", "pending", 99.0, LocalDateTime.of(2024, 3, 10, 9, 0)));
        orders.add(new Order(4L, "ORD004", "cancelled", 399.0, LocalDateTime.of(2024, 4, 5, 16, 45)));
        orders.add(new Order(5L, "ORD005", "completed", 599.0, LocalDateTime.of(2024, 5, 1, 11, 20)));
    }
    
    /**
     * GET /api/orders
     * 获取所有订单
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orders;
    }
    
    /**
     * GET /api/orders/filter?status=pending&startDate=2024-01-01T00:00:00&endDate=2024-03-31T23:59:59
     * 按状态和日期范围筛选订单
     * 
     * @param status 订单状态 (可选)
     * @param startDate 开始日期 (可选)
     * @param endDate 结束日期 (可选)
     * @return 筛选后的订单列表
     */
    @GetMapping("/filter")
    public List<Order> filterOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        return orders.stream()
                .filter(order -> status == null || order.getStatus().equals(status))
                .filter(order -> startDate == null || order.getCreateTime().isAfter(startDate))
                .filter(order -> endDate == null || order.getCreateTime().isBefore(endDate))
                .collect(Collectors.toList());
    }
}
