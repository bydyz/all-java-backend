package org.rc.apiCollect.basicApi.timeFormat.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class ISOLOCALDATETIME {
    // ISO_LOCAL_DATE_TIME 是一个格式化程序工厂方法，用于创建一个遵循 ISO 8601 日期和时间格式的 DateTimeFormatter。它生成的格式是 yyyy-MM-dd'T'HH:mm:ss。

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);        // 2024-06-10T10:10:32.887475

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        // 格式化:日期-->字符串
        String formattedDateTime = now.format(formatter);
        System.out.println(now);        // 2024-06-10T10:10:32.887475
        System.out.println("Formatted LocalDateTime: " + formattedDateTime);        // 2024-06-10T10:10:32.887475


        // 解析：字符串 -->日期
        TemporalAccessor parse = formatter.parse("2022-12-04T21:02:14.808");
        System.out.println(parse);      // {},ISO resolved to 2022-12-04T21:02:14.808
        LocalDateTime dateTime = LocalDateTime.from(parse);
        System.out.println(dateTime);       // 2022-12-04T21:02:14.808


        // ISO_LOCAL_DATE_TIME、ISO_LOCAL_DATE 和 ISO_LOCAL_TIME 都是 DateTimeFormatter 类的静态工厂方法，用于创建格式化器，这些格式化器可以用于将日期和时间对象格式化为字符串，或者解析字符串为日期和时间对象。
        // 这些格式化程序遵循 ISO 8601 标准，这是一种广泛使用的国际标准，用于表示日期和时间。
        // 使用这些格式化程序可以帮助确保日期和时间的表示在不同系统和国家之间的一致性和可交换性。

        // 通过使用 ISO_LOCAL_DATE_TIME、ISO_LOCAL_DATE 和 ISO_LOCAL_TIME，你可以方便地按照国际标准格式化和解析日期和时间值。
    }
}

