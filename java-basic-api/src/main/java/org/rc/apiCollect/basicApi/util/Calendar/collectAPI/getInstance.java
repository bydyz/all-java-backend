package org.rc.apiCollect.basicApi.util.Calendar.collectAPI;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.TimeZone;

public class getInstance {
    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        System.out.println(c);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        System.out.println(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);

        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayOfMonth);
        System.out.println(dayOfWeek);
    }

    @Test
    public void test01() {
        // 获取特定时区的 TimeZone 对象
        TimeZone t = TimeZone.getTimeZone("America/Los_Angeles");
        // 使用时区创建 Calendar 对象
        Calendar c = Calendar.getInstance(t);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        System.out.println(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);

    }
}
