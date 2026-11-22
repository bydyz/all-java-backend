在HTTP请求头中设置 `Content-Type: application/json`，是告诉服务器“**我发送的请求体数据是JSON格式，请按JSON规则来解析它**”。这是确保数据被正确理解的关键一步。

### 🎯 `Content-Type: application/json` 的核心作用

这个请求头主要承担着“**数据格式声明**”的角色，具体体现在：

*   **明确数据格式，指导服务器解析**：HTTP协议本身只是一些字节流，`Content-Type`头就是**MIME类型**的声明，它明确告知服务器请求体的媒体类型是`application/json`。这样，服务器（如Spring Boot的`@RequestBody`注解）才知道应该调用JSON解析器来反序列化数据，而不是按表单或纯文本处理。
*   **触发正确的业务逻辑与安全机制**：对于RESTful API和前后端分离架构，JSON是主流的数据交换格式。正确设置此头是服务端启用JSON绑定支持的前提。此外，它也是**CORS（跨域资源共享）预检机制**的一部分。`application/json`不属于“简单请求”的Content-Type，浏览器会自动发起`OPTIONS`预检请求。若服务器未正确配置响应头，跨域请求会被浏览器拦截，这从侧面保证了通信的安全可控。

### ⚠️ 未指定`Content-Type`会发生什么？

如果请求体是JSON但未设置此头，后果取决于**服务器端的实现策略**。主要有以下几种情况：

#### 情况一：服务器返回 `415 Unsupported Media Type` (严格模式)
这是**最常见且规范**的后果。许多现代后端框架（如**Spring Boot**）会严格校验`Content-Type`，如果缺失或类型不匹配，会直接拒绝请求并返回 **HTTP 415 Unsupported Media Type** 状态码。例如，Spring Boot的`@RequestBody`注解在检测不到`application/json`或发现是`text/plain`时，会抛出`HttpMediaTypeNotSupportedException`，导致请求失败。

#### 情况二：服务器使用默认值解析 (宽松模式)
部分服务器或API框架在缺失`Content-Type`时会采用默认值。
*   一些API设计规范（如AIP-137）建议，当此头缺失时，**默认按`application/json; charset=utf-8`处理**，并要求API不得拒绝此类请求。
*   其他一些框架或服务器可能有各自的默认值（如`text/plain`或`application/x-www-form-urlencoded`），这可能导致JSON数据被错误地解析为空值或乱码。

#### 情况三：CORS 预检失败 (前端场景)
在浏览器环境中，未设置`Content-Type`的`fetch`请求可能不会触发CORS预检，但如果服务器期望的是`application/json`，跨域请求仍可能因“简单请求”的规则限制而无法获得预期的响应，或在预检阶段因头部缺失而被拦截。

#### 情况四：静默失败或难以调试的错误
这是一种**隐蔽且危险**的情况。服务器可能返回 `200 OK`，但后端逻辑因无法解析JSON而**静默失败**，没有执行任何操作。这会导致前端看起来请求成功，但实际数据并未被处理，极大地增加了调试的难度。

### 💎 总结与最佳实践

`Content-Type: application/json`是HTTP通信中一份明确的 **“数据格式契约”** 。忽略它，就如同寄出一封没写语言种类的信，收信人可能猜错而无法理解，甚至直接拒收。

**最佳实践是在发送任何JSON请求体时，都显式设置此请求头**。不同客户端的行为差异如下：

| 客户端 | 默认 Content-Type 行为 | 正确设置方式 |
| :--- | :--- | :--- |
| **`fetch` API** | **默认不设置**`Content-Type`，需手动指定 | `headers: {'Content-Type': 'application/json'}` |
| **`XMLHttpRequest`** | 默认为 `text/plain`，需手动覆盖 | `xhr.setRequestHeader('Content-Type', 'application/json')` |
| **jQuery `$.ajax`** | 默认为 `application/x-www-form-urlencoded` | 显式设置 `contentType: 'application/json'`，并手动`JSON.stringify`数据 |
