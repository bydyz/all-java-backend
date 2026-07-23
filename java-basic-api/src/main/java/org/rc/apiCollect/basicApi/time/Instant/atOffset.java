package org.rc.apiCollect.basicApi.time.Instant;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class atOffset {
    public static void main(String[] args) {
        Instant a = Instant.now();
        System.out.println(a);

        ZoneOffset offset = ZoneOffset.of("+02:00"); // 定义时区偏移量，例如东二区
        // offset: 一个 ZoneOffset 对象，表示时区的偏移量。
        System.out.println(offset);


        OffsetDateTime b = a.atOffset(offset);
        // ZoneOffset 是一个表示时区偏移量的类，它可以接受如 "+02:00"、"-05:00" 或 "Z"（表示 UTC）这样的字符串。
        // 使用 atOffset 方法可以轻松地将无时区的时间点转换为有明确时区的时间点，这对于处理需要时区信息的日期时间计算非常重要。
        // ZonedDateTime 类型是不可变的，并且是线程安全的。
        // 在进行日期时间的转换和计算时，要特别注意夏令时（Daylight Saving Time, DST）的影响，因为这可能会影响时区偏移量。

        // b 是返回的一个 ZonedDateTime 对象，它结合了原始时间点和提供的时区偏移量。


        // 在 Java 中，atOffset 是 java.time.Instant 和 java.time.LocalDateTime 类中的一个方法，它用于将一个时间点与一个时区偏移量（ZoneOffset）结合，从而创建一个 ZonedDateTime 对象。ZonedDateTime 是带有明确时区信息的日期时间对象。

        // 对于 Instant 类，atOffset 方法允许你将一个无时区的瞬时时间点与一个时区偏移量结合，从而获得一个 ZonedDateTime 对象。
        System.out.println(b);
    }
}
