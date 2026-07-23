package org.rc.apiCollect.basicApi.time.LocalDateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class toStringAndStringTo {
    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.now();

        // 定义自定义格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        // 格式化日期时间
        String formattedDateTime = dateTime.format(formatter);
        System.out.println("Formatted Date and Time: " + formattedDateTime);

        // 解析日期时间
        String dateTimeString = "2023/05/20 15:30:45";
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTimeString, formatter);
        System.out.println("Parsed Date and Time: " + parsedDateTime);
    }
}
