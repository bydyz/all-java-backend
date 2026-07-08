package org.rc;

import org.junit.jupiter.api.Test;
import org.rc.controller.SearchController;
import org.rc.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SearchController 单元测试
 */
@SpringBootTest
class SearchControllerTest {
    
    @Autowired
    private SearchController searchController;
    
    @Test
    void testSearch() {
        List<Item> results = searchController.search("Java");
        assertNotNull(results);
        assertFalse(results.isEmpty());
        
        // 验证搜索结果包含关键词
        assertTrue(results.stream()
                .anyMatch(item -> item.getTitle().contains("Java")));
    }
    
    @Test
    void testSearchWithPagination() {
        List<Item> results = searchController.searchWithPagination("Spring", 0, 10);
        assertNotNull(results);
    }
    
    @Test
    void testSearchByCategory() {
        List<Item> results = searchController.searchByCategory("book", 50.0, 150.0);
        assertNotNull(results);
        
        // 验证价格范围
        assertTrue(results.stream()
                .allMatch(item -> item.getPrice() >= 50.0 && item.getPrice() <= 150.0));
    }
    
    @Test
    void testSearchNoResults() {
        List<Item> results = searchController.search("不存在的关键词");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}
