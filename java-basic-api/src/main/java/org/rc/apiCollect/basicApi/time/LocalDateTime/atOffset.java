package org.rc.apiCollect.basicApi.time.LocalDateTime;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class atOffset {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now(); // 获取当前本地日期时间
        System.out.println(localDateTime);      // 2025-01-06T17:07:58.252629800



        ZoneOffset offset = ZoneOffset.of("+02:00"); // 定义时区偏移量，例如东二区

        OffsetDateTime zonedDateTime1 = localDateTime.atOffset(offset);
        System.out.println(zonedDateTime1);     // 2025-01-06T17:07:58.252629800+02:00



        ZonedDateTime zonedDateTime2 = localDateTime.atOffset(offset).toZonedDateTime();
        System.out.println("ZonedDateTime with offset: " + zonedDateTime2);     // 2025-01-06T17:07:58.252629800+02:00


        // atOffset 方法是 Java 8 中 java.time.LocalDateTime 类的一个实例方法，
        // 它用于将一个没有时区信息的本地日期时间（LocalDateTime 对象）与一个时区偏移量（ZoneOffset）结合，
        // 从而创建一个带有时区信息的日期时间对象（ZonedDateTime 对象）。

        // public OffsetDateTime atOffset(ZoneOffset offset) {
        //     return OffsetDateTime.of(this, offset);
        // }

        // offset: 一个 ZoneOffset 对象，表示与协调世界时（UTC）的时区偏移量。
        // 返回一个 OffsetDateTime 对象，它结合了原始的 LocalDateTime 对象和提供的时区偏移量。






        // LocalDateTime 是无时区的日期时间表示，它只包含日期和时间信息，但不包含时区信息。
        // ZoneOffset 是 java.time 包中的一个类，用于表示时区偏移量。它可以接受如 "+02:00"、"-05:00" 或 "Z"（表示 UTC 或零偏移量）这样的字符串。
        // ZonedDateTime 是带有明确时区信息的日期时间表示，它结合了日期时间和时区偏移量。
        // 使用 atOffset 方法可以轻松地将本地日期时间转换为特定时区的日期时间，这对于处理跨越不同时区的日期时间计算非常重要。
        // atOffset 方法是不可逆的，因为一旦你将 LocalDateTime 与一个时区偏移量结合，就不能再从 ZonedDateTime 中恢复出原始的无时区信息。
    }
}
