# Lombok 常用注解指南

Lombok 是一个 Java 库，通过注解自动生成样板代码（getter/setter/toString 等），减少代码量。

---

## 1. @Getter / @Setter

**作用**：自动生成字段的 getter 和 setter 方法。

```java
@Getter
@Setter
public class User {
    private String name;
    private int age;
}
// 等价于手写 getName(), setName(), getAge(), setAge()
```

**特点**：
- 可加在类上（所有字段）
- 可加在单个字段上（仅该字段）
- `@Getter(AccessLevel.NONE)` 可禁用某个字段的 getter

---

## 2. @ToString

**作用**：自动生成 `toString()` 方法。

```java
@ToString
public class User {
    private String name;
    private int age;
}
// 输出: User(name=张三, age=25)
```

**常用属性**：
- `@ToString(onlyExplicitlyIncluded = true)` - 只包含 `@Include` 标注的字段
- `@ToString(exclude = {"password"})` - 排除指定字段
- `@ToString(of = {"name"})` - 只包含指定字段

---

## 3. @EqualsAndHashCode

**作用**：自动生成 `equals()` 和 `hashCode()` 方法。

```java
@EqualsAndHashCode
public class User {
    private String name;
    private int age;
}
```

**常用属性**：
- `@EqualsAndHashCode(of = {"id"})` - 只用 id 字段比较
- `@EqualsAndHashCode(exclude = {"password"})` - 排除字段

---

## 4. @Data

**作用**：组合注解，相当于 `@Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor`。

```java
@Data
public class User {
    private String name;
    private int age;
}
// 最常用的注解，简化 POJO 类
```

---

## 5. @Value

**作用**：不可变版本的 `@Data`，类为 final，字段为 private final。

```java
@Value
public class Config {
    String host;
    int port;
}
// 等价于: public final class Config { private final String host; ... }
// 自动生成 getter，没有 setter（不可变）
```

---

## 6. @Builder

**作用**：生成 Builder 模式代码，支持链式构建对象。

```java
@Builder
public class User {
    private String name;
    private int age;
}

// 使用方式
User user = User.builder()
    .name("张三")
    .age(25)
    .build();
```

**配合 @Data 使用**：
```java
@Data
@Builder
public class User {
    private String name;
    private int age;
}
```

---

## 7. @RequiredArgsConstructor

**作用**：为所有 final 字段和 @NonNull 字段生成构造函数。

```java
@Data
@RequiredArgsConstructor
public class User {
    private final String id;      // final 字段必传
    private String name;          // 非 final，可选
    @NonNull private String email; // @NonNull，必传
}

// 生成构造函数: User(String id, String email)
```

---

## 8. @NoArgsConstructor / @AllArgsConstructor

```java
@Data
@NoArgsConstructor    // 无参构造
@AllArgsConstructor   // 全参构造
public class User {
    private String name;
    private int age;
}
```

---

## 9. @Slf4j / @Log4j2

**作用**：自动生成日志对象。

```java
@Slf4j
public class UserService {
    public void doSomething() {
        log.info("开始处理...");
        log.error("发生错误", e);
    }
}
// 自动生成: private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
```

---

## 10. @SneakyThrows

**作用**：自动处理受检异常，无需 try-catch 或 throws 声明。

```java
@SneakyThrows
public void readFile(String path) {
    // 即使 FileNotFoundException 是受检异常，也不需要 try-catch
    new FileReader(path);
}
```

---

## 11. @Synchronized

**作用**：自动生成同步块或同步方法。

```java
@Synchronized
public void doSomething() {
    // 整个方法体被同步
}

@Synchronized("lock")
public void doOther() {
    // 使用指定锁对象 "lock"
}
```

---

## 12. @Cleanup

**作用**：自动关闭资源，相当于 try-with-resources。

```java
@Cleanup
InputStream is = new FileInputStream("file.txt");
// 方法结束时自动调用 is.close()
```

---

## 13. @Accessors

**作用**：修改 getter/setter 的生成方式。

```java
@Getter
@Setter
@Accessors(fluent = true)  // 链式调用风格
public class User {
    private String name;
}

// 使用: user.name("张三").age(25) 而不是 user.setName("张三")
```

**常用属性**：
- `fluent = true` - getter/setter 无 get/set 前缀
- `chain = true` - setter 返回 this，支持链式调用
- `prefix = {"_"}` - 字段名有前缀时去掉

---

## 14. @FieldDefaults / @FieldNaming

```java
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@FieldNaming(PrefixUnderscoreFieldNamingConvention.class)
public class User {
    String _name;  // 字段名自动去掉下划线前缀
}
```

---

## 快速选择指南

| 场景 | 推荐注解 |
|------|----------|
| 普通 POJO/实体类 | `@Data` |
| 不可变对象 | `@Value` |
| 需要 Builder 模式 | `@Builder` + `@Data` |
| REST 请求/响应体 | `@Data` + `@Builder` + `@NoArgsConstructor` |
| 配置类 | `@Value` 或 `@Data` + `@RequiredArgsConstructor` |
| 日志工具类 | `@Slf4j` |
