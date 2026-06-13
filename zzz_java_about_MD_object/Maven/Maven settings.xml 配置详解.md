# Maven 安装

解压完 maven的压缩包 后即可使用

# Maven `settings.xml` 配置详解

> `settings.xml` 是 Maven 的核心配置文件，用于定义**全局**或**用户级**的 Maven 行为。
> 位于 `~/.m2/settings.xml`（用户级）或 `$M2_HOME/conf/settings.xml`（全局级）。

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!--
  ============================================================
  settings 根元素
  - xmlns:xsi: 定义 XML Schema 命名空间
  - xsi:schemaLocation: 指定 XSD 校验文件的位置
  ============================================================
-->
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">

    <!--
      ==========================================================
      localRepository
      - 本地仓库路径，Maven 下载的依赖和插件都会存储在这里
      - 默认值: ${user.home}/.m2/repository
      - 修改后可以避免 C 盘空间膨胀，或在不同项目间共享仓库
      ==========================================================
    -->
    <localRepository>D:/maven-repository</localRepository>

    <!--
      ==========================================================
      interactiveMode
      - 是否启用交互模式（命令行中询问用户输入）
      - true:  构建过程中遇到需要输入时会暂停等待用户输入
      - false: 所有交互都使用默认值，适合自动化脚本/CI 环境
      ==========================================================
    -->
    <interactiveMode>true</interactiveMode>

    <!--
      ==========================================================
      offline
      - 是否强制 Maven 在离线模式下工作
      - false: 每次构建都会检查远程仓库是否有新版本的依赖
      - true:  只使用本地仓库已有的依赖，不访问任何远程仓库
               适合没有网络的环境，可显著加快构建速度
      ==========================================================
    -->
    <offline>false</offline>

    <!--
      ==========================================================
      pluginGroups
      - 定义一组"组织ID（groupId）"，当在命令行运行插件时，
        可以省略 groupId 前缀，直接使用插件名称
      - 默认已包含: org.apache.maven.plugins, org.codehaus.mojo
      - 例如配置后，运行 mvn help:system 等效于
        mvn org.apache.maven.plugins:maven-help-plugin:system
      ==========================================================
    -->
    <pluginGroups>
        <pluginGroup>org.apache.maven.plugins</pluginGroup>
        <pluginGroup>org.codehaus.mojo</pluginGroup>
    </pluginGroups>

    <!--
      ==========================================================
      proxies
      - 配置网络代理，当公司/学校网络需要通过代理访问外网时使用
      - 可以配置多个 proxy，Maven 会根据 id 和 active 选择使用哪个
      ==========================================================
    -->
    <proxies>
        <!--
          proxy 元素 — 单个代理配置
          - id:            代理的唯一标识
          - active:        true 表示启用此代理，false 表示禁用
          - protocol:      代理协议，通常为 http 或 https
          - host:          代理服务器主机名或 IP
          - port:          代理服务器端口
          - username:      代理认证用户名（无需认证可省略）
          - password:      代理认证密码（无需认证可省略）
          - nonProxyHosts: 不走代理的主机列表，支持通配符 *，用 | 分隔
        -->
        <proxy>
            <id>company-proxy</id>
            <active>true</active>
            <protocol>http</protocol>
            <host>proxy.company.com</host>
            <port>8080</port>
            <username>proxyuser</username>
            <password>proxypass</password>
            <nonProxyHosts>localhost|127.0.0.1|*.company.com</nonProxyHosts>
        </proxy>
    </proxies>

    <!--
      ==========================================================
      servers
      - 配置远程服务器认证信息
      - 这些信息用于 Maven 部署构件到远程仓库（如 Nexus、Artifactory）
        或从私有仓库下载依赖时的身份认证
      - 密码经过可选的加密处理（使用 maven-password-cipher）
      ==========================================================
    -->
    <servers>
        <!--
          server 元素 — 单个服务器认证配置
          - id:          服务器的标识，必须与 pom.xml 中 repository/distributionManagement 的 id 一致
          - username:    登录服务器的用户名
          - password:    登录服务器的密码（可以是加密后的密文）
          - filePermissions:  部署时文件权限（UNIX 八进制格式，如 664）
          - directoryPermissions: 部署时目录权限（UNIX 八进制格式，如 775）
          - configuration: 额外配置，具体内容取决于服务器类型
        -->
        <server>
            <id>nexus-releases</id>
            <username>deployer</username>
            <password>deployer-password</password>
        </server>
        <server>
            <id>nexus-snapshots</id>
            <username>deployer</username>
            <password>deployer-password</password>
        </server>
        <!-- 私有仓库只读访问（无需密码的私有仓库） -->
        <server>
            <id>nexus-public</id>
            <username>reader</username>
            <password>reader-password</password>
        </server>
    </servers>

    <!--
      ==========================================================
      mirrors
      - 配置镜像仓库
      - 镜像会拦截对指定远程仓库的请求，将其重定向到镜像地址
      - 通常用于:
        1. 使用国内镜像（如阿里云镜像）加速依赖下载
        2. 使用公司私有 Nexus 代理所有外部仓库
        3. 统一仓库入口，便于管理和审计
      - 注意: 只会影响 settings.xml 中配置的仓库，不会影响 pom.xml 中定义的仓库
        （除非 mirrorOf 设置为 *）
      ==========================================================
    -->
    <mirrors>
        <!--
          mirror 元素 — 单个镜像配置
          - id:             镜像的唯一标识
          - name:           镜像的友好名称，仅用于显示
          - url:            镜像的地址，所有被拦截的请求都会发往这里
          - mirrorOf:      指定拦截哪些仓库，支持以下值:
                *                  → 拦截所有仓库（最常用）
                central           → 只拦截 Maven Central
                repo1,repo2       → 拦截指定 id 的多个仓库
                *,!repo1          → 拦截除了 repo1 之外的所有仓库
                external:*        → 拦截所有非 localhost 和非 file:// 的仓库
        -->
        <!-- 阿里云国内镜像，大幅加速 Maven Central 的下载速度 -->
        <mirror>
            <id>aliyun-maven</id>
            <name>Aliyun Maven Mirror</name>
            <url>https://maven.aliyun.com/repository/public</url>
            <mirrorOf>central</mirrorOf>
        </mirror>

        <!-- 公司私有 Nexus，代理所有外部仓库 -->
        <mirror>
            <id>company-nexus</id>
            <name>Company Nexus Mirror</name>
            <url>https://nexus.company.com/repository/maven-public/</url>
            <mirrorOf>*</mirrorOf>
        </mirror>
    </mirrors>

    <!--
      ==========================================================
      profiles
      - 配置文件（Profile），允许根据不同环境定制构建行为
      - profiles 可以定义在 settings.xml 中（用户级）或 pom.xml 中（项目级）
      - 在 settings.xml 中定义的 profile 对所有项目生效
      - 通过 activeProfiles 或 -P 参数激活
      ==========================================================
    -->
    <profiles>
        <!--
          profile 元素 — 单个配置文件的定义
          包含以下可选子元素:
          - id:              profile 的唯一标识（用于激活时引用）
          - activation:      自动激活条件（JDK 版本、操作系统、文件存在等）
          - repositories:    依赖仓库列表（告诉 Maven 从哪些仓库下载依赖）
          - pluginRepositories: 插件仓库列表（告诉 Maven 从哪些仓库下载插件）
          - properties:      自定义属性，可在 pom.xml 中以 ${key} 形式引用
        -->
        <profile>
            <!--
              id: profile 的唯一标识
              推荐命名方式:
              - repo-xxx: 表示仓库相关
              - jdk-xxx:  表示 JDK 版本相关
              - env-xxx:  表示环境相关（dev/test/prod）
            -->
            <id>company-repo</id>

            <!--
              activation: 自动激活条件
              Maven 在构建时会检查这些条件，满足时自动激活此 profile
              可选条件:
              - activeByDefault: 是否默认激活（true/false）
              - jdk:             匹配 JDK 版本，支持区间如 [1.8,)
              - os:              匹配操作系统（name/family/arch/version）
              - property:        匹配系统属性（name/value）
              - file:            匹配文件是否存在（exists/missing）
            -->
            <activation>
                <!-- 默认激活，除非有更具体的 profile 也被激活 -->
                <activeByDefault>true</activeByDefault>
                <!-- 当 JDK 版本在 1.8 及以上时激活 -->
                <jdk>1.8</jdk>
                <!-- 当操作系统为 Windows 时激活 -->
                <os>
                    <family>windows</family>
                </os>
            </activation>

            <!--
              repositories: 依赖仓库定义
              每个 repository 包含:
              - id:        仓库唯一标识（必须全局唯一）
              - name:      仓库显示名称
              - url:       仓库地址
              - releases:  发布版策略（enabled/updatePolicy/checksumPolicy）
              - snapshots: 快照版策略（enabled/updatePolicy/checksumPolicy）
            -->
            <repositories>
                <repository>
                    <id>company-central</id>
                    <name>Company Central Repository</name>
                    <url>https://nexus.company.com/repository/maven-public/</url>
                    <!--
                      releases/snapshots 的策略配置:
                      - enabled:       是否启用该类型的版本
                                       true  → 允许下载发布版
                                       false → 忽略发布版
                      - updatePolicy:  更新策略，决定 Maven 何时检查远程仓库的新版本
                                       always  → 每次都检查
                                       daily   → 每天检查一次（默认值）
                                       interval:X → 每 X 分钟检查一次
                                       never   → 从不检查，只用本地缓存的
                      - checksumPolicy:校验和策略
                                       warn    → 校验和不匹配时发出警告（默认）
                                       fail    → 校验和不匹配时构建失败
                                       ignore  → 忽略校验和检查
                    -->
                    <releases>
                        <enabled>true</enabled>
                        <updatePolicy>daily</updatePolicy>
                        <checksumPolicy>warn</checksumPolicy>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                        <updatePolicy>always</updatePolicy>
                        <checksumPolicy>warn</checksumPolicy>
                    </snapshots>
                </repository>
            </repositories>

            <!--
              pluginRepositories: 插件仓库定义
              结构与 repositories 完全一致，专门用于存储 Maven 插件
              注意: 插件和依赖可能位于不同的仓库中
            -->
            <pluginRepositories>
                <pluginRepository>
                    <id>company-plugin-repo</id>
                    <name>Company Plugin Repository</name>
                    <url>https://nexus.company.com/repository/maven-public/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </pluginRepository>
            </pluginRepositories>

            <!--
              properties: 自定义属性
              可以在 pom.xml 中以 ${property.name} 引用
              常见的用途:
              - 项目级版本号统一管理
              - 编译器版本配置
              - 编码设置
              - 环境相关路径
            -->
            <properties>
                <maven.compiler.source>11</maven.compiler.source>
                <maven.compiler.target>11</maven.compiler.target>
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                <!-- 自定义属性，可在 pom.xml 中通过 ${company.email} 引用 -->
                <company.email>dev@company.com</company.email>
            </properties>
        </profile>

        <!-- 针对 JDK 17 的特殊配置 -->
        <!-- 此配置的核心目的是：当 Maven 构建运行在 JDK 17 环境下（或希望显式兼容 JDK 17）时，强制将项目的源代码级别和目标字节码级别都锁定为 17。 -->
        <!-- 如果当前 JDK 为 17，并且没有其他 profile 强制抢占激活，则此 profile 自动生效。 -->
        <!-- 指定该 profile 的唯一标识符为 jdk17，方便手动激活（如 mvn clean install -P jdk17）或在配置中引用。 -->
        <!-- 
        典型使用场景： 
            1. 你的项目必须运行在 JDK 17 或更高版本，但你希望生成的字节码严格兼容 JDK 17，避免意外使用更高版本的 API 或语言特性。
            2. 团队多人使用不同 JDK（如 17、21、11），通过 profile 自动适配编译参数，保证输出产物的一致性。
            3. 在 CI/CD 中，构建环境是 JDK 17，但 Maven 项目默认的 maven.compiler 属性可能被父 POM 覆盖，通过 profile 强制纠正。

        潜在问题：
            1.如果运行 Maven 的 JDK 版本低于 17（如 JDK 11），该 profile 不会激活，因为 <jdk>17</jdk> 条件不满足。此时若未定义其他 profile 或全局编译属性，可能回退到默认的较老版本（如 1.5），导致编译失败。建议同时为低版本 JDK 定义对应的 profile。
            2.由于 activeByDefault 与 <jdk> 同时存在，会导致“仅在 JDK 17 下才默认激活”的行为，不符合直觉。更常见的写法是只保留 <jdk>17</jdk>，去掉 activeByDefault，让 profile 完全由 JDK 版本驱动。
        -->
        <profile>
            <id>jdk17</id>
            <activation>
              <!-- 表示如果没有其他任何 profile 被显式激活，则此 profile 会作为默认配置生效。 -->
              <!-- 注意：activeByDefault 的优先级很低——当存在其他通过 -P、<activeProfiles> 或系统属性激活的 profile 时，默认激活不会生效。 -->
              <activeByDefault>true</activeByDefault>
              <jdk>17</jdk>
            </activation>

            <!-- 这些属性仅在 maven-compiler-plugin 未显式配置 source、target、compilerVersion 时才会生效。如果插件中已经硬编码了值，profile 里的属性会被忽略。 -->
            <!-- 设置这些属性后，Maven 会确保使用 JDK 17 的编译器语义，但不会自动切换 JAVA_HOME——如果运行 Maven 的 JDK 本身就是 17，则自然一致；如果运行的是 JDK 21 但设定 source=17，则 Maven 会调用当前 JDK 21 的 javac 并以 -source 17 -target 17 模式编译，生成兼容 17 的字节码。 -->
            <properties>
                <!-- 设置源代码使用的 Java 语言版本，此处即 不允许使用高于 17 的语法特性 -->
                <maven.compiler.source>17</maven.compiler.source>
                <!-- 设置生成的字节码版本，使其能被指定版本的 JVM 运行。 -->
                <maven.compiler.target>17</maven.compiler.target>
                <!-- 指定 javac 编译器的版本（如 17）。通常与 source/target 保持一致，尤其在需要显式控制编译器实现时有用（例如使用不同 JDK 的编译器）。 -->
                <maven.compiler.compilerVersion>17</maven.compiler.compilerVersion>
            </properties>
        </profile>

        <!-- 开发环境配置 -->
        <profile>
            <id>dev</id>
            <properties>
                <env>dev</env>
                <db.url>jdbc:mysql://localhost:3306/dev_db</db.url>
            </properties>
        </profile>

        <!-- 生产环境配置 -->
        <profile>
            <id>prod</id>
            <properties>
                <env>prod</env>
                <db.url>jdbc:mysql://prod-server:3306/prod_db</db.url>
            </properties>
        </profile>
    </profiles>

    <!--
      ==========================================================
      activeProfiles
      - 指定默认激活的 profiles 列表
      - 列在这里的 profile 会在每次构建时自动激活
      - 可以在命令行用 -P 覆盖: mvn clean install -Pprod
      - 注意: 如果 profile 没有 activation 条件，仍然需要在这里主动声明
      ==========================================================
    -->
    <activeProfiles>
        <!-- 默认启用公司仓库配置 -->
        <activeProfile>company-repo</activeProfile>
        <!-- 可以激活多个 profile -->
        <!-- <activeProfile>dev</activeProfile> -->
    </activeProfiles>

</settings>
```

---

## 核心概念速查表

| 元素 | 作用 | 常见场景 |
|------|------|----------|
| `localRepository` | 本地依赖缓存路径 | 避免 C 盘占满，多项目共享缓存 |
| `mirrors` | 远程仓库镜像 | 国内使用阿里云镜像加速，公司统一 Nexus 入口 |
| `servers` | 远程服务器认证 | 向 Nexus/Artifactory 部署构件时提供用户名密码 |
| `proxies` | 网络代理 | 公司网络需要 HTTP 代理才能访问外网 |
| `profiles` | 环境配置集 | 区分开发/测试/生产环境的不同配置 |
| `activeProfiles` | 默认激活的 profile | 每次构建自动生效的配置 |
| `pluginGroups` | 插件组简写 | 命令行中省略插件的 groupId |

## Maven 配置优先级（从高到低）

1. **命令行参数**（`-D`、`-P` 等）
2. **用户级 settings.xml**（`~/.m2/settings.xml`）
3. **全局级 settings.xml**（`$M2_HOME/conf/settings.xml`）
4. **项目级 pom.xml**
5. **Maven 内置默认值**

## 常用命令

```bash
# 查看当前生效的 settings 配置
mvn help:effective-settings

# 查看当前生效的 pom 配置（含 profiles 解析结果）
mvn help:effective-pom

# 指定自定义 settings 文件路径
mvn clean install -s C:\path\to\settings.xml

# 使用全局 settings 文件
mvn clean install -gs C:\path\to\global-settings.xml

# 通过 -P 激活指定 profile
mvn clean install -Pprod

# 跳过测试
mvn clean install -DskipTests
```
