# 必须安装 和 必须配置

1. 必须安装 lombok插件
2. 必须配置 lombok依赖
    ```xml
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.42</version>
        <!-- scope=provided 表示 Lombok 仅在编译时需要，运行时不需要。 -->
        <scope>provided</scope>
    </dependency>
    ```
3. 必须配置 `annotationProcessorPaths`
    ```xml
    <annotationProcessorPaths>
        <path>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.34</version>
        </path>
    </annotationProcessorPaths>
    ```


# 生成 jar包 后，使用 `java -jar jar的绝对路径` 即可运行