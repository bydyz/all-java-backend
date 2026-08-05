# Phase 5：Security 过滤器链配置阶段

## 概述

Phase 5 是 Spring Boot 启动流程的第五个阶段，主要完成 Spring Security 过滤器链的配置。包括 CORS 跨域配置、Security 过滤器链配置、JWT 认证过滤器配置等。

---

## 5.1 CORS 跨域配置

### 5.1.1 CorsConfig 配置

`CorsConfig.java:12-33`

```java
@Configuration
public class CorsConfig {
    
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
}
```

### 5.1.2 CORS 配置参数

| 参数 | 值 | 说明 |
|------|-----|------|
| `addAllowedOriginPattern("*")` | * | 允许所有来源 |
| `addAllowedHeader("*")` | * | 允许所有请求头 |
| `addAllowedMethod("*")` | * | 允许所有 HTTP 方法 |
| `setAllowCredentials(true)` | true | 允许携带凭证（Cookie、Authorization 等） |
| `setMaxAge(3600L)` | 3600秒 | 预检请求有效期 |

### 5.1.3 CORS 处理流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    CORS 处理流程                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 浏览器发送预检请求 (OPTIONS)                                  │
│     ├── Origin: http://localhost:3000                           │
│     ├── Access-Control-Request-Method: POST                     │
│     └── Access-Control-Request-Headers: Authorization           │
│                                                                 │
│  2. CorsFilter 处理预检请求                                      │
│     ├── 检查 Origin 是否允许                                     │
│     ├── 检查 Method 是否允许                                     │
│     └── 检查 Headers 是否允许                                    │
│                                                                 │
│  3. 返回 CORS 响应头                                             │
│     ├── Access-Control-Allow-Origin: http://localhost:3000      │
│     ├── Access-Control-Allow-Methods: GET, POST, PUT, DELETE    │
│     ├── Access-Control-Allow-Headers: *                         │
│     ├── Access-Control-Allow-Credentials: true                  │
│     └── Access-Control-Max-Age: 3600                            │
│                                                                 │
│  4. 浏览器发送实际请求                                            │
│     └── POST /api/auth/login                                    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5.2 Security 过滤器链配置

### 5.2.1 SecurityFilterChain 配置

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

### 5.2.2 配置项说明

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `csrf(AbstractHttpConfigurer::disable)` | 禁用 | REST API 不需要 CSRF 保护 |
| `sessionManagement(STATELESS)` | 无状态 | 不创建 HTTP Session |
| `requestMatchers(OPTIONS, "/**").permitAll()` | 允许 | 放行所有 OPTIONS 预检请求 |
| `requestMatchers("/api/auth/login").permitAll()` | 允许 | 登录接口无需认证 |
| `requestMatchers("/api/auth/register").permitAll()` | 允许 | 注册接口无需认证 |
| `requestMatchers("/doc.html").permitAll()` | 允许 | Knife4j 文档无需认证 |
| `anyRequest().authenticated()` | 需认证 | 其他所有请求需要认证 |
| `addFilterBefore(jwtFilter, UsernamePasswordAuthFilter.class)` | 添加 | 在用户名密码认证过滤器之前添加 JWT 过滤器 |

### 5.2.3 Security 过滤器链

```
┌─────────────────────────────────────────────────────────────────┐
│                    Security 过滤器链                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  HTTP Request                                                   │
│       │                                                         │
│       ▼                                                         │
│  ┌──────────────────────────────────────────┐                  │
│  │ 1. CorsFilter (CorsConfig.java:16)      │                  │
│  │    - 允许所有来源                         │                  │
│  │    - 允许所有方法                         │                  │
│  └──────────────────────────────────────────┘                  │
│       │                                                         │
│       ▼                                                         │
│  ┌──────────────────────────────────────────┐                  │
│  │ 2. SecurityFilterChain                  │                  │
│  │    ├── CSRF: disabled                   │                  │
│  │    ├── Session: STATELESS               │                  │
│  │    ├── Public URLs:                      │                  │
│  │    │   ├── /api/auth/login              │                  │
│  │    │   ├── /api/auth/register           │                  │
│  │    │   ├── /doc.html                    │                  │
│  │    │   ├── /webjars/**                  │                  │
│  │    │   ├── /swagger-resources/**        │                  │
│  │    │   └── /v3/api-docs/**              │                  │
│  │    └── All other: authenticated         │                  │
│  └──────────────────────────────────────────┘                  │
│       │                                                         │
│       ▼                                                         │
│  ┌──────────────────────────────────────────┐                  │
│  │ 3. JwtAuthenticationFilter              │                  │
│  │    (在 UsernamePasswordAuthFilter 之前)  │                  │
│  │    (JwtAuthenticationFilter.java:34)     │                  │
│  └──────────────────────────────────────────┘                  │
│       │                                                         │
│       ▼                                                         │
│  ┌──────────────────────────────────────────┐                  │
│  │ 4. UsernamePasswordAuthenticationFilter  │                  │
│  │    (Spring Security 默认)                │                  │
│  └──────────────────────────────────────────┘                  │
│       │                                                         │
│       ▼                                                         │
│  Controller / Resource                                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5.3 JWT 认证过滤器

### 5.3.1 JwtAuthenticationFilter 配置

`JwtAuthenticationFilter.java:24-61`

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final StringRedisTemplate redisTemplate;
    
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
    
    /**
     * 从请求中获取 JWT Token
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

### 5.3.2 JWT 认证流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    JWT 认证流程                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 从请求头提取 Token                                           │
│     └── Authorization: Bearer eyJhbG...                         │
│                                                                 │
│  2. 验证 Token 有效性                                            │
│     ├── 签名验证                                                 │
│     ├── 过期时间验证                                             │
│     └── 格式验证                                                 │
│                                                                 │
│  3. 检查 Token 是否在 Redis 中                                   │
│     └── token:eyJhbG... → 1 (userId)                           │
│                                                                 │
│  4. 从 Token 中提取用户名                                        │
│     └── subject: admin                                          │
│                                                                 │
│  5. 加载用户详情                                                 │
│     └── UserDetailsService.loadUserByUsername("admin")          │
│                                                                 │
│  6. 创建认证对象                                                 │
│     └── UsernamePasswordAuthenticationToken                     │
│                                                                 │
│  7. 设置到 SecurityContext                                       │
│     └── SecurityContextHolder.getContext().setAuthentication()  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5.4 JWT Token 生成与验证

### 5.4.1 JwtTokenProvider 配置

`JwtTokenProvider.java:17-87`

```java
@Slf4j
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    /**
     * 生成 JWT Token
     */
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
    
    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.getSubject();
    }
    
    /**
     * 验证 Token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
    
    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

### 5.4.2 JWT Token 结构

```
┌─────────────────────────────────────────────────────────────────┐
│                    JWT Token 结构                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Header.Payload.Signature                                       │
│                                                                 │
│  Header:                                                        │
│  {                                                              │
│    "alg": "HS256",                                              │
│    "typ": "JWT"                                                 │
│  }                                                              │
│                                                                 │
│  Payload:                                                       │
│  {                                                              │
│    "sub": "admin",                                              │
│    "iat": 1691234567,                                           │
│    "exp": 1691320967                                            │
│  }                                                              │
│                                                                 │
│  Signature:                                                     │
│  HMACSHA256(base64UrlEncode(header) + "." +                     │
│             base64UrlEncode(payload), secret)                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 5.4.3 JWT 配置参数

`application-dev.yml:32-36`

```yaml
jwt:
  secret: rbac-secret-key-must-be-at-least-256-bits-long-for-hs256
  expiration: 86400000  # 24小时 (毫秒)
  header: Authorization
  prefix: "Bearer "
```

| 参数 | 值 | 说明 |
|------|-----|------|
| `secret` | rbac-secret-key-... | 签名密钥（至少 256 位） |
| `expiration` | 86400000 | 过期时间（24小时） |
| `header` | Authorization | Token 请求头 |
| `prefix` | "Bearer " | Token 前缀 |

---

## 5.5 用户认证服务

### 5.5.1 UserDetailsServiceImpl

`UserDetailsServiceImpl.java:18-47`

```java
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserMapper userMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        // 获取用户权限
        List<String> permissions = userMapper.selectPermissionsByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getStatus() == 1, // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
}
```

### 5.5.2 用户加载流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    用户加载流程                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 接收用户名                                                  │
│     └── username = "admin"                                      │
│                                                                 │
│  2. 查询用户表                                                   │
│     └── UserMapper.selectByUsername("admin")                    │
│                                                                 │
│  3. 查询用户权限                                                 │
│     └── UserMapper.selectPermissionsByUserId(1)                 │
│                                                                 │
│  4. 构建权限列表                                                 │
│     ├── user:add                                                │
│     ├── user:edit                                               │
│     ├── user:delete                                             │
│     └── ...                                                     │
│                                                                 │
│  5. 构建 UserDetails 对象                                        │
│     ├── username: admin                                         │
│     ├── password: $2a$10$... (BCrypt 加密)                      │
│     ├── enabled: true                                           │
│     └── authorities: [user:add, user:edit, ...]                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5.6 认证管理器配置

`SecurityConfig.java:67-70`

```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
}
```

### 5.6.1 AuthenticationManager 工作流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    AuthenticationManager 工作流程                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 接收认证请求                                                 │
│     └── UsernamePasswordAuthenticationToken                      │
│                                                                 │
│  2. 遍历 AuthenticationProvider 列表                             │
│     └──DaoAuthenticationProvider                                │
│                                                                 │
│  3. 调用 UserDetailsService.loadUserByUsername()                 │
│     └── UserDetailsServiceImpl                                  │
│                                                                 │
│  4. 验证密码                                                     │
│     └── PasswordEncoder.matches(rawPassword, encodedPassword)   │
│                                                                 │
│  5. 返回认证结果                                                 │
│     └── Authentication (包含 UserDetails 和 Authorities)         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5.7 密码编码器配置

`SecurityConfig.java:59-62`

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### 5.7.1 BCrypt 加密

```
┌─────────────────────────────────────────────────────────────────┐
│                    BCrypt 密码加密                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  原始密码: admin123                                              │
│                                                                 │
│  加密后: $2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2│
│                                                                 │
│  验证:                                                          │
│  └── BCryptPasswordEncoder.matches("admin123", encodedPassword) │
│      └── 返回 true                                              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5.8 Security 配置时序图

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 5 Security 配置时序图                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ApplicationContext        SecurityConfig         HttpSecurity  │
│        │                       │                      │         │
│        │  配置 Security        │                      │         │
│        │──────────────────────▶│                      │         │
│        │                       │                      │         │
│        │                       │  1. 禁用 CSRF        │         │
│        │                       │─────────────────────▶│         │
│        │                       │                      │         │
│        │                       │  2. 配置无状态会话    │         │
│        │                       │─────────────────────▶│         │
│        │                       │                      │         │
│        │                       │  3. 配置公开 URL      │         │
│        │                       │─────────────────────▶│         │
│        │                       │                      │         │
│        │                       │  4. 添加 JWT 过滤器   │         │
│        │                       │─────────────────────▶│         │
│        │                       │                      │         │
│        │                       │  5. 构建过滤器链      │         │
│        │                       │─────────────────────▶│         │
│        │                       │                      │         │
│        │  Security 配置完成    │                      │         │
│        │◀──────────────────────│                      │         │
│        │                       │                      │         │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5.9 安全配置完成标志

当以下条件满足时，Security 过滤器链配置阶段完成：

- [x] CORS 配置完成
- [x] CSRF 禁用配置完成
- [x] 无状态会话配置完成
- [x] 公开 URL 配置完成
- [x] JWT 过滤器添加完成
- [x] 密码编码器配置完成
- [x] 认证管理器配置完成

**下一步：** 进入 Phase 6（Web 容器启动阶段），开始启动 Tomcat 并注册 Servlet。

---

## 5.10 常见问题

### Q1: CORS 配置不生效怎么办？

**可能原因：**
1. CorsFilter 未正确注册
2. 其他过滤器拦截了请求
3. 浏览器缓存了预检响应

**排查方法：**
```bash
# 检查响应头
curl -I -X OPTIONS http://localhost:8080/api/auth/login \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST"

# 清除浏览器缓存
# Chrome: chrome://settings/clearBrowserData
```

### Q2: JWT Token 验证失败怎么办？

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
```

### Q3: 如何添加新的公开 URL？

**修改 SecurityConfig：**

```java
.requestMatchers(
    "/api/auth/login",
    "/api/auth/register",
    "/api/public/**",  // 添加新的公开路径
    "/doc.html",
    "/webjars/**",
    "/swagger-resources/**",
    "/v3/api-docs/**"
).permitAll()
```

---

## 5.11 下一步

Security 过滤器链配置阶段完成后，Spring 将进入 Phase 6（Web 容器启动阶段），开始：
1. 初始化 Tomcat
2. 注册 DispatcherServlet
3. 注册 HandlerMapping
4. 启动服务

详见 [Phase 6：Web 容器启动阶段](./Phase6-Web容器启动阶段.md)
