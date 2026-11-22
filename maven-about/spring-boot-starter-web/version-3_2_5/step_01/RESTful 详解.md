## 一句话解释

**RESTful 是一种设计 Web API 的架构风格**：把系统里的东西都看作“资源”，用 **URL 定位资源**，用 **HTTP 方法表达操作**，用 **HTTP 状态码表达结果**，并且尽量做到**无状态、统一接口**。

注意：REST 不是协议，也不是标准，更不等于 JSON。RESTful 是“符合 REST 风格”的 API 设计方式。

---

## REST 的核心思想

| 概念 | 含义 | 例子 |
|---|---|---|
| 资源 Resource | 被操作的对象 | 用户、文章、订单、图书 |
| URI | 资源地址，用名词表示 | `/users`、`/books/123` |
| HTTP 方法 | 表示对资源做什么操作 | GET 查、POST 增、PUT/PATCH 改、DELETE 删 |
| 表现层 Representation | 资源的表现形式 | JSON、XML、HTML |
| 无状态 Stateless | 每个请求自带完成处理所需信息 | 每次请求都带 Token |
| 统一接口 | 用一致的方式操作资源 | 都用标准 HTTP 方法和状态码 |
| 状态码 | 表示请求结果 | 200、201、404、409 |

---

## 一个反例：不是 RESTful 的设计

假设图书管理 API 这样设计：

```http
GET /getAllBooks
GET /getBookById?id=123
POST /addBook
POST /deleteBook?id=123
POST /updateBook?id=123
```

问题：

1. URL 里带动词：`getAllBooks`、`addBook`、`deleteBook`；
2. 所有操作都用 GET/POST，没有利用 HTTP 方法语义；
3. 删除用 POST，不符合资源操作风格；
4. URL 设计不统一。

---

## RESTful 设计示例：图书管理 API

资源是“图书”，集合地址是 `/books`，单个图书地址是 `/books/{id}`。

### 1. 查询所有图书

```http
GET /books
```

响应：

```json
[
  {
    "id": 1,
    "title": "深入理解计算机系统",
    "author": "Randal E. Bryant",
    "price": 139.00
  },
  {
    "id": 2,
    "title": "代码大全",
    "author": "Steve McConnell",
    "price": 128.00
  }
]
```

含义：`GET /books` 表示获取图书集合。

---

### 2. 查询单本图书

```http
GET /books/1
```

响应：

```json
{
  "id": 1,
  "title": "深入理解计算机系统",
  "author": "Randal E. Bryant",
  "price": 139.00
}
```

如果不存在：

```http
HTTP/1.1 404 Not Found
```

```json
{
  "code": "BOOK_NOT_FOUND",
  "message": "图书不存在"
}
```

含义：URL 定位资源，状态码表达结果。

---

### 3. 新增图书

```http
POST /books
Content-Type: application/json

{
  "title": "重构",
  "author": "Martin Fowler",
  "price": 99.00
}
```

成功响应：

```http
HTTP/1.1 201 Created
Location: /books/3
```

```json
{
  "id": 3,
  "title": "重构",
  "author": "Martin Fowler",
  "price": 99.00
}
```

含义：

- `POST /books`：向图书集合新增资源；
- `201 Created`：创建成功；
- `Location`：新资源地址。

---

### 4. 全量更新图书

```http
PUT /books/3
Content-Type: application/json

{
  "title": "重构：改善既有代码的设计",
  "author": "Martin Fowler",
  "price": 109.00
}
```

响应：

```http
HTTP/1.1 200 OK
```

```json
{
  "id": 3,
  "title": "重构：改善既有代码的设计",
  "author": "Martin Fowler",
  "price": 109.00
}
```

`PUT` 通常表示“整体替换”。

---

### 5. 部分更新图书

```http
PATCH /books/3
Content-Type: application/json

{
  "price": 89.00
}
```

响应：

```json
{
  "id": 3,
  "title": "重构：改善既有代码的设计",
  "author": "Martin Fowler",
  "price": 89.00
}
```

`PATCH` 表示只修改部分字段。

---

### 6. 删除图书

```http
DELETE /books/3
```

成功响应：

```http
HTTP/1.1 204 No Content
```

含义：删除成功，没有响应体。

---

## HTTP 方法和语义

| 方法 | 作用 | 是否安全 | 是否幂等 | 示例 |
|---|---|---|---|---|
| GET | 查询资源 | 是 | 是 | `GET /books` |
| POST | 新增资源/提交操作 | 否 | 否 | `POST /books` |
| PUT | 整体更新或创建 | 否 | 是 | `PUT /books/3` |
| PATCH | 部分更新 | 否 | 通常不保证 | `PATCH /books/3` |
| DELETE | 删除资源 | 否 | 是 | `DELETE /books/3` |

- **安全**：不会修改资源，比如 GET；
- **幂等**：执行一次和执行多次结果一样。比如 `DELETE /books/3` 第一次删掉，后面再删结果仍是“资源不存在”，整体效果一致。

---

## 常见 RESTful URL 设计

### 集合和单个资源

```http
GET    /books          查询图书列表
POST   /books          新增图书
GET    /books/1        查询 id=1 的图书
PUT    /books/1        全量更新 id=1 的图书
PATCH  /books/1        部分更新 id=1 的图书
DELETE /books/1        删除 id=1 的图书
```

### 子资源

查询某个用户的订单：

```http
GET /users/1/orders
```

查询某个用户某个订单：

```http
GET /users/1/orders/100
```

### 过滤、排序、分页

```http
GET /books?author=Martin&sort=price,desc&page=2&size=20
```

表示：

- 作者是 Martin；
- 按价格降序；
- 第 2 页；
- 每页 20 条。

### 版本控制

```http
GET /v1/books
GET /v2/books
```

---

## 状态码示例

| 状态码 | 含义 | 场景 |
|---|---|---|
| 200 OK | 成功 | 查询成功、更新成功 |
| 201 Created | 已创建 | POST 新增成功 |
| 204 No Content | 成功但无响应体 | DELETE 成功 |
| 400 Bad Request | 请求参数错误 | JSON 格式错误 |
| 401 Unauthorized | 未认证 | 没带 Token |
| 403 Forbidden | 无权限 | 已登录但不能删除 |
| 404 Not Found | 资源不存在 | `/books/999` |
| 409 Conflict | 冲突 | 用户名已存在 |
| 500 Internal Server Error | 服务器错误 | 代码异常 |

不要所有响应都返回 `200`，然后在 JSON 里写：

```json
{
  "success": false,
  "message": "图书不存在"
}
```

更好的方式是 HTTP 状态码 + 业务错误码结合：

```http
HTTP/1.1 404 Not Found
```

```json
{
  "code": "BOOK_NOT_FOUND",
  "message": "图书不存在"
}
```

---

## 无状态是什么意思？

RESTful API 通常要求服务端不保存客户端会话状态。每个请求都要携带身份信息。

比如：

```http
GET /books
Authorization: Bearer eyJhbGciOi...
```

而不是：

1. 客户端先调用 `/login`；
2. 服务端在内存里记住“这个客户端已登录”；
3. 后续请求依赖这个内存会话。

无状态的好处：

- 更容易水平扩展；
- 多台服务器不用共享会话；
- 请求可独立重试；
- 更适合分布式系统。

---

## HATEOAS：REST 的进阶概念

HATEOAS 意思是响应里带上下一步可操作的链接。

例如：

```json
{
  "id": 1,
  "title": "深入理解计算机系统",
  "price": 139.00,
  "_links": {
    "self": { "href": "/books/1" },
    "update": { "href": "/books/1", "method": "PUT" },
    "delete": { "href": "/books/1", "method": "DELETE" },
    "author": { "href": "/authors/10" }
  }
}
```

它让客户端通过链接发现操作。实际项目中不一定强制使用，很多所谓 RESTful API 只做到“资源 + HTTP 方法 + 状态码”。

---

## RESTful 和 RPC 风格对比

RPC 风格更像“调用函数”：

```http
POST /createBook
POST /deleteBook
GET  /getBook?id=1
```

RESTful 风格更像“操作资源”：

```http
POST   /books
DELETE /books/1
GET    /books/1
```

RESTful 的关键不是“用了 JSON”，而是：

> **URL 表示资源，HTTP 方法表示动作，状态码表示结果，接口统一且无状态。**

---

## 设计 RESTful API 的实用规则

1. URL 用名词，不用动词：
  - 推荐：`/books`
  - 不推荐：`/getBooks`

2. 集合用复数：
  - `/users`、`/orders`、`/books`

3. 单个资源用 ID：
  - `/books/1`

4. 用 HTTP 方法表达操作：
  - GET 查、POST 增、PUT/PATCH 改、DELETE 删

5. 查询条件放 query 参数：
  - `/books?author=Martin&page=1&size=20`

6. 用状态码表达结果：
  - 201 创建成功、404 不存在、409 冲突

7. 保持无状态：
  - 每次请求带 Token，不依赖服务端会话

8. 返回统一错误结构：
   ```json
   {
     "code": "BOOK_NOT_FOUND",
     "message": "图书不存在",
     "traceId": "abc123"
   }
   ```

9. 版本放 URL 或 Header：
  - `/v1/books`

10. 不要把 RESTful 理解成“必须使用 JSON”：
  - JSON 最常见，但 XML、HTML、Protobuf 也可以作为表现层。

---

## 总结

RESTful 可以记成一句话：

> **用 URL 表示资源，用 HTTP 方法表示操作，用状态码表示结果，用统一、无状态的方式设计 Web API。**

图书 API 的典型 RESTful 设计就是：

```http
GET    /books        查询图书列表
POST   /books        新增图书
GET    /books/1      查询单本图书
PUT    /books/1      全量更新图书
PATCH  /books/1      部分更新图书
DELETE /books/1      删除图书
```

这就是最直观的 RESTful API 示例。
