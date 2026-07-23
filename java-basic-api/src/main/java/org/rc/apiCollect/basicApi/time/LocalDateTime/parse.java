package org.rc.apiCollect.basicApi.time.LocalDateTime;

import java.time.LocalDateTime;

// parse(String text)：从字符串解析为 LocalDateTime 实例（默认格式为 yyyy-MM-dd'T'HH:mm:ss）
public class parse {
    public static void main(String[] args) {
        // 从字符串解析日期时间
        String dateTimeString = "2023-05-20T15:30:45";
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTimeString);
        System.out.println("Parsed Date and Time: " + parsedDateTime);
    }
}
