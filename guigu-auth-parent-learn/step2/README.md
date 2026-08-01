# 编辑的内容

## 添加 service接口  service实现类  service测试  controller

1. E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step2\service-system\src\main\java\com\rc\system\controller\SysRoleController.java
2. E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step2\service-system\src\main\java\com\rc\system\service\SysRoleService.java
3. E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step2\service-system\src\main\java\com\rc\system\service\impl\SysRoleServiceImpl.java
4. E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step2\service-system\src\test\java\com\rc\system\test\SysRoleServiceTest.java


## 集成swagger

1. 在 service-util 添加依赖配置、增加配置类
    ```xml
    <!-- swagger的依赖包 -->
    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <artifactId>knife4j-spring-boot-starter</artifactId>
    </dependency>
    ```

   E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step2\common\service-util\src\main\java\com\rc\system\config\Knife4jConfig.java

2. 修改 E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step2\service-system\src\main\java\com\rc\system\controller\SysRoleController.java


## 为 E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step2\service-system\src\main\java\com\rc\system\controller\SysRoleController.java 多加上了 结果统一格式 的代码

在 E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step2\common\common-util\src\main\java\com\rc\common\result 里添加 Result.java  和  ResultCodeEnum.java