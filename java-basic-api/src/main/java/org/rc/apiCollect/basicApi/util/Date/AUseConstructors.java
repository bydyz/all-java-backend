package org.rc.apiCollect.basicApi.util.Date;

import java.util.Date;

public class AUseConstructors {
    public static void main(String[] args) {
        // 表示特定的瞬间，精确到毫秒。

        // 使用无参构造器创建的对象可以获取本地当前时间
        Date a = new Date();
        System.out.println(a);      // Sat Jun 08 11:10:50 CST 2024


        // 把该毫秒值换算成日期时间对象
        Date b = new Date(666666);
        System.out.println(b);      // Thu Jan 01 08:11:06 CST 1970
    }
}
