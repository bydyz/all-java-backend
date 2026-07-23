package org.rc.apiCollect.basicApi.lang.System;

public class exit {
    public static void main(String[] args) {
        System.out.println("111");
        System.exit(0);     // 程序停止，后面的 222 打印不会执行
        System.out.println("222");
    }
}
