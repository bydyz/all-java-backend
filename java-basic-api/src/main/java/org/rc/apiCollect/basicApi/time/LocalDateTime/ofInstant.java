package org.rc.apiCollect.basicApi.time.LocalDateTime;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

// 在 Java 中
// LocalDateTime 类表示没有时区的日期和时间。
// Instant 类表示时间线上的一个瞬时点，通常与协调世界时（UTC）相关联。
// ZoneId 用于表示时区。
public class ofInstant {
    // 假设你有一个文件，你想获取它最后修改的时间，并将其转换为上海时区（"Asia/Shanghai"）的 LocalDateTime。

    public static void main(String[] args) {
        // 创建一个文件路径
        Path path = Paths.get("/Java/forJavaIO/a/a/a.txt");
        System.out.println("000   " + path);
        File file = path.toFile();
        System.out.println("111   " + file);

        // 检查文件是否存在
        if (file.exists()) {
            // 获取文件最后修改的时间（毫秒）
            long lastModifiedMillis = file.lastModified();
            System.out.println("222   " + lastModifiedMillis);

            // 使用文件最后修改的时间创建一个 Instant 对象
            Instant instant = Instant.ofEpochMilli(lastModifiedMillis);
            System.out.println("333   " + instant);

            // 指定时区 "Asia/Shanghai"
            ZoneId zoneId = ZoneId.of("Asia/Shanghai");
            System.out.println("444   " + zoneId);

            // 将 Instant 对象转换为 LocalDateTime 对象，转换为上海时区的时间
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

            // 打印结果
            System.out.println("Last modified time in Shanghai timezone: " + localDateTime);
        } else {
            System.out.println("File does not exist.");
        }
    }
}
