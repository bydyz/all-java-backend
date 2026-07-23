package org.rc.apiCollect.basicApi.text.SimpleDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class AConstructors {
    // java.text.SimpleDateFormat 类是一个不与语言环境有关的方式来格式化和解析日期的具体类。
    // 可以进行格式化：日期 --> 文本
    // 可以进行解析：文本 --> 日期
    public static void main(String[] args) {
        // 创建 Date 对象
        Date currentDate = new Date();






        System.out.println();





        // 默认构造器
        SimpleDateFormat dateFormat1 = new SimpleDateFormat();
        System.out.println(dateFormat1.format(currentDate));






        System.out.println();





        

        // 创建 SimpleDateFormat 对象，定义日期时间格式

        // SimpleDateFormat 使用特定的模式来格式化日期和时间。以下是一些常用的模式符号：
        //     y：年
        //     M：月（例如，M 表示 1 到 12）
        //     d：月中的日（例如，d 表示 1 到 31）
        //     H：一天中的小时数（00 到 23）
        //     m：小时中的分钟数（00 到 59）
        //     s：分钟中的秒数（00 到 59）
        //     S：毫秒数（000 到 999）
        //     E：星期的缩写（例如，Sun、Mon）
        //     a：上午或下午（例如，AM、PM）
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 格式化日期
        String formattedDate = dateFormat2.format(currentDate);

        // 打印格式化后的日期
        System.out.println("Formatted Date: " + formattedDate);






        System.out.println();






        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH 时 mm 分 ss 秒 SSS 毫秒 E Z");
        System.out.println(dateFormat3.format(currentDate));





        System.out.println();






        SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 定义一个日期字符串
        String dateString = "2023-04-08 10:30:00";

        // 解析字符串为 Date 对象
        Date date = null;
        try {
            date = dateFormat4.parse(dateString);
            System.out.println(dateFormat4.format(date));
            System.out.println("Parsed Date: " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // SimpleDateFormat 是线程不安全的，因此如果需要在多线程环境中使用，应当注意线程同步问题。
        // 格式化和解析日期时，SimpleDateFormat 依赖于默认或指定的 Locale。如果需要特定的语言环境，可以创建 SimpleDateFormat 时传入相应的 Locale 对象。
        // 时区对日期和时间的显示有影响，可以通过 setTimeZone 方法指定时区。
        // 尽管 SimpleDateFormat 在旧代码中很常见，但在新项目中，推荐使用 Java 8 引入的 java.time 包中的类，因为它们提供了更好的 API 设计和时区处理。
    }
}
