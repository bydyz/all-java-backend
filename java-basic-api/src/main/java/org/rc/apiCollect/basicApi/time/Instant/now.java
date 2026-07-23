package org.rc.apiCollect.basicApi.time.Instant;

import java.time.Instant;
// Instant 类代表了一个时间线上的瞬时点，与时区无关，始终表示 UTC 时间。
// Instant.now() 方法提供了一种简单的方式来获取当前时间的 Instant 表示，常用于记录事件发生的时间戳。
// 由于 Instant 是不可变的，所以它是线程安全的，可以作为并发编程中的时间戳使用。
// Instant 类型常用于分布式系统之间的时间同步，因为它提供了一种统一的时间表示方式。

// 与其他日期时间类的比较
//     与 java.util.Date 相比，Instant 提供了更强的类型安全性和更丰富的日期时间API。
//     与 java.time.LocalDateTime 相比，Instant 表示的是 UTC 时间，而 LocalDateTime 表示的是没有时区信息的本地时间。
//     与 java.time.ZonedDateTime 相比，Instant 不包含时区信息，而 ZonedDateTime 包含了时区信息。

// 注意事项
// Instant.now() 方法返回的时间戳是基于系统时间的，如果系统时间发生变化，获取的 Instant 也会相应变化。
// 在进行时间计算时，可以使用 Instant 类的 plus 和 minus 方法来添加或减去时间量。
// 尽管 Instant 广泛用于记录时间戳，但在需要处理具体日期和时间（如年、月、日、时、分等）时，可能需要转换为其他日期时间类，如 LocalDateTime 或 ZonedDateTime。
//
// Instant.now() 方法是 Java 8 日期时间API 的基础部分，提供了一种简单、可靠的方式来获取当前 UTC 时间的瞬时表示。

public class now {
    public static void main(String[] args) {
        Instant a = Instant.now();
        System.out.println(a);      // 2024-06-09T02:47:11.674675500Z

        // "2024-06-09T02:47:11.674675500Z" 是一个使用 ISO 8601 格式表示的时间点的 UTC 时间，它代表了协调世界时（UTC）的一个具体时刻。下面是对这个时间表示的详细解释：

            // 2024: 这是年份，表示为 2024 年。
            // 06: 这是月份，表示为 6 月。
            // 09: 这是日，表示为 9 日。

            // T: 这是时间（Time）的分隔符，用于区分日期和时间部分。

            // 02: 这是小时，表示为 2 点（24小时制）。
            // 47: 这是分钟，表示为 47 分。
            // 11: 这是秒，表示为 11 秒。

            // .674675500: 这是秒的小数部分，表示为 674675500 纳秒。这提供了时间的更高精度，通常用于需要精确时间测量的场合。

            // Z: 这表示该时间是使用协调世界时（UTC，Universal Time Coordinated）表示的，即没有时区偏移。在 ISO 8601 格式中，"Z" 总是用来表示 UTC 时间。


    }
}
