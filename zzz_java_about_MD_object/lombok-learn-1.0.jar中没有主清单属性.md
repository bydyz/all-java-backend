# 使用 `java -jar XXX.jar的绝对路径` 报 XXX.jar中没有主清单属性

这表示该 JAR 文件的 META-INF/MANIFEST.MF 中没有指定 Main-Class，因此 Java 不知道从哪个类的 main 方法启动。


## 原因分析

默认情况下，Maven 构建的普通 JAR（不带 `shade`）不会自动添加 `Main-Class` 属性。
你需要显式配置插件来指定主类，或者使用 可执行 fat JAR（如 `maven-shade-plugin` 或 `maven-assembly-plugin`）并配置主类。



## 解决方案（按推荐顺序）

### ✅ 方案一：使用 maven-shade-plugin 生成带主类的 fat JAR

最常见的方式，尤其适用于需要打包所有依赖的项目。

在 `pom.xml` 的 `<build><plugins>` 中添加：
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.5.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>com.yourpackage.YourMainClass</mainClass>   <!-- 改成你的主类全限定名 -->
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
</plugin>
```

生成的 JAR 通常带有 -shaded.jar 后缀（例如 lombok-learn-1.0-shaded.jar），这个 JAR 就包含了主清单属性。