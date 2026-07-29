package org.rc.apiCollect.basicApi.time.Period;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class between {
    public static void main(String[] args) {
        LocalDate t1 = LocalDate.now();
        System.out.println("111   " + t1);
        LocalDate t2 = LocalDate.of(2018, 12, 31);
        System.out.println("222   " + t2);
        // t2 - t1
        Period between = Period.between(t1, t2);
        System.out.println(between);

        System.out.println("相差的年数：" + between.getYears());
        System.out.println("相差的月数：" + between.getMonths());
        System.out.println("相差的天数：" + between.getDays());
        System.out.println("相差的总月份数：" + between.toTotalMonths());
    }

    @Test
    public void test4(){
        //Period:用于计算两个“日期”间隔，以年、月、日衡量
        LocalDate localDate = LocalDate.now();
        System.out.println("111   " + localDate);
        LocalDate localDate1 = LocalDate.of(2028, 3, 18);
        System.out.println("222   " + localDate1);
        Period period = Period.between(localDate, localDate1);

        System.out.println(period);

        Period period1 = period.withYears(2);
        // withYears 方法
        // withYears 方法用于修改 Period 对象的年部分。如果需要修改月或日部分，可以使用 withMonths 或 withDays 方法。
        System.out.println(period1);

    }
}
