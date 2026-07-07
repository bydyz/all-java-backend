# Java 编译的两个"类路径"（Classpath）是完全隔离的

Maven 编译时，Javac（Java编译器）内部维护了两个独立的路径表：

| 路径 | Maven 对应配置 | 加载时机 | 作用 |
|------|----------------|----------|------|
| **编译类路径**（Classpath） | `<dependencies>` 里的 jar | 编译启动时加载 | 解析源代码中的类型定义（如 `import lombok.Data;`） |
| **注解处理器路径**（Processor Path） | `<annotationProcessorPaths>` | 编译过程中启动 | 运行 Lombok 的代码生成逻辑（修改 AST、生成 getter/setter） |

---

## 为什么"删除依赖"会导致编译直接报错？

当你在 Java 文件中写下：

```java
import lombok.Data;  // ← 导入语句

@Data                // ← 注解使用
public class User {}
```

编译过程（Javac）的内部顺序是这样的：

1. **词法/语法分析阶段**：Javac 读取 .java 文件，遇到 `import lombok.Data;`
2. **符号解析阶段**：Javac 必须在编译类路径（`-classpath`）上找到 `lombok.Data.class` 这个类文件。如果找不到，立即报 `package lombok does not exist`，编译直接终止
3. **注解处理阶段**：如果第 2 步通过，Javac 才会去执行 `annotationProcessorPaths` 中的 Lombok 处理器，让 Lombok 在内存中动态生成 getter/setter

> **关键点**：`@Data` 这个注解本身是有 `.class` 字节码文件的（它只是一个普通的 Java 接口/注解）。Javac 必须先"认识"这个注解的类型，才能去"处理"它。`annotationProcessorPaths` 只在第 3 步生效，无法替代第 2 步缺失的类定义。

---

## 为什么用 `<scope>provided</scope>` 而不用 `compile`？

既然不能删，那为什么不用默认的 `compile`，非得用 `provided`？

| Scope | 说明 | 适用场景 |
|-------|------|----------|
| `compile`（默认） | Lombok 的 jar 包会打入最终的 war/jar 包，并传递到依赖你的下游项目 | ❌ 不推荐 - 因为 Lombok 的注解是 `@Retention(SOURCE)`（源码级别），运行时完全不需要，传过去只会污染依赖树，甚至引发版本冲突 |
| `provided`（推荐） | 告诉 Maven "编译时需要，运行时由 JDK/容器提供，不要打包进去，也不要传递" | ✅ 推荐 - 既保证 Javac 能识别 `import lombok.Data`，又保证了你打出的包干净清爽 |

---

## 深度验证（你可以自己测试）

如果你真的删除 `<dependency>`，只保留 `<annotationProcessorPaths>`，执行 `mvn clean compile`，你会看到类似下面的编译错误：

```text
[ERROR] /path/to/User.java:[1,1] 程序包lombok不存在
[ERROR] /path/to/User.java:[3,1] 找不到符号
  符号:   类 Data
```

这就从底层证明了：**编译类路径和注解处理器路径缺一不可**。
