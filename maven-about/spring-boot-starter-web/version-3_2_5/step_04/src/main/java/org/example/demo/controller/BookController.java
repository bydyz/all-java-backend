package org.example.demo.controller;

import org.example.demo.dto.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "图书管理", description = "图书的增删改查与分页接口")
public class BookController {

    // 实际项目中应注入 BookService，此处为演示省略

    @Operation(summary = "获取全部图书")
    @ApiResponse(responseCode = "200", description = "成功返回图书列表")
    @GetMapping
    public List<Book> getAllBooks() {
        return List.of(); // 示意
    }

    @Operation(summary = "分页查询图书")
    @GetMapping("/paginated")
    public Page<Book> getBooksPaginated(
            @ParameterObject Pageable pageable
    ) {
        return Page.empty(); // 示意
    }

    @Operation(summary = "根据 ID 获取图书")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "找到图书"),
            @ApiResponse(responseCode = "404", description = "图书不存在")
    })
    @GetMapping("/{id}")
    public Book getBookById(
            @Parameter(description = "图书 ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        return new Book(); // 示意
    }

    @Operation(summary = "新增图书")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "创建成功"),
            @ApiResponse(responseCode = "400", description = "参数校验失败")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "待创建的图书信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Book.class))
            )
            @Valid @RequestBody Book book
    ) {
        return book; // 示意
    }

    @Operation(summary = "更新图书")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "更新成功"),
            @ApiResponse(responseCode = "404", description = "图书不存在"),
            @ApiResponse(responseCode = "400", description = "参数校验失败")
    })
    @PutMapping("/{id}")
    public Book updateBook(
            @Parameter(description = "图书 ID", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Book book
    ) {
        return book; // 示意
    }

    @Operation(summary = "删除图书")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "删除成功"),
            @ApiResponse(responseCode = "404", description = "图书不存在")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(
            @Parameter(description = "图书 ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        // 调用删除逻辑
    }

    @Operation(summary = "按书名搜索（分页）")
    @GetMapping("/search")
    public Page<Book> searchBooks(
            @Parameter(description = "搜索关键词", example = "Clean")
            @RequestParam String query,
            @ParameterObject Pageable pageable
    ) {
        return Page.empty(); // 示意
    }
}
