package org.rc;

import org.junit.jupiter.api.Test;
import org.rc.controller.RequestInfoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RequestInfoController 单元测试
 */
@SpringBootTest
class RequestInfoControllerTest {
    
    @Autowired
    private RequestInfoController requestInfoController;
    
    @Test
    void testGetHeaders() {
        Map<String, String> headers = requestInfoController.getHeaders(
                "Mozilla/5.0", "application/json");
        
        assertNotNull(headers);
        assertEquals("Mozilla/5.0", headers.get("User-Agent"));
        assertEquals("application/json", headers.get("Accept"));
    }
    
    @Test
    void testGetCookies() {
        Map<String, String> cookies = requestInfoController.getCookies(
                "session-123", "user-456");
        
        assertNotNull(cookies);
        assertEquals("session-123", cookies.get("sessionId"));
        assertEquals("user-456", cookies.get("userId"));
    }
    
    @Test
    void testGetCookiesDefaultValues() {
        // 注意：@CookieValue 的 defaultValue 在直接调用时不生效
        // 它只在 HTTP 请求中生效
        Map<String, String> cookies = requestInfoController.getCookies(
                null, null);
        
        assertNotNull(cookies);
        assertNull(cookies.get("sessionId"));
        assertNull(cookies.get("userId"));
    }
    
    @Test
    void testCombinedExample() {
        Map<String, Object> result = requestInfoController.combinedExample(
                1L, "xml", "Mozilla/5.0");
        
        assertNotNull(result);
        assertEquals(1L, result.get("id"));
        assertEquals("xml", result.get("format"));
        assertEquals("Mozilla/5.0", result.get("userAgent"));
    }
}
