package org.rc.otherKnowledge.switch_usage;

public class demo002 {
    public static void main(String[] args) {
        int day = 1;

        // 传统写法：每个 case 要 break
        switch (day) {
            case 1:
                System.out.println("周一");
                break;
            case 2:
                System.out.println("周二");
                break;
        }

        // JDK 14+ 箭头语法：无穿透、无 break
        switch (day) {
            case 1 -> System.out.println("周一");
            case 2 -> System.out.println("周二");
            default -> System.out.println("其他");
        }
    }
}
