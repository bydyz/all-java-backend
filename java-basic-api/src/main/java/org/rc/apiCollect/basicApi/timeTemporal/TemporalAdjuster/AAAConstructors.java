package org.rc.apiCollect.basicApi.timeTemporal.TemporalAdjuster;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

public class AAAConstructors {
    public static void main(String[] args) {
        TemporalAdjuster item = new TemporalAdjuster() {
            @Override
            public Temporal adjustInto(Temporal temporal) {
                LocalDate date = (LocalDate) temporal;
                if (date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                    return date.plusDays(3);
                } else if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                    return date.plusDays(2);
                } else {
                    return date.plusDays(1);
                }
            }
        };

        LocalDate localDate = LocalDate.now().with(item);
        System.out.println("下一个工作日是：" + localDate);


        // 此段代码使用 TemporalAdjuster 接口来自定义日期调整逻辑。TemporalAdjuster 是一个函数式接口，用于调整 Temporal 对象（例如 LocalDate、LocalDateTime 等）。

        // 创建 TemporalAdjuster 实例：
        //     TemporalAdjuster item = new TemporalAdjuster() {...};: 创建 TemporalAdjuster 的实例 item，实例中包含了自定义的日期调整逻辑。

        // 实现 adjustInto 方法：
        //     adjustInto(Temporal temporal): 这是 TemporalAdjuster 接口中需要实现的方法。它接受一个 Temporal 对象作为参数，并返回一个调整后的 Temporal 对象。
        //     LocalDate date = (LocalDate) temporal;: 将传入的 Temporal 对象转换为 LocalDate 类型。这里使用了强制类型转换，因为 Temporal 是一个泛型接口。

        // 调整逻辑：
        //     根据 LocalDate 对象的星期几来决定如何调整日期：
        //         如果是 FRIDAY（星期五），则将日期向后调整 3 天（即下周一）。
        //         如果是 SATURDAY（星期六），则将日期向后调整 2 天（即下周日）。
        //         否则，将日期向后调整 1 天。

        // 使用 TemporalAdjuster 调整日期：
        //     LocalDate localDate = LocalDate.now().with(item);: 获取当前日期，并使用 with 方法结合 TemporalAdjuster 实例来调整日期。
    }
}
