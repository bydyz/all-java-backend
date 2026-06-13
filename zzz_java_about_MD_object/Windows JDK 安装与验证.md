# Windows JDK 安装与验证

## 一、下载 JDK

### 方式 1：Oracle JDK（推荐）
1. 访问 [Oracle JDK 下载页面](https://www.oracle.com/java/technologies/downloads/)
2. 选择 **Windows** 版本
3. 根据系统架构选择：
   - `x64 Installer`（64 位系统）
   - `x64 MSI Installer`（企业批量部署用 MSI 包）
4. 点击下载，需登录 Oracle 账号（免费注册即可）

### 方式 2：OpenJDK / Adoptium（开源免费）
1. 访问 [Adoptium Temurin 下载页](https://adoptium.net/temurin/releases/)
2. 选择操作系统 `Windows`、架构 `x64`
3. 下载 `msi` 或 `zip` 包

### 方式 3：Amazon Corretto（免费，长期支持）
1. 访问 [Amazon Corretto 下载](https://aws.amazon.com/cn/corretto/)
2. 选择对应 JDK 版本和 Windows 平台
3. 下载 MSI 安装包

---

## 二、安装 JDK

### 使用 MSI 安装（推荐）
1. 双击下载的 `.msi` 或 `.exe` 文件
2. 一路点击 **Next**
3. 可修改安装路径（建议路径中**不要包含空格和中文字符**）
   - 例如：`C:\Program Files\Java\jdk-17`
4. 点击 **Install** 等待安装完成
5. 点击 **Finish** 完成

### 使用 ZIP 解压
1. 解压下载的 `.zip` 到目标目录
   - 例如：`C:\Java\jdk-17`
2. 后续需手动配置环境变量（见下文）

---

## 三、配置环境变量

### 打开环境变量设置
1. 按 `Win + R`，输入 `sysdm.cpl` 回车
2. 切换到 **高级** 选项卡，点击 **环境变量**
3. 在 **系统变量** 区域操作

### 新建 JAVA_HOME
1. 点击 **新建**
2. 变量名：`JAVA_HOME`
3. 变量值：JDK 安装路径，例如 `C:\Program Files\Java\jdk-17`
4. 点击 **确定**

### 编辑 Path 变量
1. 在系统变量列表中找到 `Path`，双击
2. 点击 **新建**，添加：
   ```
   %JAVA_HOME%\bin
   ```
3. 点击 **确定** 保存

### 配置说明
| 变量名 | 示例值 | 作用 |
|--------|--------|------|
| `JAVA_HOME` | `C:\Program Files\Java\jdk-17` | 供其他 Java 工具（Maven、Gradle、Tomcat 等）引用 |
| `Path` | `%JAVA_HOME%\bin` | 使 `java`、`javac` 命令可在任意目录下使用 |

> 注：若安装了多个 JDK，可通过修改 `JAVA_HOME` 快捷切换版本。

---

## 四、验证安装

### 验证 Java 运行时
```cmd
java -version
```

**正确输出示例**（JDK 17）：
```
java version "17.0.9" 2023-10-17 LTS
Java(TM) SE Runtime Environment (build 17.0.9+11-LTS-201)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.9+11-LTS-201, mixed mode, sharing)
```

### 验证 Java 编译器
```cmd
javac -version
```

**正确输出示例**：
```
javac 17.0.9
```

### 常见错误及解决

| 错误信息 | 原因 | 解决 |
|----------|------|------|
| `'java' 不是内部或外部命令` | `Path` 未配置或未生效 | 检查 `%JAVA_HOME%\bin` 是否在 `Path` 中，重新打开 **新** CMD 窗口 |
| `java -version` 显示的版本与安装的不符 | 系统中有多个 JDK，`Path` 中旧版本路径优先级更高 | 在 `Path` 中将 `%JAVA_HOME%\bin` **上移**，或删除其他 JDK 的路径 |
| `Error: opening registry key 'Software\JavaSoft\JRE'` | JDK 注册表损坏或不完整 | 卸载后重新安装，或使用 ZIP 版手动配置 |

---

## 五、完整验证脚本

新建文件 `TestJava.java`：

```java
public class TestJava {
    public static void main(String[] args) {
        System.out.println("JDK 安装成功！");
        System.out.println("Java 版本: " + System.getProperty("java.version"));
    }
}
```

编译并运行：

```cmd
javac TestJava.java
java TestJava
```

**预期输出**：
```
JDK 安装成功！
Java 版本: 17.0.9
```

---

## 六、卸载 JDK

1. **控制面板卸载**（MSI 安装）：
   - `控制面板` → `程序和功能` → 找到对应 JDK → 右键卸载

2. **手动清理**（ZIP 解压或残留文件）：
   - 删除 JDK 安装目录
   - 删除环境变量 `JAVA_HOME`
   - 在 `Path` 中移除 `%JAVA_HOME%\bin`

---

## 七、推荐版本

| 版本 | 说明 | 支持状态 |
|------|------|----------|
| **JDK 17** (LTS) | 当前主流 LTS，推荐生产环境使用 | 长期支持 |
| **JDK 21** (LTS) | 最新 LTS，含虚拟线程等新特性 | 长期支持 |
| **JDK 11** (LTS) | 较老 LTS，部分遗留项目使用 | 长期支持（已过时） |
| **JDK 8** (LTS) | 极老版本，部分老旧项目 | 免费版本已停止更新 |

> 建议新项目统一使用 **JDK 17** 或 **JDK 21**。
