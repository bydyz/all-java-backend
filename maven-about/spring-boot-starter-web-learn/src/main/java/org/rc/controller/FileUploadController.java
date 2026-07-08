package org.rc.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器 - 演示文件上传处理
 * 
 * 演示内容：
 * 1. MultipartFile - 文件上传
 * 2. @RequestParam - 获取文件参数
 */
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    
    /**
     * POST /api/upload
     * 单文件上传
     * 
     * @param file 上传的文件
     * @return 上传结果
     */
    @PostMapping
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        if (file.isEmpty()) {
            result.put("success", false);
            result.put("message", "请选择文件");
            return result;
        }
        
        // 获取文件信息
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        String contentType = file.getContentType();
        
        // 模拟保存文件
        result.put("success", true);
        result.put("message", "文件上传成功");
        result.put("fileName", fileName);
        result.put("fileSize", fileSize);
        result.put("contentType", contentType);
        
        return result;
    }
    
    /**
     * POST /api/upload/multiple
     * 多文件上传
     * 
     * @param files 上传的文件数组
     * @return 上传结果
     */
    @PostMapping("/multiple")
    public Map<String, Object> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // 模拟保存每个文件
                successCount++;
            }
        }
        
        result.put("success", true);
        result.put("message", "成功上传 " + successCount + " 个文件");
        result.put("totalFiles", files.length);
        
        return result;
    }
}
