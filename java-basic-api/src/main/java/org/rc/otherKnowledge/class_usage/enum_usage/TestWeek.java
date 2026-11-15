package org.rc.otherKnowledge.class_usage.enum_usage;

public class TestWeek {
    public static void main(String[] args) {
        Week week = Week.MONDAY;
        System.out.println(week);
        switch (week) {
            case MONDAY:
                System.out.println("怀念周末，困意很浓");
                break;
            case TUESDAY:
                System.out.println("进入学习状态");
                break;
            case WEDNESDAY:
                System.out.println("死撑");
                break;
            case THURSDAY:
                System.out.println("小放松");
                break;
            case FRIDAY:
                System.out.println("又信心满满");
                break;
            case SATURDAY:
                System.out.println("开始盼周末，无心学习");
                break;
            case SUNDAY:
                System.out.println("一觉到下午");
                break;
        }
    }
}
