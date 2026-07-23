package org.rc.apiCollect.basicApi.timeFormat.DateTimeFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ISOLOCALDATE {
    public static void main(String[] args) {
        // ISO_LOCAL_DATE 是一个格式化程序工厂方法，用于创建一个遵循 ISO 8601 日期格式的 DateTimeFormatter。它生成的格式是 yyyy-MM-dd。

        LocalDate today = LocalDate.now();
        System.out.println(today);      // 2024-06-10

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        String formattedDate = today.format(formatter);
        System.out.println("Formatted LocalDate: " + formattedDate);        // 2024-06-10

        // ISO_LOCAL_DATE_TIME、ISO_LOCAL_DATE 和 ISO_LOCAL_TIME 都是 DateTimeFormatter 类的静态工厂方法，用于创建格式化器，这些格式化器可以用于将日期和时间对象格式化为字符串，或者解析字符串为日期和时间对象。
        // 这些格式化程序遵循 ISO 8601 标准，这是一种广泛使用的国际标准，用于表示日期和时间。
        // 使用这些格式化程序可以帮助确保日期和时间的表示在不同系统和国家之间的一致性和可交换性。

        // 通过使用 ISO_LOCAL_DATE_TIME、ISO_LOCAL_DATE 和 ISO_LOCAL_TIME，你可以方便地按照国际标准格式化和解析日期和时间值。
    }

}
