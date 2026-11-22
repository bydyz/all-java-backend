### `@ResponseStatus(HttpStatus.CREATED)` 的作用详解

在 Spring Boot 的 Controller 方法上，`@ResponseStatus(HttpStatus.CREATED)` 的核心作用是**修改 HTTP 响应的状态码**。具体到你的代码中，它的作用详解如下：

#### 1. 改变默认的状态码
在 Spring Boot 中，如果一个 Controller 方法正常执行完毕且没有抛出异常，框架默认会返回 HTTP 状态码 **`200 OK`**。当你加上了 `@ResponseStatus(HttpStatus.CREATED)` 后，只要这个方法成功执行，Spring 就会拦截默认的响应，并将 HTTP 状态码强制修改为 **`201 Created`**。

#### 2. 符合 RESTful 语义规范
在 RESTful API 设计中，不同的操作应该返回不同的状态码以明确告知客户端结果：
*   **`200 OK`**：通常用于查询（GET）或更新（PUT/PATCH）成功。
*   **`201 Created`**：专门用于**创建资源成功**的场景。

你的方法名是 `create`，且使用了 `@PostMapping`，这在语义上代表“新建数据”。因此，使用 `HttpStatus.CREATED`（201）比默认的 200 更加严谨和标准，前端或调用方看到 201 就能明确知道“资源已被成功创建”。

#### 3. 底层执行逻辑
当请求到达这个方法时：
1. Spring 解析 `@RequestBody` 并将 JSON 转换为 `Map`。
2. 方法体执行 `return body;`。
3. Spring 在将 `Map` 序列化为 JSON 写回给客户端之前，会检查方法上是否有 `@ResponseStatus` 注解。
4. 发现有该注解，于是调用底层的 `HttpServletResponse.setStatus(201)`，最终返回给客户端的 HTTP 头部信息中就会包含 `HTTP/1.1 201 Created`。

---

####  进阶对比：与 `ResponseEntity` 的区别

虽然 `@ResponseStatus` 很方便，但它在某些场景下不如 `ResponseEntity` 灵活。以下是两者的对比：

| 特性 | `@ResponseStatus` | `ResponseEntity` |
| :--- | :--- | :--- |
| **灵活性** | 静态的，状态码在编译时就固定了 | 动态的，可以根据业务逻辑在运行时决定返回 200、201 还是 400 |
| **控制粒度** | 只能控制状态码，响应体由方法返回值决定 | 可以同时控制状态码、响应体、甚至自定义 HTTP 请求头（如 `Location`） |
| **适用场景** | 简单的、状态码固定的成功响应（如你的 `create` 方法） | 复杂的业务逻辑、需要返回自定义错误信息或重定向的场景 |

**示例：如果你想在创建成功后，在响应头中返回新资源的访问路径（Location），`@ResponseStatus` 就做不到，必须用 `ResponseEntity`：**

```java
@PostMapping
public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
    // 假设这里生成了一个 ID
    String newId = "12345";

    // 构建新资源的 URI
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(newId).toUri();

    // 返回 201，并带上 Location 头
    return ResponseEntity.created(location).body(body);
}
