package org.rc.otherKnowledge.class_usage.enum_usage;

import java.util.Scanner;

public class TestEnumMethod {
    public static void main(String[] args) {
        //values()
        Week[] values = Week.values();
        for (int i = 0; i < values.length; i++) {
            //ordinal()、name()
            System.out.println((values[i].ordinal() + 1) + "->" + values
                    [i].name());
        }
        System.out.println("------------------------");
        Scanner input = new Scanner(System.in);
        System.out.print("请输入星期值：");
        int weekValue = input.nextInt();
        Week week = values[weekValue - 1];
        //toString()
        System.out.println(week);
        System.out.print("请输入星期名：");
        String weekName = input.next();
        //valueOf()
        week = Week.valueOf(weekName);
        System.out.println(week);
        input.close();
    }

}
