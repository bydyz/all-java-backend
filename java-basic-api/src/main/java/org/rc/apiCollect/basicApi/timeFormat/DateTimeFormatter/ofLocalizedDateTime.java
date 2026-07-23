package org.rc.apiCollect.basicApi.timeFormat.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ofLocalizedDateTime {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("111" + localDateTime);


        // 本地化相关的格式。如：ofLocalizedDateTime()
        // FormatStyle.LONG / FormatStyle.MEDIUM / FormatStyle.SHORT   :适用于 LocalDateTime
        DateTimeFormatter formatter1 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        // 格式化
        String str2 = formatter1.format(localDateTime);
        System.out.println("111-2" + localDateTime);
        System.out.println("222" + str2);   // 2022 年 12 月 4 日 下午 09 时 03 分 55 秒


        DateTimeFormatter formatter2 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        String str3 = formatter2.format(localDateTime);
        System.out.println("111-3" + localDateTime);
        System.out.println("333" + str3);


        DateTimeFormatter formatter3 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        String str4 = formatter3.format(localDateTime);
        System.out.println("111-4" + localDateTime);
        System.out.println("444" + str4);


        // 暂不处理

        // java.time.DateTimeException 是一个由 Java 中的日期时间 API 抛出的运行时异常，表明在处理日期时间时遇到了问题。在你提供的错误信息中：
        //
        // Exception in thread "main" java.time.DateTimeException: Unable to extract ZoneId from temporal 2024-06-10T15:35:20.982412100
        //
        // 异常信息 Unable to extract ZoneId from temporal 指出了具体的问题：无法从给定的时间（temporal）中提取时区信息（ZoneId）。这通常发生在尝试将一个没有明确时区信息的时间点转换为需要时区信息的时间点时。
        //
        // 详解
        // 问题原因：错误信息中的字符串 "2024-06-10T15:35:20.982412100" 表示一个具体的时刻，但它本身不包含时区信息。在 ISO 8601 格式中，如果时间字符串的末尾有一个 'Z' 字符，它表示协调世界时（UTC）。如果时间字符串包含时区偏移量（例如 +02:00），它将指示特定的时区。在你的例子中，时间字符串没有包含这些信息。
        //
        // 解决方法：如果你需要处理一个没有时区信息的时间字符串，并且你想要将它转换为带有时区的时间，你需要在解析时指定时区或者使用 ZonedDateTime 来保留原始的时区未知状态。
    }
}
