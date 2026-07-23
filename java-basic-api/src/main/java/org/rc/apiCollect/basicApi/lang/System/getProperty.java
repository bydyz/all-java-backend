package org.rc.apiCollect.basicApi.lang.System;

public class getProperty {
    public static void main(String[] args) {
        // java运行时环境版本
        String javaVersion = System.getProperty("java.version");
        System.out.println("java 的 version:" + javaVersion);

        // java安装目录
        String javaHome = System.getProperty("java.home");
        System.out.println("java 的 home:" + javaHome);

        // 操作系统名称
        String osName = System.getProperty("os.name");
        System.out.println("os 的 name:" + osName);

        // 操作系统版本
        String osVersion = System.getProperty("os.version");
        System.out.println("os 的 version:" + osVersion);

        // 用户的账户名称
        String userName = System.getProperty("user.name");
        System.out.println("user 的 name:" + userName);

        // 用户的主目录
        String userHome = System.getProperty("user.home");
        System.out.println("user 的 home:" + userHome);

        // 用户当前的工作目录
        String userDir = System.getProperty("user.dir");
        System.out.println("user 的 dir:" + userDir);
    }
}
