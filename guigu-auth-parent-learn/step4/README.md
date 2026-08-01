# 编辑的内容

## 全局异常处理--特定异常处理--自定义异常处理(需手动抛出)------最终会调用何种异常处理需注意
    
    在  E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step4\common\service-util\src\main\java\com\rc\system\exception  下 添加2个异常处理
    在  E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step4\service-system\src\main\java\com\rc\system\controller\SysRoleController.java  中导入与使用


## mybatis的代码生成器的使用（生成的代码的结构和我们自己写的不同，需要微调）

    ```xml
    <!-- 在 service-system-4 包中添加 -->

    <!-- mybatis的代码生成器的两个包 -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-generator</artifactId>
        <version>3.4.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.velocity</groupId>
        <artifactId>velocity-engine-core</artifactId>
        <version>2.0</version>
    </dependency>
    ```

    新增 E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step4\service-system-4\src\test\java\com\rc\system\test\CodeGet.java



## jwt的集成与使用 MD5密码加密

    ```xml
    <!-- 在 common-util-4 包中添加 -->

    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
    </dependency>
    ```

    新增 E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step4\common-4\common-util-4\src\main\java\com\rc\common\utils\JwtHelper.java
    新增 E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step4\common-4\common-util-4\src\main\java\com\rc\common\utils\MD5.java



## Spring Security 的集成 开启后下次运行项目用浏览器调用接口时，需要一个登录的操作，登录的默认用户名是 user 密码在控制台

    ```xml
    <!-- 在 service-system-4 包中添加 -->

    <dependency>
        <groupId>com.rc</groupId>
        <artifactId>spring-security</artifactId>
        <version>1.0</version>
    </dependency>
    ```

    ```xml
    <!-- 在 spring-security-4 包中添加 -->


    <!-- Spring Security依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- Spring Security 有自己的管理 web页面 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <scope>provided </scope>
    </dependency>
    ```

    新增 E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step4\common-4\spring-security-4\src\main\java\com\rc\system\config\WebSecurityConfig.java