package org.rc.apiCollect.basicApi.time.Instant;

import java.time.Instant;
// 这个时间表示是与时区无关的，总是以 UTC 为准。
// 在 Java 中，Instant 类型用于表示瞬时时间点，它与任何时区无关，总是表示 UTC 时间。
// 如果你需要将这个时间点转换为特定时区的时间，可以使用 ZonedDateTime 类进行转换。
// 在处理时间序列、日志记录、事件时间戳等场景时，使用 UTC 时间可以避免时区带来的混淆。

public class parse {
    public static void main(String[] args) {
        // 使用 ISO 8601 格式的字符串创建 Instant 对象
        Instant a = Instant.parse("2024-06-09T02:47:11.674675500Z");
        System.out.println(a);


        String str1 = "2024-06-09T02:47:11.674675500Z";
        System.out.println(Instant.parse(str1));
    }
}
