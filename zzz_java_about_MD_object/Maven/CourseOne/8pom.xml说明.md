<!-- pox.xml文件会转换为1.0-SNAPSHOT\pro1-maven-java-1.0-SNAPSHOT.pom文件，放在和jar包在的同一个文件夹中 -->
<!-- 因此我们在Maven的本地仓库中想看一个jar包原始的pom.xml文件时，查看对应的XXX.pom文件即可，他们本质上是同一个文件 -->

<!-- 根标签: project 表示对当前工程进行配置、管理 -->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	
	<!-- modelVersion  从Maven 2 开始就固定是 4.0.0 -->
	<!-- 代表当前 pom.xml 所采用的标签结构  一般不用去改 -->
  <modelVersion>4.0.0</modelVersion>


	<!-- 坐标信息 -->
	<!-- mvn打包后，在本地仓库中对应的路径： com\atguigu\maven\pro1-maven-java\1.0-SNAPSHOT\pro1-maven-java-1.0-SNAPSHOT.jar -->
	<!-- 公司项目域名的倒序，通常会多加 项目名称 -->
  <groupId>com.atguigu.maven</groupId>
	<!-- 项目下的某一模块的名称 -->
  <artifactId>pro1-maven-java</artifactId>
	<!-- 当前模块的版本 -->
  <version>1.0-SNAPSHOT</version>
	<!-- 工程打包方式 -->
	<!-- 取值为 jar: 生成 jar 包，说明这是一个 Java 工程 -->
	<!-- 取值为 war: 生成 war 包，说明这是一个 Web 工程 -->
	<!-- 取值为 war: 说明这是一个用来管理其他工程的工程 -->
  <packaging>jar</packaging>


	<!-- 当前工程的名称 -->
  <name>pro1-maven-java</name>
  <url>http://maven.apache.org</url>


	<!-- 用来再 Maven 工程中定义属性值 -->
  <properties>
		<!-- 再构建过程中读取源码时使用的字符集 -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>


	<!-- 配置工程的所有的依赖信息，可以包含多个 dependency 子标签 -->
  <dependencies>
		<!-- 配置一个具体的依赖信息 -->
    <dependency>
			<!-- 要使用依赖的坐标信息，需要导入哪个jar包，就再 dependency 中配置其坐标信息即可 -->
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
			
			<!-- 配置当前依赖的范围 -->
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
