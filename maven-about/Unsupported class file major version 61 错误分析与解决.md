# Unsupported class file major version 61 错误分析与解决

## 一、错误现象

运行 `SysRoleMapperTest.java` 中的 `add()` 测试方法时，Spring Boot 启动阶段抛出异常：

```
BeanDefinitionStoreException: Failed to read candidate component class:
  file [.../target/test-classes/com/rc/system/test/SysRoleMapperTest.class]
  nested exception is NestedIOException:
    ASM ClassReader failed to parse class file - probably due to a new Java class
    file version that isn't supported yet
  nested exception is
    java.lang.IllegalArgumentException: Unsupported class file major version 61
```

Spring 在进行组件扫描（`@SpringBootTest` 触发）时，使用 ASM 库读取 `.class` 文件，发现其字节码版本超出了 ASM 的支持范围，直接失败。

---

## 二、Java Class 文件版本号对照表

| Java 版本 | class file major version |
|-----------|------------------------|
| Java 8    | 52                     |
| Java 9    | 53                     |
| Java 10   | 54                     |
| Java 11   | 55                     |
| Java 12   | 56                     |
| Java 13   | 57                     |
| Java 14   | 58                     |
| Java 15   | 59                     |
| Java 16   | 60                     |
| Java 17   | **61**                 |
| Java 21   | 65                     |

错误中 `major version 61` 对应 **Java 17**。

---

## 三、根因分析

### 3.1 环境信息

| 项 | 值 |
|----|----|
| 系统 JDK | Java 17.0.10 |
| 项目 pom.xml 配置 | `<java.version>1.8</java.version>` |
| Spring Boot 版本 | 2.3.6.RELEASE |
| Spring Framework 版本 | 5.2.x（Spring Boot 2.3.6 管理） |
| 内嵌 ASM 版本 | **ASM 7.x**（支持到 Java 14，即 major version 58） |
| Lombok 版本 | ~1.18.12（Spring Boot 2.3.6 管理，不支持 Java 17） |

### 3.2 矛盾链条

```
┌─────────────────────────────────────────────────────────────┐
│  1. 系统安装的是 Java 17                                     │
│  2. 项目 pom.xml 声明 <java.version>1.8</java.version>      │
│  3. IDE（IntelliJ IDEA）的编译器设置使用 Java 17            │
│     → IDE 编译器设置覆盖了 Maven 的 source/target 配置      │
│  4. .class 文件被编译为 Java 17 字节码                       │
│     → major version = 61                                    │
│  5. Spring Boot 2.3.6 的 ASM 7.x 最高支持 Java 14 (58)     │
│  6. ASM 读取 version 61 的 class 文件 → 抛出异常            │
└─────────────────────────────────────────────────────────────┘
```

### 3.3 问题本质

| 层面 | 说明 |
|------|------|
| **编译器层面** | Java 17 编译器产出的 class 文件字节码版本为 61 |
| **ASM 层面** | ASM 7.x 只认识到 major version 58（Java 14） |
| **Spring 层面** | Spring 组件扫描依赖 ASM 解析 class，ASM 解析失败 → 启动失败 |

核心矛盾：**编译端产出高版本字节码，运行端 ASM 库不认识高版本字节码。**

---

## 四、解决方案

### 方案对比

| 方案 | 改动范围 | 风险 | 适用场景 |
|------|---------|------|---------|
| 方案 1：升级 Spring Boot | pom.xml | 高（需验证依赖兼容性） | 项目可接受大版本升级 |
| 方案 2：IDE 统一设置为 Java 8 | IDE 设置 | 低（仅改 IDE 配置） | 仅个人开发环境 |
| **方案 3：Maven 显式指定编译版本** | **pom.xml** | **低** | **推荐，团队统一生效** |

### 方案 3 详细实施（推荐）

#### 4.1 添加 maven-compiler-plugin

在父 pom.xml 的 `<build>` 中显式指定编译目标为 Java 8：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

> **为什么需要这一步？**
> `<java.version>` 只是 Spring Boot Parent 提供的一个属性，它通过 `maven-compiler-plugin` 的 `release` 参数生效。但当 IDE 直接使用 Java 17 编译器时，IDE 的编译器设置会覆盖 Maven 配置。显式配置 `<source>` 和 `<target>` 可以更强制地约束编译输出版本。

#### 4.2 升级 Lombok 版本

Spring Boot 2.3.6 管理的 Lombok 约为 1.18.12，该版本**不支持 Java 17 编译器**（会报 `IllegalAccessError`：Lombok 的注解处理器无法访问 `jdk.compiler` 模块内部 API）。

在父 pom.xml 中覆盖 Lombok 版本为 1.18.24（支持 Java 17）：

```xml
<properties>
    <lombok.version>1.18.24</lombok.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

#### 4.3 重新编译

```bash
mvn clean compile test-compile
```

#### 4.4 验证 class 文件版本

```bash
javap -verbose target/test-classes/com/rc/system/test/SysRoleMapperTest.class | findstr "major version"
```

输出应为：

```
major version: 52
```

52 = Java 8，ASM 7.x 可正常解析。

---

## 五、完整修改示例（父 pom.xml）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.6.RELEASE</version>
    </parent>

    <properties>
        <java.version>1.8</java.version>
        <lombok.version>1.18.24</lombok.version>
        <!-- 其他版本属性... -->
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- 覆盖 Spring Boot 管理的 Lombok 版本 -->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </dependency>
            <!-- 其他依赖... -->
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 六、补充说明

### 6.1 为什么 `<java.version>1.8</java.version>` 没有生效？

Spring Boot Parent 中通过 `maven-compiler-plugin` 的 `release` 参数读取 `${java.version}`，但在以下情况下会被 IDE 覆盖：

- IntelliJ IDEA 中 `Project Structure → Project → Language Level` 设为 17
- IntelliJ IDEA 中 `Settings → Build → Compiler → Java Compiler → Target bytecode version` 设为 17
- Maven 命令行编译时 `JAVA_HOME` 指向 Java 17，且未显式指定 compiler plugin 参数

### 6.2 ASM 版本与 Java 版本对应关系

| Spring Boot 版本 | Spring Framework | ASM 版本 | 最高支持 Java |
|-----------------|-----------------|---------|-------------|
| 2.3.x           | 5.2.x           | ASM 7.1 | Java 14     |
| 2.4.x           | 5.3.x           | ASM 9.0 | Java 16     |
| 2.5.x           | 5.3.x           | ASM 9.1 | Java 17     |
| 2.6.x / 2.7.x  | 5.3.x           | ASM 9.2+| Java 17+    |
| 3.0.x           | 6.0.x           | ASM 9.3+| Java 19+    |

### 6.3 Lombok 版本与 Java 版本兼容性

| Lombok 版本 | 最低支持 Java 编译器 | 说明 |
|------------|-------------------|------|
| 1.18.12    | Java 8 ~ 14      | Spring Boot 2.3.6 默认 |
| 1.18.20    | Java 8 ~ 16      | |
| 1.18.22    | Java 8 ~ 17      | Java 17 正式支持 |
| **1.18.24** | **Java 8 ~ 17**  | **推荐，稳定性好** |
| 1.18.30    | Java 8 ~ 21      | 最新稳定版 |

---

## 七、总结

```
问题：Unsupported class file major version 61

根因：
  Java 17 编译器 → 产出 version 61 的 class 文件
  Spring Boot 2.3.6 的 ASM 7.x → 最高识别 version 58
  ASM 无法解析 → Spring 启动失败

解决：
  1. maven-compiler-plugin 显式指定 source/target=1.8 → 约束字节码版本为 52
  2. 升级 Lombok 到 1.18.24 → 兼容 Java 17 编译器
  3. mvn clean compile test-compile → 重新编译验证
```
