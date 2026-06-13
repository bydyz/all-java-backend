# Maven 命令详解

## 一、Maven 基础命令

### 1.1 命令结构说明

```bash
mvn [主命令] : [子命令] : [插件] : [目标]
```

- **mvn**: Maven 主命令
- **主命令**: 如 archetype、clean、compile 等
- **子命令**: 如 generate、package、install 等
- **插件**: 实现具体功能的插件
- **目标**: 插件的具体执行目标

示例：
```bash
mvn archetype:generate
```
- mvn: Maven 主命令
- archetype: 子命令
- generate: 目标

### 1.2 Maven 环境变量配置

配置环境变量后：
- 在任何位置都可以运行 `mvn -v` 查看 Maven 版本信息

重要提醒：
- **执行 Maven 构建操作时，必须在 pom.xml 所在的目录下运行**
- 否则会报错，提示找不到 pom.xml 文件

## 二、常用构建命令

### 2.1 生命周期相关命令

#### 清理命令

```bash
# 删除 target 目录
mvn clean

# 先清理再打包（清理 + package）
mvn clean package

# 先清理再安装
mvn clean install
```

#### 编译命令

```bash
# 编译主程序
mvn compile

# 编译测试程序
mvn test-compile

# 编译结果位置：
# - target/classes (主程序)
# - target/test-classes (测试程序)
```

#### 测试命令

```bash
# 执行测试（会先编译，再执行测试代码）
# 会调用 test 中所写的 Test方法，测试的报告存放的目录是 target/surefire-reports
# 由于 maven 生命周期的作用，执行 maven 的测试命令时，也会执行其前面的命令，  包含：处理主程序的资源、编译主程序、处理测试程序的资源、编译测试程序
mvn test

# 安装时跳过测试（常用于开发环境）
mvn install -Dmaven.test.skip=true

# 清理并安装，跳过测试
mvn clean install -Dmaven.test.skip=true
```

#### 打包命令

```bash
# 打包（会重新编译和测试）
# maven程序的打包命令，java程序会打成jar包，web程序会打成war包。存放的目录 target    包名是 artifactId + version + 后缀名
# 由于 maven 生命周期的作用，执行 maven 的打包命令时，也会执行其前面的命令，  包含：处理主程序的资源、编译主程序、处理测试程序的资源、编译测试程序、测试
mvn package

# 打包后安装到本地仓库
# maven程序的安装命令，安装的效果是将本地构建过程中生成的jar包存入 maven本地放库，路径则是根据项目的坐标信息生成的，除此之外还会有个同名的pom文件
# 由于 maven 生命周期的作用，执行 maven 的打包命令时，也会执行其前面的命令，  包含：处理主程序的资源、编译主程序、处理测试程序的资源、编译测试程序、测试、打包
mvn install

# 打包时跳过测试
mvn package -Dmaven.test.skip=true
```

```XML
<groupId>org.example</groupId>
<artifactId>test</artifactId>
<version>1.0-SNAPSHOT</version>

mvn install 会得到一下内容：
  1. com\example\test\1.0-SNAPSHOT\test-1.0-SNAPSHOT.jar
  2.com\example\test\1.0-SNAPSHOT\test-1.0-SNAPSHOT.pom   这个pom文件和项目中的pom文件一摸一样
```

### 2.2 生命周期阶段说明

Maven 生命周期按阶段执行：

1. **validate**: 验证项目结构是否正确
2. **compile**: 编译源代码
3. **test**: 运行测试
4. **package**: 打包（生成 jar/war）
5. **verify**: 验证包是否有效
6. **install**: 安装到本地仓库
7. **deploy**: 部署到远程仓库

**执行任何阶段命令会自动执行其前面的所有阶段**

示例：
```bash
mvn package  # 会依次执行 validate → compile → test → package
```

## 三、创建工程命令

### 3.1 创建 Java 工程

```bash
# 基础创建命令
mvn archetype:generate

# 指定参数创建 Java 工程
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.4 \
  -DgroupId=com.example \
  -DartifactId=my-project
```

### 3.2 创建 Web 工程

```bash
# 创建 Web 工程
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes \
  -DarchetypeArtifactId=maven-archetype-webapp \
  -DarchetypeVersion=1.4 \
  -DgroupId=com.example \
  -DartifactId=my-webapp
```

### 3.3 创建 Spring Boot 工程（推荐）

```bash
# 使用 Maven 生成 Spring Boot 项目
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-springboot-app \
  -DarchetypeGroupId=org.springframework.boot \
  -DarchetypeArtifactId=spring-boot-starter-parent \
  -DarchetypeVersion=2.7.0 \
  -DinteractiveMode=false
```

## 四、依赖管理命令

### 4.1 查看依赖

```bash
# 列出工程中所有使用的依赖
mvn dependency:list

# 以树形结构查看依赖及其传递依赖
mvn dependency:tree

# 查看依赖树并排除传递依赖
mvn dependency:tree -Dincludes=groupId:artifactId

# 查看指定依赖的信息
mvn dependency:list -DincludeScope=compile
```

### 4.2 依赖分析命令

```bash
# 分析依赖冲突
mvn dependency:tree -Dverbose

# 查看依赖列表详细信息
mvn dependency:list -DoutputFile=dependencies.txt
```

## 五、常用参数说明

### 5.1 通用参数

| 参数 | 说明 |
|------|------|
| `-Dmaven.test.skip=true` | 跳过测试执行 |
| `-Dmaven.test.skip=false` | 强制执行测试（即使失败） |
| `-X` | 输出调试信息 |
| `-e` | 输出错误信息 |
| `-U` | 强制更新依赖（跳过本地缓存） |

### 5.2 示例

```bash
# 强制更新依赖并查看调试信息
mvn clean install -U -X

# 跳过测试并强制更新依赖
# -D表示后面要附加命令的参数，字母D和后面的参数是紧挨着的，中间没有任何其它字符# maven.test.skip=true 表示在执行命令的过程中跳过测试
mvn clean install -Dmaven.test.skip=true -U
```

## 六、常见问题及解决

### 6.1 依赖冲突

```bash
# 使用 -U 强制更新依赖
mvn clean install -U

# 分析依赖树找出冲突
mvn dependency:tree -Dverbose
```

### 6.2 清理构建缓存

```bash
# 清理本地仓库中的依赖缓存
mvn clean -U
```

### 6.3 构建失败处理

```bash
# 仅编译不执行测试
mvn clean compile

# 跳过插件执行
mvn clean install -Dmaven.plugin.skip=true
```

## 七、最佳实践

1. **开发流程**：
   ```bash
   mvn clean install -Dmaven.test.skip=true
   ```

2. **生产环境打包**：
   ```bash
   mvn clean package -Dmaven.test.skip=true
   ```

3. **调试模式**：
   ```bash
   mvn clean install -X
   ```

4. **强制更新**：
   ```bash
   mvn clean install -U
   ```

## 八、其他命令

1. `mvn -v` 命令和构建操作无关，只要正确配置了PATH，在任何目录下执行都可以。初次之外的 maven命令 基本都需要进入到项目的 pom 所在的文件夹下再输入命令。

## 九、总结

- 执行构建命令前，确保在 pom.xml 所在目录
- 使用 `-Dmaven.test.skip=true` 跳过测试
- 使用 `-U` 强制更新依赖
- 使用 `-X` 查看调试信息
- 使用 `dependency:tree` 分析依赖关系