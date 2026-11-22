package org.example;

import org.example.controller.GreetingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GreetingController.class)          // 只加载这一个 Controller
class GreetingIntegrationTest01 {

    @Autowired
    private MockMvc mockMvc;                   // 模拟 HTTP 请求的核心工具

    // 如果 Controller 依赖了 Service，在这里用 @MockBean 替换
    // @MockBean
    // private SomeService someService;

    @Test
    void greet_shouldReturnDefaultMessage_whenNoNameParam() throws Exception {
        mockMvc.perform(get("/api/greeting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, World!"));
    }

    @Test
    void greet_shouldReturnCustomMessage_whenNameProvided() throws Exception {
        mockMvc.perform(get("/api/greeting").param("name", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Alice!"));
    }

    @Test
    void echo_shouldReturnReceivedContent() throws Exception {
        String jsonBody = """
                {"content": "test payload"}
                """;

        mockMvc.perform(post("/api/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.received").value("test payload"));
    }
}
