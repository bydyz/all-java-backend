package org.rc;

import org.junit.jupiter.api.Test;
import org.rc.controller.OrderController;
import org.rc.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderController 单元测试
 */
@SpringBootTest
class OrderControllerTest {
    
    @Autowired
    private OrderController orderController;
    
    @Test
    void testGetAllOrders() {
        List<Order> orders = orderController.getAllOrders();
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(5, orders.size());
    }
    
    @Test
    void testFilterOrdersByStatus() {
        List<Order> pendingOrders = orderController.filterOrders(
                "pending", null, null);
        
        assertNotNull(pendingOrders);
        assertTrue(pendingOrders.stream()
                .allMatch(order -> "pending".equals(order.getStatus())));
    }
    
    @Test
    void testFilterOrdersByDateRange() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 3, 31, 23, 59);
        
        List<Order> filteredOrders = orderController.filterOrders(
                null, startDate, endDate);
        
        assertNotNull(filteredOrders);
        assertTrue(filteredOrders.stream()
                .allMatch(order -> 
                    order.getCreateTime().isAfter(startDate) && 
                    order.getCreateTime().isBefore(endDate)));
    }
    
    @Test
    void testFilterOrdersCombined() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        
        List<Order> completedOrders = orderController.filterOrders(
                "completed", startDate, endDate);
        
        assertNotNull(completedOrders);
        assertTrue(completedOrders.stream()
                .allMatch(order -> "completed".equals(order.getStatus())));
    }
}
