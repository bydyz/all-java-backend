package org.rc.apiCollect.basicApi.util.Calendar.collectAPI;

import java.util.Calendar;
// 在 Java 中，Calendar 类是一个抽象类，它为日期时间的字段（如年、月、日、时、分、秒等）提供了一些操作方法，并且允许你在这些字段之间进行转换。Calendar 类本身不存储日期时间值，而是通过其子类（如 GregorianCalendar）来实例化和操作日期时间值。

import java.util.Date;

public class getTime {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();

        // 从一个 Calendar 对象中获取 Date 对象       当前时间点
        Date date = calendar.getTime();
        System.out.println(date);

        // 使用给定的 Date 设置此 Calendar 的时间
        date = new Date(234234235235L);
        System.out.println(date);

        calendar.setTime(date);
        // Calendar.setTime(Date date) 方法不会改变传入的 Date 对象的值。这个方法的作用是将 Calendar 对象的内部状态设置为与传入的 Date 对象相同的时间。
        //     不变性：Date 对象是不可变的。一旦一个 Date 对象被创建，它所表示的时间就不能被更改。Calendar.setTime(Date date) 方法不会对原始的 Date 对象有任何影响。
        //     时间设置：setTime() 方法设置 Calendar 对象的当前时间。它是将 Calendar 与传入的 Date 对象同步，而不是相反。
        //     独立对象：Date 和 Calendar 是两个独立的对象。Calendar 对象可以独立于 Date 对象进行修改，例如改变年、月、日等字段，这些修改不会反映到 Date 对象上。
        //     方法行为：setTime() 方法只是将 Calendar 的时间设置为与 Date 对象相同的值。它不会执行任何修改 Date 对象的操作。
        //     后续操作：在 Calendar 对象上进行的任何后续操作（如增加天数或设置特定的字段值）都不会影响原始的 Date 对象，因为 Date 是不可变的。

        // 当你使用 Calendar 类的 setTime() 方法时，你实际上是在告诉 Calendar 对象将某个特定的日期时间值设置为其当前时间。这个日期时间值通常是 Date 对象表示的。

        // setTime() 方法的作用和意义：
        //     设置 Calendar 的当前时间：setTime() 方法将 Calendar 对象的当前时间设置为传入的 Date 对象所表示的时间。
        //     时间的重新解释：由于 Calendar 可以处理不同的日历系统（如公历、日本日历、泰国日历等），setTime() 方法还可以将 Date 对象的时间重新解释为 Calendar 对象所采用的特定日历系统。
        //     字段的设置：一旦 Calendar 对象的时间被设置，你就可以使用 Calendar 的方法来访问和修改日期时间的各个字段，例如获取年、月、日等。
        //     对日期时间进行操作：使用 setTime() 方法设置时间后，你可以对日期时间进行增加、减少或计算等操作。

        // setTime() 方法接受一个 Date 对象作为参数，如果传入 null，则 Calendar 对象的时区和字段值将被清空。
        // Calendar 类是线程不安全的，因此如果需要在多线程环境中使用，应当注意线程同步问题。
        // 从 Java 8 开始，推荐使用 java.time 包中的类，因为它们提供了更好的 API 设计和时区处理。
        calendar.set(Calendar.DAY_OF_MONTH, 8);
        System.out.println(date);
        System.out.println("当前时间日设置为 8 后,时间是: " + calendar.getTime());

        calendar.add(Calendar.HOUR, 2);
        System.out.println("当前时间加 2 小时后,时间是: " + calendar.getTime());

        calendar.add(Calendar.MONTH, -2);
        System.out.println("当前日期减 2 个月后,时间是: " + calendar.getTime());
    }
}

