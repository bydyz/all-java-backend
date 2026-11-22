package org.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/all-in-one")
@CrossOrigin(origins = "*")
public class AllInOne {

    // ① GET + @RequestParam：查询字符串 / 表单参数
    @GetMapping
    public List<String> list(@RequestParam(required = false) String name,
                             @RequestParam(defaultValue = "0") int page) {
        return List.of("name=" + name, "page=" + page);
    }

    // ② GET + @PathVariable + ResponseEntity
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("id", id));
    }

    // ③ POST + @RequestBody + @ResponseStatus
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody Map<String, Object> body) {
        return body;
    }

    // ④ PUT + @PathVariable + @RequestBody
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id,
                                      @RequestBody Map<String, Object> body) {
        return Map.of("id", id, "body", body);
    }

    // ⑤ PATCH + @PathVariable + @RequestBody
    @PatchMapping("/{id}")
    public Map<String, Object> patch(@PathVariable Long id,
                                     @RequestBody Map<String, Object> updates) {
        return Map.of("id", id, "updates", updates);
    }

    // ⑥ DELETE + @PathVariable + @ResponseStatus
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        // 示例不调用服务
    }

    // ⑦ @RequestHeader + @CookieValue
    @GetMapping("/headers")
    public Map<String, String> headers(
            @RequestHeader("User-Agent") String userAgent,
            @CookieValue(value = "sessionId", required = false) String sessionId) {

        Map<String, String> result = new HashMap<>();
        result.put("userAgent", userAgent);
        result.put("sessionId", sessionId);
        return result;
    }

    // ⑧ @RequestPart：multipart/form-data 文件上传
    @PostMapping("/{id}/avatar")
    public Map<String, Object> uploadAvatar(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) {

        return Map.of(
                "id", id,
                "filename", String.valueOf(file.getOriginalFilename())
        );
    }

    // ⑨ @ModelAttribute：绑定请求参数到对象
    // 这里用 Map 占位，实际项目建议绑定专用查询 POJO
    @GetMapping("/search")
    public Map<String, Object> search(
            @ModelAttribute("query") Map<String, String> query) {
        return Map.of("query", query);
    }

    // ⑩ @ResponseBody：@RestController 下已隐式生效，这里显式写出仅作展示
    @GetMapping("/response-body")
    @ResponseBody
    public String responseBody() {
        return "plain text";
    }

    // ⑪ 局部异常处理：@ExceptionHandler + @ResponseStatus
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("error", String.valueOf(ex.getMessage()));
    }
}
