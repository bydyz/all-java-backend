package org.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "图书信息")
public class Book {

    @Schema(description = "唯一标识", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 200)
    @Schema(description = "书名", example = "Clean Code", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank
    @Schema(description = "作者", example = "Robert C. Martin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String author;

    @Schema(description = "ISBN 编号", example = "978-0132350884")
    private String isbn;

    @Schema(description = "价格（美元）", example = "29.99")
    private BigDecimal price;

    @Past
    @Schema(description = "出版日期", example = "2008-08-01")
    private LocalDate publicationDate;

    @Schema(description = "图书简介", example = "A handbook of agile software craftsmanship")
    private String description;
}
