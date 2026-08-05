# Phase 8：请求处理流程

## 概述

Phase 8 展示了 Spring Boot RBAC 系统的请求处理流程，以登录为例，详细说明从客户端发送请求到服务器返回响应的完整链路。

---

## 8.1 请求处理时序图

```
┌─────────────────────────────────────────────────────────────────┐
│                    请求处理时序 (以登录为例)                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Client                  Server                                │
│   │                       │                                    │
│   │  POST /api/auth/login │                                    │
│   │  {username, password} │                                    │
│   │──────────────────────▶│                                    │
│   │                       │                                    │
│   │                       │  1. CorsFilter                     │
│   │                       │     └── 跨域处理                   │
│   │                       │                                    │
│   │                       │  2. SecurityFilterChain            │
│   │                       │     ├── 匹配 permitAll URL         │
│   │                       │     └── 放行 (不需要认证)          │
│   │                       │                                    │
│   │                       │  3. JwtAuthenticationFilter        │
│   │                       │     └── 无token, 放行              │
│   │                       │                                    │
│   │                       │  4. AuthController.login()         │
│   │                       │     └── UserServiceImpl.login()    │
│   │                       │         ├── authenticationManager  │
│   │                       │         │   .authenticate()        │
│   │                       │         │   └── 调用UserDetailsService│
│   │                       │         │       .loadUserByUsername()│
│   │                       │         │       └── UserMapper查询  │
│   │                       │         │           ├── selectByUsername()
│   │                       │         │           └── selectPermissions()
│   │                       │         │                           │
│   │                       │         ├── JwtTokenProvider        │
│   │                       │         │   .generateToken()       │
│   │                       │         │                           │
│   │                       │         └── Redis存储token          │
│   │                       │             .set("token:xxx","1")  │
│   │                       │                                    │
│   │  Response:            │                                    │
│   │  {                   │                                    │
│   │    "code": 200,      │                                    │
│   │    "data": {         │                                    │
│   │      "token": "eyJhbG",│                                  │
│   │      "userInfo": {...}│                                    │
│   │    }                 │                                    │
│   │  }                   │                                    │
│   │◀──────────────────────│                                    │
│   │                       │                                    │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8.2 请求处理详细流程

### 8.2.1 步骤 1：客户端发送请求

```bash
# 登录请求示例
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**请求头：**

| Header | Value | 说明 |
|--------|-------|------|
| Content-Type | application/json | 请求体格式 |
| Origin | http://localhost:3000 | 来源地址（可选） |

**请求体：**

```json
{
  "username": "admin",
  "password": "admin123"
}
```

---

### 8.2.2 步骤 2：CorsFilter 处理

`CorsConfig.java:16-33`

```java
@Bean
public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    // 允许所有来源
    config.addAllowedOriginPattern("*");
    // 允许所有头
    config.addAllowedHeader("*");
    // 允许所有方法
    config.addAllowedMethod("*");
    // 允许携带凭证
    config.setAllowCredentials(true);
    // 预检请求有效期（秒）
    config.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    
    return new CorsFilter(source);
}
```

**处理逻辑：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    CorsFilter 处理逻辑                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 检查请求方法                                                 │
│     └── 如果是 OPTIONS 预检请求，返回 CORS 响应头                 │
│                                                                 │
│  2. 添加 CORS 响应头                                             │
│     ├── Access-Control-Allow-Origin: *                          │
│     ├── Access-Control-Allow-Methods: GET, POST, PUT, DELETE    │
│     ├── Access-Control-Allow-Headers: *                         │
│     ├── Access-Control-Allow-Credentials: true                  │
│     └── Access-Control-Max-Age: 3600                            │
│                                                                 │
│  3. 放行请求                                                     │
│     └── filterChain.doFilter(request, response)                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

### 8.2.3 步骤 3：SecurityFilterChain 处理

`SecurityConfig.java:34-54`

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(
                "/api/auth/login",
                "/api/auth/register",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v3/api-docs/**"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
}
```

**处理逻辑：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    SecurityFilterChain 处理逻辑                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 检查 CSRF                                                   │
│     └── 已禁用，跳过                                             │
│                                                                 │
│  2. 检查会话管理                                                 │
│     └── STATELESS，不创建 HTTP Session                           │
│                                                                 │
│  3. 匹配 URL 规则                                                │
│     ├── /api/auth/login → permitAll                             │
│     └── 匹配成功，放行                                           │
│                                                                 │
│  4. 放行请求                                                     │
│     └── filterChain.doFilter(request, response)                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

### 8.2.4 步骤 4：JwtAuthenticationFilter 处理

`JwtAuthenticationFilter.java:34-61`

```java
@Override
protected void doFilterInternal(HttpServletRequest request, 
                                HttpServletResponse response, 
                                FilterChain filterChain) throws ServletException, IOException {
    try {
        String jwt = getJwtFromRequest(request);
        
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            // 检查 token 是否在 Redis 中
            String tokenKey = "token:" + jwt;
            String userId = redisTemplate.opsForValue().get(tokenKey);
            
            if (userId != null) {
                String username = jwtTokenProvider.getUsernameFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    } catch (Exception ex) {
        log.error("Could not set user authentication in security context", ex);
    }
    
    filterChain.doFilter(request, response);
}
```

**处理逻辑：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    JwtAuthenticationFilter 处理逻辑              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 从请求头提取 Token                                           │
│     └── Authorization: Bearer eyJhbG...                         │
│                                                                 │
│  2. 检查 Token 是否存在                                          │
│     └── 登录请求无 Token，跳过认证                                │
│                                                                 │
│  3. 放行请求                                                     │
│     └── filterChain.doFilter(request, response)                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

### 8.2.5 步骤 5：AuthController 处理

`AuthController.java:27-36`

```java
@Operation(summary = "用户登录")
@PostMapping("/login")
public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
        LoginResponse response = userService.login(request);
        return Result.success("登录成功", response);
    } catch (Exception e) {
        return Result.error("登录失败: " + e.getMessage());
    }
}
```

**处理逻辑：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    AuthController 处理逻辑                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 接收 LoginRequest                                            │
│     ├── username: "admin"                                       │
│     └── password: "admin123"                                    │
│                                                                 │
│  2. 调用 UserService.login()                                     │
│     └── 返回 LoginResponse                                      │
│                                                                 │
│  3. 构建 Result 响应                                             │
│     └── Result.success("登录成功", response)                     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

### 8.2.6 步骤 6：UserServiceImpl 处理登录

`UserServiceImpl.java:56-98`

```java
@Override
public LoginResponse login(LoginRequest request) {
    // 认证
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
    
    // 生成token
    String token = jwtTokenProvider.generateToken(authentication);
    
    // 获取用户信息
    User user = userMapper.selectByUsername(request.getUsername());
    
    // 获取角色列表
    List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
    List<String> roleKeys = roles.stream().map(Role::getRoleKey).collect(Collectors.toList());
    
    // 获取权限列表
    List<String> permissions = userMapper.selectPermissionsByUserId(user.getId());
    
    // 构建响应
    LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
    userInfo.setId(user.getId());
    userInfo.setUsername(user.getUsername());
    userInfo.setNickname(user.getNickname());
    userInfo.setAvatar(user.getAvatar());
    userInfo.setRoles(roleKeys);
    userInfo.setPermissions(permissions);
    
    LoginResponse response = new LoginResponse();
    response.setToken(token);
    response.setUserInfo(userInfo);
    
    // 存储token到Redis
    redisTemplate.opsForValue().set(
            "token:" + token,
            user.getId().toString(),
            24, TimeUnit.HOURS
    );
    
    return response;
}
```

**处理逻辑：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    UserServiceImpl 登录处理逻辑                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 认证                                                         │
│     └── authenticationManager.authenticate()                     │
│         ├── 创建 UsernamePasswordAuthenticationToken             │
│         ├── 调用 UserDetailsService.loadUserByUsername()         │
│         │   └── UserMapper.selectByUsername("admin")            │
│         │   └── UserMapper.selectPermissionsByUserId(1)         │
│         └── 验证密码                                              │
│             └── PasswordEncoder.matches("admin123", encodedPwd) │
│                                                                 │
│  2. 生成 Token                                                    │
│     └── jwtTokenProvider.generateToken()                         │
│         ├── subject: "admin"                                    │
│         ├── issuedAt: new Date()                                │
│         └── expiration: new Date(now + 86400000)                │
│                                                                 │
│  3. 获取用户信息                                                  │
│     ├── UserMapper.selectByUsername("admin")                    │
│     ├── RoleMapper.selectRolesByUserId(1)                       │
│     └── UserMapper.selectPermissionsByUserId(1)                 │
│                                                                 │
│  4. 存储 Token 到 Redis                                          │
│     └── redisTemplate.set("token:xxx", "1", 24, HOURS)         │
│                                                                 │
│  5. 返回 LoginResponse                                           │
│     ├── token: "eyJhbG..."                                      │
│     └── userInfo: {id, username, nickname, roles, permissions}  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

### 8.2.7 步骤 7：生成 JWT Token

`JwtTokenProvider.java:30-41`

```java
public String generateToken(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpiration);
    
    return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey())
            .compact();
}
```

**Token 生成流程：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    JWT Token 生成流程                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 获取用户信息                                                  │
│     └── userDetails.getUsername() → "admin"                     │
│                                                                 │
│  2. 设置 Token 属性                                              │
│     ├── subject: "admin"                                        │
│     ├── issuedAt: 2026-08-05 23:58:00                          │
│     └── expiration: 2026-08-06 23:58:00                        │
│                                                                 │
│  3. 签名 Token                                                   │
│     └── HMAC-SHA256(header + payload, secret)                   │
│                                                                 │
│  4. 返回 Token 字符串                                            │
│     └── eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIuLi4.          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

### 8.2.8 步骤 8：存储 Token 到 Redis

```java
// 存储token到Redis
redisTemplate.opsForValue().set(
        "token:" + token,
        user.getId().toString(),
        24, TimeUnit.HOURS
);
```

**Redis 存储结构：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    Redis Token 存储                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Key: token:eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIuLi4.      │
│  Value: 1 (userId)                                              │
│  TTL: 86400 秒 (24小时)                                         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

### 8.2.9 步骤 9：返回响应

**响应体：**

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiI...",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "nickname": "超级管理员",
      "avatar": null,
      "roles": ["admin"],
      "permissions": [
        "user:add",
        "user:edit",
        "user:delete",
        "user:disable",
        "role:add",
        "role:edit",
        "role:delete",
        "menu:add",
        "menu:edit",
        "menu:delete"
      ]
    }
  }
}
```

---

## 8.3 其他接口处理流程

### 8.3.1 获取用户信息

```
┌─────────────────────────────────────────────────────────────────┐
│                    获取用户信息流程                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Client                                                         │
│   │                                                            │
│   │  GET /api/current-user/info                                │
│   │  Authorization: Bearer eyJhbG...                           │
│   │──────────────────────▶                                     │
│   │                                                            │
│   │                               ┌─────────────────────┐      │
│   │                               │ JwtAuthFilter       │      │
│   │                               │                     │      │
│   │                               │ 1. 提取 Token       │      │
│   │                               │ 2. 验证 Token       │      │
│   │                               │ 3. 检查 Redis       │      │
│   │                               │ 4. 加载用户信息      │      │
│   │                               │ 5. 设置认证信息      │      │
│   │                               └──────────┬──────────┘      │
│   │                                          │                 │
│   │                                          ▼                 │
│   │                               ┌─────────────────────┐      │
│   │                               │ CurrentUserCtrl     │      │
│   │                               │                     │      │
│   │                               │ 1. 获取当前用户名    │      │
│   │                               │ 2. 查询用户信息      │      │
│   │                               │ 3. 查询角色列表      │      │
│   │                               │ 4. 查询权限列表      │      │
│   │                               └──────────┬──────────┘      │
│   │                                          │                 │
│   │  Response: {userInfo}                    │                 │
│   │◀──────────────────────│                 │                 │
│   │                                                            │
└─────────────────────────────────────────────────────────────────┘
```

### 8.3.2 需要认证的接口

```
┌─────────────────────────────────────────────────────────────────┐
│                    需要认证的接口                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. GET /api/user/page                                          │
│  2. GET /api/user/{id}                                          │
│  3. POST /api/user                                              │
│  4. PUT /api/user/{id}                                          │
│  5. DELETE /api/user/{id}                                       │
│  6. GET /api/role/page                                          │
│  7. GET /api/role/{id}                                          │
│  8. POST /api/role                                              │
│  9. PUT /api/role/{id}                                          │
│  10. DELETE /api/role/{id}                                      │
│  11. GET /api/menu/tree                                         │
│  12. GET /api/menu/{id}                                         │
│  13. POST /api/menu                                             │
│  14. PUT /api/menu/{id}                                         │
│  15. DELETE /api/menu/{id}                                      │
│  16. GET /api/current-user/info                                 │
│  17. GET /api/current-user/menus                                │
│  18. POST /api/auth/logout                                      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 8.3.3 无需认证的接口

```
┌─────────────────────────────────────────────────────────────────┐
│                    无需认证的接口                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. POST /api/auth/login                                        │
│  2. POST /api/auth/register                                     │
│  3. OPTIONS /** (所有 OPTIONS 请求)                              │
│  4. /doc.html (Knife4j UI)                                     │
│  5. /webjars/** (Knife4j 静态资源)                              │
│  6. /swagger-resources/** (Swagger 资源)                        │
│  7. /v3/api-docs/** (OpenAPI 文档)                              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8.4 登出流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    登出流程                                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Client                  Server                                │
│   │                       │                                    │
│   │  POST /api/auth/logout│                                    │
│   │  Authorization: Bearer│                                    │
│   │──────────────────────▶│                                    │
│   │                       │                                    │
│   │                       │  1. JwtAuthFilter                  │
│   │                       │     ├── 提取 Token                 │
│   │                       │     ├── 验证 Token                 │
│   │                       │     ├── 检查 Redis                 │
│   │                       │     └── 设置认证信息                │
│   │                       │                                    │
│   │                       │  2. AuthController.logout()        │
│   │                       │     └── UserServiceImpl.logout()   │
│   │                       │         ├── 删除 Redis Token       │
│   │                       │         │   .delete("token:xxx")   │
│   │                       │         └── 清除 SecurityContext   │
│   │                       │             .clearContext()        │
│   │                       │                                    │
│   │  Response: {          │                                    │
│   │    "code": 200,       │                                    │
│   │    "message": "登出成功"│                                   │
│   │  }                    │                                    │
│   │◀──────────────────────│                                    │
│   │                       │                                    │
└─────────────────────────────────────────────────────────────────┘
```

**登出处理代码：**

```java
@Override
public void logout(String token) {
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
    }
    // 从Redis删除token
    redisTemplate.delete("token:" + token);
    // 清除SecurityContext
    SecurityContextHolder.clearContext();
}
```

---

## 8.5 注册流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    注册流程                                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Client                  Server                                │
│   │                       │                                    │
│   │  POST /api/auth/register                                  │
│   │  {username, password,  │                                    │
│   │   nickname, email, phone}                                  │
│   │──────────────────────▶│                                    │
│   │                       │                                    │
│   │                       │  1. AuthController.register()      │
│   │                       │     └── UserServiceImpl.register() │
│   │                       │         ├── 检查用户名是否已存在    │
│   │                       │         │   UserMapper.select...   │
│   │                       │         ├── 创建用户               │
│   │                       │         │   user.setPassword(      │
│   │                       │         │     passwordEncoder      │
│   │                       │         │       .encode(password)) │
│   │                       │         │   UserMapper.insert()    │
│   │                       │         └── 分配默认角色            │
│   │                       │             UserRoleMapper.insert()│
│   │                       │                                    │
│   │  Response: {          │                                    │
│   │    "code": 200,       │                                    │
│   │    "message": "注册成功"│                                   │
│   │  }                    │                                    │
│   │◀──────────────────────│                                    │
│   │                       │                                    │
└─────────────────────────────────────────────────────────────────┘
```

**注册处理代码：**

```java
@Override
@Transactional
public void register(RegisterRequest request) {
    // 检查用户名是否已存在
    User existingUser = userMapper.selectByUsername(request.getUsername());
    if (existingUser != null) {
        throw new RuntimeException("用户名已存在");
    }
    
    // 创建用户
    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setNickname(request.getNickname());
    user.setEmail(request.getEmail());
    user.setPhone(request.getPhone());
    user.setStatus(Constants.STATUS_ENABLED);
    user.setCreateBy("system");
    userMapper.insert(user);
    
    // 分配默认角色（普通用户）
    UserRole userRole = new UserRole();
    userRole.setUserId(user.getId());
    userRole.setRoleId(2L); // 普通用户角色ID
    userRoleMapper.insert(userRole);
}
```

---

## 8.6 异常处理

### 8.6.1 认证失败处理

```java
// AuthController 中的异常处理
@PostMapping("/login")
public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
        LoginResponse response = userService.login(request);
        return Result.success("登录成功", response);
    } catch (Exception e) {
        return Result.error("登录失败: " + e.getMessage());
    }
}
```

### 8.6.2 常见异常类型

```
┌─────────────────────────────────────────────────────────────────┐
│                    常见异常类型                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. UsernameNotFoundException                                   │
│     └── 用户不存在                                               │
│                                                                 │
│  2. BadCredentialsException                                     │
│     └── 密码错误                                                 │
│                                                                 │
│  3. DisabledException                                           │
│     └── 用户被禁用                                               │
│                                                                 │
│  4. LockedException                                              │
│     └── 用户被锁定                                               │
│                                                                 │
│  5. ExpiredJwtException                                         │
│     └── Token 已过期                                             │
│                                                                 │
│  6. MalformedJwtException                                       │
│     └── Token 格式错误                                           │
│                                                                 │
│  7. SignatureException                                           │
│     └── Token 签名错误                                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8.7 请求处理流程总结

```
┌─────────────────────────────────────────────────────────────────┐
│                    请求处理流程总结                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 客户端发送请求                                                │
│     └── HTTP Request (GET/POST/PUT/DELETE)                      │
│                                                                 │
│  2. CorsFilter 处理跨域                                          │
│     └── 添加 CORS 响应头                                         │
│                                                                 │
│  3. SecurityFilterChain 处理安全                                  │
│     ├── 检查 URL 是否需要认证                                     │
│     └── 放行或拒绝                                               │
│                                                                 │
│  4. JwtAuthenticationFilter 处理 JWT                             │
│     ├── 提取 Token                                               │
│     ├── 验证 Token                                               │
│     └── 设置认证信息                                              │
│                                                                 │
│  5. DispatcherServlet 分发请求                                    │
│     └── 根据 URL 找到对应的 Handler                              │
│                                                                 │
│  6. Controller 处理请求                                          │
│     ├── 调用 Service 层                                          │
│     └── 返回 Result 响应                                         │
│                                                                 │
│  7. Service 层处理业务逻辑                                       │
│     ├── 调用 Mapper 层                                           │
│     └── 处理数据                                                 │
│                                                                 │
│  8. Mapper 层访问数据库                                          │
│     └── 执行 SQL 语句                                            │
│                                                                 │
│  9. 返回响应                                                     │
│     └── JSON 格式的 Result 对象                                  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8.8 常见问题

### Q1: 登录时提示"用户名或密码错误"怎么办？

**可能原因：**
1. 用户名或密码输入错误
2. 密码未正确加密存储
3. 数据库中用户数据不正确

**排查方法：**
```bash
# 检查数据库中的用户数据
mysql -u root -p -e "SELECT * FROM rbac_db.users;"

# 检查密码是否正确加密
# 密码应该是 BCrypt 加密格式：$2a$10$...
```

### Q2: Token 验证失败怎么办？

**可能原因：**
1. Token 已过期
2. Token 签名错误
3. Token 不在 Redis 中

**排查方法：**
```bash
# 检查 Token 是否在 Redis 中
redis-cli GET "token:eyJhbG..."

# 检查 Token 过期时间
redis-cli TTL "token:eyJhbG..."

# 检查 JWT 配置
# application-dev.yml 中的 jwt.secret 和 jwt.expiration
```

### Q3: 如何扩展权限控制？

**方法一：** 使用 `@PreAuthorize` 注解

```java
@PreAuthorize("hasAuthority('user:add')")
@PostMapping("/user")
public Result<Void> createUser(@RequestBody UserCreateRequest request) {
    // ...
}
```

**方法二：** 使用 `@Secured` 注解

```java
@Secured("ROLE_ADMIN")
@GetMapping("/admin")
public Result<Void> adminOnly() {
    // ...
}
```

---

## 8.9 下一步

请求处理流程文档到此结束。如需了解更多：

- 查看完整启动时序图：[Phase 7：完整启动时序图](./Phase7-完整启动时序图.md)
- 查看项目 README：`README.md`
