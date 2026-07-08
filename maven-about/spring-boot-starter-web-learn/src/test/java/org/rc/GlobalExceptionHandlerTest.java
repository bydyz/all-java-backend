package org.rc;

import org.junit.jupiter.api.Test;
import org.rc.exception.ErrorResponse;
import org.rc.exception.GlobalExceptionHandler;
import org.rc.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GlobalExceptionHandler 单元测试
 */
@SpringBootTest
class GlobalExceptionHandlerTest {
    
    @Autowired
    private GlobalExceptionHandler exceptionHandler;
    
    @Test
    void testHandleNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User", 999L);
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNotFound(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NOT_FOUND", response.getBody().getCode());
        assertTrue(response.getBody().getMessage().contains("999"));
    }
    
    @Test
    void testHandleBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBadRequest(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("BAD_REQUEST", response.getBody().getCode());
        assertEquals("Invalid input", response.getBody().getMessage());
    }
    
    @Test
    void testHandleGeneral() {
        Exception ex = new RuntimeException("Something went wrong");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGeneral(ex);
        
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_ERROR", response.getBody().getCode());
    }
}
