package org.rc;

import org.junit.jupiter.api.Test;
import org.rc.controller.FileUploadController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileUploadController 单元测试
 */
@SpringBootTest
class FileUploadControllerTest {
    
    @Autowired
    private FileUploadController fileUploadController;
    
    @Test
    void testUploadFile() {
        // 创建模拟文件
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Hello, World!".getBytes()
        );
        
        Map<String, Object> result = fileUploadController.uploadFile(file);
        
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals("test.txt", result.get("fileName"));
    }
    
    @Test
    void testUploadEmptyFile() {
        // 创建空文件
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[0]
        );
        
        Map<String, Object> result = fileUploadController.uploadFile(file);
        
        assertNotNull(result);
        assertFalse((Boolean) result.get("success"));
        assertEquals("请选择文件", result.get("message"));
    }
    
    @Test
    void testUploadMultipleFiles() {
        // 创建多个模拟文件
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "file1.txt",
                "text/plain",
                "Content 1".getBytes()
        );
        
        MockMultipartFile file2 = new MockMultipartFile(
                "files",
                "file2.txt",
                "text/plain",
                "Content 2".getBytes()
        );
        
        MockMultipartFile[] files = {file1, file2};
        
        Map<String, Object> result = fileUploadController.uploadMultipleFiles(files);
        
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(2, result.get("totalFiles"));
    }
}
