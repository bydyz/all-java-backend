# @MapperScan 注解详解

## 1. 来源

`@MapperScan` 是 **MyBatis-Spring** 模块提供的注解，属于 `org.mybatis.spring.annotation` 包。

> 需要引入依赖：`mybatis-spring`（Spring 项目）或 `mybatis-spring-boot-starter`（Spring Boot 项目）

---

## 2. 版本历史

| 版本 | 说明 |
|------|------|
| **1.2.0** | 引入 `@MapperScan` 和 `<mybatis:scan/>`，替代手动配置 `MapperFactoryBean` |
| **2.0.2** | 支持 `lazy-init` 懒加载特性（默认 `false`） |
| **2.0.4** | 若未指定 `basePackages`，默认扫描声明该注解的类所在包 |
| **2.0.6** | 支持 `default-scope` 作用域配置（配合 Spring Cloud `@RefreshScope`） |
| **4.1.0** | 最新稳定版，支持 Jakarta EE，属性保持兼容 |

---

## 3. 注解属性与默认值

```java
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Import(MapperScannerRegistrar.class)
@Repeatable(MapperScans.class)
public @interface MapperScan {

    // 别名，等同于 basePackages
    @AliasFor("basePackages")
    String[] value() default {};

    // 要扫描的基础包路径
    @AliasFor("value")
    String[] basePackages() default {};

    // 指定基础包的类（包路径从类所在目录推导）
    Class<?>[] basePackageClasses() default {};

    // 扫描带有指定注解的接口（默认扫描所有接口）
    Class<? extends Annotation> annotationClass() default Annotation.class;

    // 仅扫描继承指定父接口的接口
    Class<?> markerInterface() default void.class;

    // 多数据源时指定 SqlSessionFactory 的 bean 名称
    String sqlSessionFactoryRef() default "";

    // 多数据源时指定 SqlSessionTemplate 的 bean 名称
    String sqlSessionTemplateRef() default "";

    // 自定义 Bean 名称生成器
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    // 指定自定义 MapperFactoryBean
    Class<? extends MapperFactoryBean> factoryBean() default MapperFactoryBean.class;

    // 作用域（2.0.6+）
    String defaultScope() default "";

    // 懒加载（2.0.2+）
    boolean lazyInitialization() default false;
}
```

### 关键默认值

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` / `basePackages` | `{}` | 空数组；2.0.4+ 会扫描注解声明类所在包 |
| `sqlSessionFactoryRef` | `""` | 空字符串，使用 Spring 自动注入的唯一 SqlSessionFactory |
| `annotationClass` | `Annotation.class` | 不过滤，扫描所有接口 |
| `markerInterface` | `void.class` | 不过滤 |
| `factoryBean` | `MapperFactoryBean.class` | 默认的代理工厂 |
| `lazyInitialization` | `false` | 不开启懒加载 |

---

## 4. 使用示例

### 4.1 基础用法（推荐）

```java
@Configuration
@MapperScan("com.example.mapper")
public class MyBatisConfig {
}
```

### 4.2 多包扫描

```java
@Configuration
@MapperScan({"com.example.mapper", "com.example.dao"})
public class MyBatisConfig {
}
```

### 4.3 多数据源场景

```java
@Configuration
@MapperScan(
    basePackages = "com.example.mapper.db1",
    sqlSessionFactoryRef = "db1SqlSessionFactory"
)
public class Db1Config {
}

@Configuration
@MapperScan(
    basePackages = "com.example.mapper.db2",
    sqlSessionFactoryRef = "db2SqlSessionFactory"
)
public class Db2Config {
}
```

### 4.4 带过滤条件

```java
// 只扫描带有 @MyMapper 注解的接口
@Configuration
@MapperScan(
    basePackages = "com.example.mapper",
    annotationClass = MyMapper.class
)
public class MyBatisConfig {
}

// 只扫描继承 BaseMapper 的接口
@Configuration
@MapperScan(
    basePackages = "com.example.mapper",
    markerInterface = BaseMapper.class
)
public class MyBatisConfig {
}
```

### 4.5 Spring Boot 简化写法

```java
@SpringBootApplication
@MapperScan("com.example.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 4.6 2.0.4+ 默认扫描当前包

```java
// 声明在 com.example.config 包下，则默认扫描 com.example.config
@Configuration
@MapperScan  // 不指定包路径，自动扫描当前包
public class MyBatisConfig {
}
```

---

## 5. @MapperScan vs @Mapper 对比

| 特性 | @Mapper | @MapperScan |
|------|---------|-------------|
| 作用范围 | 单个 Mapper 接口 | 批量扫描整个包 |
| 使用方式 | 加在每个 Mapper 接口上 | 加在配置类上一次即可 |
| 配置量 | 接口多时繁琐 | 零配置，自动扫描 |
| 推荐场景 | 接口少、需要精细控制 | 绝大多数场景（推荐） |

**最佳实践**：使用 `@MapperScan` 统一扫描，无需在每个 Mapper 接口上加 `@Mapper`。

---

## 6. 底层原理

1. `@MapperScan` 通过 `@Import(MapperScannerRegistrar.class)` 导入注册器
2. `MapperScannerRegistrar` 实现 `BeanDefinitionRegistryPostProcessor`
3. 扫描指定包路径下的所有接口
4. 为每个 Mapper 接口注册 `MapperFactoryBean`
5. `MapperFactoryBean` 通过 JDK 动态代理生成实现类，注册为 Spring Bean

---

## 7. 常见问题

### Q1: @MapperScan 不生效？

检查是否引入了正确依赖：
```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
</dependency>

<!-- 非 Spring Boot -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
</dependency>
```

### Q2: 多数据源时 Mapper 混用？

确保每个 `@MapperScan` 的 `basePackages` 不重叠，并指定对应的 `sqlSessionFactoryRef`。

### Q3: 懒加载有什么限制？

以下场景不支持懒加载：
- `<association>` / `<collection>` 引用其他 Mapper
- `<include>` 包含其他 Mapper 的片段
- `<cache-ref>` 引用其他 Mapper 的缓存
- `<select resultMap>` 引用其他 Mapper 的结果集

可通过 `@DependsOn` 解决部分依赖问题。

---

## 8. 参考资料

- [MyBatis-Spring 官方文档](https://mybatis.org/spring/zh_CN/mappers.html)
- [MapperScan API 文档](https://mybatis.org/spring/zh_CN/apidocs/org/mybatis/spring/annotation/MapperScan.html)
