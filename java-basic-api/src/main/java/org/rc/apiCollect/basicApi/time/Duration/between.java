package org.rc.apiCollect.basicApi.time.Duration;

import org.junit.jupiter.api.Test;

import java.time.*;

public class between {
    public static void main(String[] args) {
        LocalDateTime t1 = LocalDateTime.now();
        System.out.println("111   " + t1);
        LocalDateTime t2 = LocalDateTime.of(2017, 8, 29, 0, 0, 0, 0);
        System.out.println("222   " + t2);
        // t2 - t1
        Duration between = Duration.between(t1, t2);
        System.out.println(between);

        System.out.println("相差的总天数："+between.toDays());
        System.out.println("相差的总小时数："+between.toHours());
        System.out.println("相差的总分钟数："+between.toMinutes());
        System.out.println("相差的总秒数："+between.getSeconds());
        System.out.println("相差的总毫秒数："+between.toMillis());
        System.out.println("相差的总纳秒数："+between.toNanos());
        System.out.println("不够一秒的纳秒数："+between.getNano());
    }

    @Test
    public void test03(){
        //Duration:用于计算两个“时间”间隔，以秒和纳秒为基准
        LocalTime localTime = LocalTime.now();
        System.out.println("111   " + localTime);
        LocalTime localTime1 = LocalTime.of(15, 23, 32);
        System.out.println("222   " + localTime1);
        //between():静态方法，返回 Duration 对象，表示两个时间的间隔
        Duration duration = Duration.between(localTime1, localTime);

        System.out.println(duration);
        System.out.println(duration.getSeconds());
        System.out.println(duration.getNano());
    }

}
