package org.example.controller;

import org.example.config.Phone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 演示 spring-boot-starter-validation 的所有常见用法
 *
 * 说明：
 *  - @Valid     : jakarta.validation 的标准注解，用于级联/嵌套校验
 *  - @Validated : Spring 的注解，是 @Valid 的增强版，支持"分组校验"，
 *                 标在类上还可以开启"方法参数（路径/查询参数）校验"
 */
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    // ============================================================
    // 场景 1：@RequestBody + @Valid  —— 最常见的用法
    // 校验失败会抛出 MethodArgumentNotValidException
    // ============================================================
    @PostMapping
    public String create(@Valid @RequestBody UserCreateRequest request) {
        return "创建成功：" + request.getUsername();
    }

    // ============================================================
    // 场景 2：@Valid + BindingResult —— 手动处理错误，不抛异常
    // 注意：BindingResult 必须紧跟在被校验参数后面
    // ============================================================
    @PostMapping("/with-result")
    public Object createWithBindingResult(@Valid @RequestBody UserCreateRequest request,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 收集所有字段错误 -> {字段名: 错误信息}
            return bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            fe -> fe.getField(),
                            fe -> fe.getDefaultMessage(),
                            (a, b) -> a));
        }
        return Map.of("msg", "创建成功", "username", request.getUsername());
    }

    // ============================================================
    // 场景 3：分组校验
    // 新增时校验 username 必填，更新时不校验 username
    // ============================================================
    @PostMapping("/group-create")
    public String createByGroup(
            @Validated(UserCreateRequest.Create.class) @RequestBody UserCreateRequest request) {
        return "分组新增：" + request.getUsername();
    }

    @PutMapping("/group-update")
    public String updateByGroup(
            @Validated(UserCreateRequest.Update.class) @RequestBody UserCreateRequest request) {
        return "分组更新，id=" + request.getId();
    }

    // ============================================================
    // 场景 4：路径变量校验（需要类上有 @Validated）
    // 校验失败抛 ConstraintViolationException（Spring 6.1 内置校验下为
    // HandlerMethodValidationException，见异常处理器）
    // ============================================================
    @GetMapping("/{id}")
    public String getById(
            @PathVariable @NotNull @Min(value = 1, message = "id 最小为 1")
            @Max(value = 999_999, message = "id 最大为 999999") Long id) {
        return "查询用户 id=" + id;
    }

    // ============================================================
    // 场景 5：查询参数 / 表单参数校验
    // ============================================================
    @GetMapping("/search")
    public String search(
            @RequestParam @NotBlank(message = "关键字不能为空")
            @Size(max = 20, message = "关键字最长 20 个字符") String keyword,

            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "页码从 1 开始") int page,

            @RequestParam(defaultValue = "10")
            @Min(1) @Max(100) int size,

            @RequestParam(required = false)
            @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式 yyyy-MM-dd")
            String date) {

        return String.format("keyword=%s, page=%d, size=%d, date=%s", keyword, page, size, date);
    }

    // ============================================================
    // 场景 6：集合 / 数组元素校验（@Size 作用于集合本身，@Valid 作用于元素）
    // ============================================================
    @PostMapping("/batch")
    public String batch(
            @RequestBody
            @NotEmpty(message = "批次不能为空")
            @Size(max = 10, message = "一次最多 10 条")
            List<@Valid @NotNull UserCreateRequest> users) {
        return "批量创建 " + users.size() + " 条";
    }

    // ============================================================
    // 场景 7：参数上的 @Valid 用于级联校验（对象作为查询参数/表单对象）
    // ============================================================
    @GetMapping("/page")
    public String page(@Valid PageQuery query) {
        return "page=" + query.getPage() + ", size=" + query.getSize();
    }

    // ============================================================
    // 场景 8：自定义约束注解 @Phone
    // ============================================================
    @PostMapping("/bind-phone")
    public String bindPhone(
            @RequestParam @NotBlank @Phone String phone) {
        return "绑定手机号：" + phone;
    }

    // ============================================================
    // 场景 9：@AssertTrue / @AssertFalse（布尔断言，常用于"同意协议"）
    // 已放在 DTO 的 agree 字段上演示
    // ============================================================

    // ============================================================
    // 场景 10：复合约束 —— @Email、@Past、@Future、@Positive、@Negative
    // 已放在 DTO 字段上演示
    // ============================================================


    // =================================================================
    // DTO 定义
    // =================================================================

    /** 创建/更新请求体 */
    public static class UserCreateRequest {

        /** 分组标记：新增 */
        public interface Create {}
        /** 分组标记：更新 */
        public interface Update {}

        // 只在 Update 分组校验
        @NotNull(message = "更新时 id 不能为空", groups = Update.class)
        @Positive(message = "id 必须为正数", groups = Update.class)
        private Long id;

        // 只在 Create 分组校验
        @NotBlank(message = "用户名不能为空", groups = Create.class)
        @Size(min = 3, max = 20, message = "用户名长度 3~20", groups = Create.class)
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字、下划线",
                groups = Create.class)
        private String username;

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 32, message = "密码长度 6~32")
        private String password;

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        @NotNull(message = "年龄不能为空")
        @Min(value = 1, message = "年龄最小为 1")
        @Max(value = 150, message = "年龄最大为 150")
        private Integer age;

        @Past(message = "生日必须是过去的日期")
        private LocalDate birthday;

        @NotNull(message = "余额不能为空")
        @DecimalMin(value = "0.00", message = "余额不能为负")
        @Digits(integer = 8, fraction = 2, message = "余额整数最多 8 位，小数最多 2 位")
        private BigDecimal balance;

        @NotEmpty(message = "标签不能为空")
        @Size(max = 5, message = "最多 5 个标签")
        private List<@NotBlank(message = "标签不能为空白") String> tags;

        /** 嵌套对象校验：必须加 @Valid 才会级联校验内部字段 */
        @Valid
        @NotNull(message = "地址不能为空")
        private Address address;

        /** 集合内对象校验 */
        @Valid
        @Size(max = 3, message = "最多 3 个收货地址")
        private List<Address> addresses;

        /** 布尔断言 */
        @AssertTrue(message = "必须同意用户协议")
        private Boolean agree;

        // ---------- getter / setter（省略，实际工程用 Lombok @Data） ----------
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public LocalDate getBirthday() { return birthday; }
        public void setBirthday(LocalDate birthday) { this.birthday = birthday; }
        public BigDecimal getBalance() { return balance; }
        public void setBalance(BigDecimal balance) { this.balance = balance; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public Address getAddress() { return address; }
        public void setAddress(Address address) { this.address = address; }
        public List<Address> getAddresses() { return addresses; }
        public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
        public Boolean getAgree() { return agree; }
        public void setAgree(Boolean agree) { this.agree = agree; }
    }

    /** 嵌套对象 */
    public static class Address {

        @NotBlank(message = "省份不能为空")
        private String province;

        @NotBlank(message = "城市不能为空")
        private String city;

        @NotBlank(message = "详细地址不能为空")
        @Size(max = 100, message = "详细地址最多 100 字")
        private String detail;

        @Pattern(regexp = "^\\d{6}$", message = "邮编必须是 6 位数字")
        private String zipCode;

        public String getProvince() { return province; }
        public void setProvince(String province) { this.province = province; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getDetail() { return detail; }
        public void setDetail(String detail) { this.detail = detail; }
        public String getZipCode() { return zipCode; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    }

    /** 作为查询参数绑定的对象 */
    public static class PageQuery {

        @Min(value = 1, message = "页码从 1 开始")
        private int page = 1;

        @Min(value = 1, message = "每页至少 1 条")
        @Max(value = 100, message = "每页最多 100 条")
        private int size = 10;

        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
    }
}
