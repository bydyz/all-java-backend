package org.rc.apiCollect.basicApi.util.Date.collectAPI;

import java.util.Date;

public class toString {
    public static void main(String[] args) {
        // 在 Java 中，Date 类的 toString() 方法返回一个表示日期和时间的字符串。这个方法按照一定的格式来格式化日期对象，这个格式大致是：
        //     EEE MMM dd HH:mm:ss zzz yyyy
        // 其中：
        //     EEE 是星期的缩写（例如，Sun、Mon）。
        //     MMM 是月份的缩写（例如，Jan、Feb）。
        //     dd 是月份中的天数（00-31）。
        //     HH 是小时数（00-23）。
        //     mm 是分钟数（00-59）。
        //     ss 是秒数（00-59）。
        //     zzz 是时区（例如，GMT、PST）。
        //     yyyy 是四位年份。
        Date a = new Date();
        System.out.println(a);              // Sat Jun 08 11:24:37 CST 2024

        String str1 = a.toString();
        System.out.println(str1);           // Sat Jun 08 11:24:37 CST 2024
    }
}
