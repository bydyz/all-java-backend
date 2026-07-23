package org.rc.apiCollect.basicApi.util.TimeZone.collectAPI;

import java.util.TimeZone;

public class getTimeZone {
    public static void main(String[] args) {
        // public static TimeZone getTimeZone(String ID)

        // ID: 时区的唯一标识符。这个ID可以是多种格式，最常见的是"GMT"或"UTC"加上零或更多的小时、分钟和秒偏移量，格式为"GMT±hh:mm"或"UTC±hh:mm"。例如，"GMT+08:00"代表北京时间。

        // 返回一个 TimeZone 对象，它与指定的时区ID关联。

        // TimeZone.getTimeZone() 是 Java 中 java.util 包下 TimeZone 类的一个静态方法，用于获取与特定时区ID关联的 TimeZone 对象。每个 TimeZone 对象都包含有关特定时区的信息，例如夏令时规则和UTC（协调世界时）的偏移量。

        // 获取特定时区的 TimeZone 对象
        TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");

        System.out.println(timeZone);

        // 打印时区ID
        System.out.println("Time Zone ID: " + timeZone.getID());
        // 打印时区的显示名称
        System.out.println("Time Zone Display Name: " + timeZone.getDisplayName());
        // 打印时区相对于UTC的偏移量，其中 false 表示标准时间（非夏令时）
        System.out.println("Time Zone Offset: " + timeZone.getRawOffset());



        // TimeZone.getTimeZone() 方法接受的时区ID应该是有效的，否则会抛出 IllegalArgumentException。
        // TimeZone 对象是不可变的，并且是线程安全的。
        // 时区的偏移量是相对于UTC的毫秒数。例如，如果偏移量是 -28800000（即 -8 * 60 * 60 * 1000），这意味着该时区比UTC时间晚8个小时。
        // 时区对象的 getID() 方法返回的时区ID遵循 "GMT±hh:mm" 的格式，这与ISO 8601标准不同，后者通常使用 "±hh:mm" 格式。
        //
        // TimeZone.getTimeZone() 方法是处理时区相关操作的基本工具，它允许你获取特定时区的信息，并在需要时区感知的日期和时间计算中使用这些信息。在全球化的应用程序中，正确处理时区是非常重要的。
    }
}
