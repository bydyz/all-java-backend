# springdoc-openapi 中 Swagger UI 同一 Controller 内接口排序详解

在 springdoc-openapi 工程中，Swagger UI 里同一个 Controller 内接口的排序，**默认并没有一个固定的“业务顺序”**。它本质上是按照 OpenAPI 规范文档（`/v3/api-docs`）中接口的生成顺序来展示的，而这个顺序在多数情况下并不可靠，因此常给人“乱序”的感觉。不过，springdoc 提供了从全局到局部的多种方式来调整这个顺序。

---

## 一、第一层：全局排序（`operationsSorter`）

这是影响同一 Controller 内所有接口顺序最直接的方式。在 `application.yml` 中配置 `springdoc.swagger-ui.operationsSorter` 即可。

```yaml
springdoc:
  swagger-ui:
    operations-sorter: alpha # 可选值: alpha, method, order (自定义时需配合注解)
```

`operationsSorter` 的可选值与效果如下：

| 配置值 | 排序效果 |
|---|---|
| `alpha` | 按照接口的**路径（Path）**进行字母顺序排序。例如 `/users/create` 会排在 `/users/list` 前面。 |
| `method` | 按照 **HTTP 方法**排序。常见的顺序是 `DELETE`、`GET`、`POST`、`PUT` 等。 |
| `order` | 这是一种**自定义排序**模式。启用后，Swagger UI 会读取每个接口上通过注解或编程方式设定的具体顺序值来进行排列。 |
| 不配置（默认） | 保持服务器端返回的原始顺序不变。但如前所述，这个原始顺序对业务展示来说通常不够理想。 |

> **注意**：`operationsSorter` 只控制**同一个 Controller 内部**接口的顺序。如果你还想控制不同 Controller（在 Swagger UI 中通常表现为 Tag）之间的顺序，则需要配置 `springdoc.swagger-ui.tagsSorter`，常用值为 `alpha`。

---

## 二、第二层：精细化自定义排序（`order` 属性）

当你希望接口严格按照**业务逻辑**（如“创建 -> 查询 -> 更新 -> 删除”）排列，而不仅仅是字母或 HTTP 方法顺序时，就需要使用自定义排序。

### 1. 在接口方法上使用 `@Operation` 注解

直接在 Controller 的方法上，通过 `@Operation` 注解的 `order` 属性指定一个整数值。**值越小，排序越靠前**。

```java
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Operation(summary = "创建用户", order = 1) // order 值最小，排最前
    @PostMapping
    public User create(...) { ... }

    @Operation(summary = "查询用户列表", order = 2)
    @GetMapping
    public List<User> list(...) { ... }

    @Operation(summary = "更新用户", order = 3)
    @PutMapping("/{id}")
    public User update(...) { ... }
}
```

**关键一步**：要让 `@Operation(order = ...)` 生效，必须将 `operationsSorter` 设置为 **`order`**：

```yaml
springdoc:
  swagger-ui:
    operations-sorter: order
```

有资料将其误写为 `@Operation(sort = n)`，但在 springdoc 的常见实践中，`order` 是配合 `operationsSorter: order` 生效的通用属性。

### 2. 使用 `OperationCustomizer` 进行全局批量排序

如果 Controller 很多，逐个方法添加注解会很繁琐。这时可以实现 `OperationCustomizer` 接口，在程序层面**自动为不同 Controller 的接口分配排序值**。

例如，可以编写一个自定义器，让所有 `/v1` 开头的 Controller 下的接口，其排序值都加上 100；`/v2` 开头的则加上 200。这样就能在全局范围内，先按 API 版本分组，再在组内按接口自身的 `order` 值排序。

```java
@Bean
public OperationCustomizer versionBasedCustomizer() {
    return (operation, handlerMethod) -> {
        // 获取 Controller 上的 @RequestMapping 路径前缀
        String prefix = handlerMethod.getBeanType().getAnnotation(RequestMapping.class).path()[0];
        if (prefix.startsWith("/v1")) {
            operation.setOrder(operation.getOrder() + 100); // 为 v1 组接口的 order 值加偏移量
        } else if (prefix.startsWith("/v2")) {
            operation.setOrder(operation.getOrder() + 200); // 为 v2 组接口的 order 值加偏移量
        }
        return operation;
    };
}
```

---

## 三、总结

调整同一 Controller 内接口顺序的决策路径很清晰：

1. **接受简单规则**：如果按路径或 HTTP 方法排列就足够，直接配置 `operations-sorter: alpha` 或 `method`。
2. **需要业务顺序**：为每个方法添加 `@Operation(order = n)` 注解，并配置 `operations-sorter: order`。这是最直观的精细化控制方式。
3. **大规模自动化**：当 Controller 数量众多，需要按版本、模块等维度进行全局编排时，实现 `OperationCustomizer` 来动态注入排序逻辑是最高效的方案。
