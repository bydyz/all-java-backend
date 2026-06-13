# Maven packaging 标签详解

## 概述

`packaging` 标签用于定义 Maven 项目的打包类型，决定了构建产物是什么以及构建过程中的行为。每个 Maven 项目必须指定 packaging，或者使用默认值。

## 默认值

- **默认值**：`jar`
- 如果 pom.xml 中未明确指定 `packaging` 标签，Maven 默认使用 `jar` 作为打包类型

## 可取值与详解

### 1. jar（Java Archive）

**定义**：将项目构建为标准的 JAR 文件

**作用**：
- 生成包含所有编译后的 class 文件、资源文件和依赖的 JAR 包
- 可以通过 Maven 自带的 `maven-jar-plugin` 进行配置
- 默认主清单类为 `META-INF/MANIFEST.MF` 中定义的 Main-Class

**实际用途**：
- 纯 Java 库项目
- 可被其他项目依赖的组件
- 微服务或独立应用程序
- 示例：
```xml
<project>
    <packaging>jar</packaging>
</project>
```

### 2. war（Web Archive）

**定义**：将项目构建为 WAR（Web Application Archive）文件

**作用**：
- 生成包含所有编译后的 class 文件、资源文件、webapp 内容和依赖的 WAR 包
- 构建过程中会自动包含 webapp 目录（通常在 `src/main/webapp`）
- 可配置 `maven-war-plugin` 进行定制
- 默认部署到 Servlet 容器（Tomcat、Jetty 等）

**实际用途**：
- Web 应用程序
- SaaS 服务
- 企业级 Web 项目
- 示例：
```xml
<project>
    <packaging>war</packaging>
</project>
```

### 3. pom

**定义**：项目本身就是父 POM，不生成任何构建产物

**作用**：
- 仅包含项目配置信息（依赖管理、模块定义等）
- 不执行编译、测试、打包等构建过程
- 用于多模块项目中的聚合器和父 POM

**实际用途**：
- 多模块项目中的聚合器
- 定义全局依赖版本
- 配置插件行为
- 示例：
```xml
<project>
    <packaging>pom</packaging>
</project>
```

### 4. ear（Enterprise Archive）

**定义**：将项目构建为 EAR（Enterprise Archive）文件

**作用**：
- 生成包含所有依赖模块（JAR/WAR）和配置文件的 EAR 包
- 用于企业级 Java EE/Java EE 应用程序
- 自动将依赖模块打包到 EAR 内部
- 可配置 `maven-ear-plugin` 进行定制

**实际用途**：
- 企业级分布式应用
- 需要多个模块协同的复杂应用
- EJB 项目
- 示例：
```xml
<project>
    <packaging>ear</packaging>
</project>
```

### 5. maven-plugin

**定义**：将项目构建为 Maven 插件

**作用**：
- 构建结果是一个 JAR，但标记为 Maven 插件
- 包含 Mojo（Maven Plain Old Java Object）类
- 用于扩展 Maven 功能

**实际用途**：
- 自定义 Maven 插件
- 创建构建工具
- 扩展 Maven 原生能力
- 示例：
```xml
<project>
    <packaging>maven-plugin</packaging>
</project>
```

### 6. apk（Android Package）

**定义**：将项目构建为 Android APK 文件

**作用**：
- 生成 Android 安装包
- 需要配合 Android Maven 插件使用

**实际用途**：
- Android 应用开发
- 移动应用程序
- 示例：
```xml
<project>
    <packaging>apk</packaging>
</project>
```

### 7. aar（Android Archive）

**定义**：将项目构建为 Android AAR 文件

**作用**：
- 生成 Android 库包
- 包含编译后的 class 文件、资源文件和清单文件

**实际用途**：
- Android 库项目
- 可被其他 Android 应用依赖的组件
- 示例：
```xml
<project>
    <packaging>aar</packaging>
</project>
```

### 8. zip、tar、tar.gz、zip 等

**定义**：将项目构建为压缩包

**作用**：
- 生成指定格式的压缩文件
- 通常用于分发源代码或资源包

**实际用途**：
- 源代码分发
- 资源包分发
- 示例：
```xml
<project>
    <packaging>zip</packaging>
</project>
```

## 其他特殊值

### 源代码包

- **packaging**：`zip`、`tar.gz` 等
- **用途**：打包项目源代码，无需编译

### 自动构建支持

某些 packaging 类型会触发自动构建阶段：

| packaging | 自动执行阶段 | 说明 |
|-----------|-------------|------|
| jar       | package     | 打包 JAR |
| war       | package     | 打包 WAR |
| ear       | package     | 打包 EAR |
| pom       | 无          | 不执行任何构建 |
| maven-plugin | package | 打包插件 JAR |

## 配置示例

### 基础配置
```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>my-project</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging> <!-- 指定打包类型 -->
</project>
```

### 多模块项目中的 packaging 使用
```xml
<project>
    <groupId>com.example</groupId>
    <artifactId>parent</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging> <!-- 父模块使用 pom -->
    
    <modules>
        <module>module1</module>
        <module>module2</module>
    </modules>
</project>
```

## 常见场景选择

1. **纯 Java 库**
   - 选择：`jar`
   - 场景：可被其他项目依赖的组件

2. **Web 应用**
   - 选择：`war`
   - 场景：部署到 Tomcat/Jetty 等容器

3. **企业级应用**
   - 选择：`ear`
   - 场景：需要多个模块、EJB、JMS 等企业特性

4. **Maven 插件开发**
   - 选择：`maven-plugin`
   - 场景：扩展 Maven 功能

5. **Android 应用**
   - 选择：`apk` 或 `aar`
   - 场景：Android 开发

6. **多模块聚合**
   - 选择：`pom`
   - 场景：聚合子模块，不产生构建产物

## 注意事项

1. **必须字段**：packaging 是必须的，但可以省略（默认为 jar）

2. **一致性**：packaging 与项目类型应保持一致，避免混淆

3. **插件影响**：不同 packaging 类型会使用不同的插件处理，确保选择正确的插件

4. **继承关系**：子模块可以继承父模块的 packaging 类型，也可以覆盖

5. **打包路径**：不同 packaging 生成的文件在 `target/` 目录中的位置不同

## 参考资源

- [Maven Packaging 官方文档](https://maven.apache.org/pom.html#Packaging)
- [Maven Build Lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
