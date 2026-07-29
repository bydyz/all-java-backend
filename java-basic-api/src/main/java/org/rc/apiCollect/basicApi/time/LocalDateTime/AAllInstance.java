package org.rc.apiCollect.basicApi.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class AAllInstance {
    
    // 实例方法  获取时间
    @Test
    public void test0() {
        LocalDateTime dateTime = LocalDateTime.now();

        System.out.println("Year: " + dateTime.getYear());
        System.out.println("Month: " + dateTime.getMonth().name());
        System.out.println("Day of Month: " + dateTime.getDayOfMonth());
        System.out.println("Hour: " + dateTime.getHour());
        System.out.println("Minute: " + dateTime.getMinute());
        System.out.println("Second: " + dateTime.getSecond());

        System.out.println("Date only: " + dateTime.toLocalDate());
        System.out.println("Time only: " + dateTime.toLocalTime());
    }
    
    
    @Test
    public void test1() {
        LocalDateTime dateTime = LocalDateTime.now();

        // 修改日期时间       不会修改源数据
        LocalDateTime modifiedDateTime = dateTime.withYear(2023)
                .withMonth(5)
                .withDayOfMonth(20)
                .withHour(15)
                .withMinute(30)
                .withSecond(45);

        System.out.println("Modified Date and Time: " + modifiedDateTime);

        // 增加或减少日期时间       不会修改源数据
        LocalDateTime futureDateTime = dateTime.plusDays(1).plusHours(2);
        LocalDateTime pastDateTime = dateTime.minusDays(1).minusHours(2);

        System.out.println("Future Date and Time (+1 day, +2 hours): " + futureDateTime);
        System.out.println("Past Date and Time (-1 day, -2 hours): " + pastDateTime);
    }
}
