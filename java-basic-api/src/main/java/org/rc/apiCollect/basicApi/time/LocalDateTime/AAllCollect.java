package org.rc.apiCollect.basicApi.time.LocalDateTime;

public class AAllCollect {
    // 创建 LocalDateTime 实例      三个皆是静态方法
    //     now()：获取当前的日期时间。
    //     of(int year, int month, int dayOfMonth, int hour, int minute)：创建指定年月日时分的 LocalDateTime 实例。
    //     parse(String text)：从字符串解析为 LocalDateTime 实例（默认格式为 yyyy-MM-dd'T'HH:mm:ss）。



    // 获取日期和时间部分        全为实例方法
    //     getYear()：返回年份。
    //     getMonth()：返回月份（Month 枚举类型）。
    //     getDayOfMonth()：返回月份中的天数。
    //     getHour()：返回小时数（0-23）。
    //     getMinute()：返回分钟数（0-59）。
    //     getSecond()：返回秒数（0-59）。
    //     toLocalDate()：转换为 LocalDate。
    //     toLocalTime()：转换为 LocalTime。



    // 修改日期和时间
    //     withYear(int year)：设置年份。
    //     withMonth(int month)：设置月份。
    //     withDayOfMonth(int dayOfMonth)：设置月份中的天数。
    //     withHour(int hour)：设置小时数。
    //     withMinute(int minute)：设置分钟数。
    //     withSecond(int second)：设置秒数。
    //     plusYears(long yearsToToAdd)：增加年数。
    //     plusMonths(long monthsToToAdd)：增加月数。
    //     plusDays(long daysToToAdd)：增加天数。
    //     plusHours(long hoursToToAdd)：增加小时数。
    //     plusMinutes(long minutesToToAdd)：增加分钟数。
    //     plusSeconds(long secondsToToAdd)：增加秒数。
    //     minusYears(long yearsToSubtract)：减少年数。
    //     minusMonths(long monthsToSubtract)：减少月数。
    //     minusDays(long daysToSubtract)：减少天数。
    //     minusHours(long hoursToSubtract)：减少小时数。
    //     minusMinutes(long minutesToSubtract)：减少分钟数。
    //     minusSeconds(long secondsToSubtract)：减少秒数。



    // 格式化和解析
    // format(DateTimeFormatter formatter)：使用提供的格式化器将 LocalDateTime 转换为字符串。
    // parse(CharSequence text, DateTimeFormatter formatter)：使用提供的格式化器从字符串解析为 LocalDateTime 实例。



    // 比较两个 LocalDateTime
    // isEqual(LocalDateTime other)：判断两个 LocalDateTime 是否相等。
    // isBefore(LocalDateTime other)：判断是否在另一个 LocalDateTime 之前。
    // isAfter(LocalDateTime other)：判断是否在另一个 LocalDateTime 之后。
}
