package org.rc.apiCollect.basicApi.time.LocalDateTime;

import java.time.LocalDateTime;

// of(int year, int month, int dayOfMonth, int hour, int minute)：创建指定年月日时分的 LocalDateTime 实例
public class of {
    public static void main(String[] args) {
        // 创建指定日期时间的实例
        LocalDateTime specificDateTime = LocalDateTime.of(2023, 5, 20, 15, 30);
        System.out.println("Specific Date and Time: " + specificDateTime);
    }
}
