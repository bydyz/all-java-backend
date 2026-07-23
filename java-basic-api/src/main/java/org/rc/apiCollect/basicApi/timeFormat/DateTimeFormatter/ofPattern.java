package org.rc.apiCollect.basicApi.timeFormat.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class ofPattern {
    public static void main(String[] args) {
        // ofPattern(String pattern)  静态方法，返回一个指定字符串格式的DateTimeFormatter
        // 自定义的方式进行格式化（关注、重点）
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


        // format(TemporalAccessor t)  格式化一个日期、时间，返回字符串
        //格式化
        String strDateTime = dateTimeFormatter.format(LocalDateTime.now());
        System.out.println(strDateTime); // 2022/12/04 21:05:42


        // parse(CharSequence text) 将指定格式的字符序列解析为一个日期、时间
        //解析
        TemporalAccessor accessor = dateTimeFormatter.parse("2022/12/04 21:05:42");
        LocalDateTime localDateTime = LocalDateTime.from(accessor);
        System.out.println(localDateTime);  //2022-12-04T21:05:42
    }
}
