package org.rc.otherKnowledge.break_usage;

// 嵌套循环：break 只跳出最近一层
public class demo001 {
    public static void main(String[] args) {
        // 跳出 for 循环
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i == 2 && j == 2) {
                    System.out.println("break 在内层 i=" + i + ", j=" + j);
                    break;                 // ← 只跳出内层 for，外层继续
                }
                System.out.println("i=" + i + ", j=" + j);
            }
        }

        // 跳出 while / do-while
        int n = 0;
        while (true) {
            n++;
            if (n == 5) {
                System.out.println("n 到 5 了，退出");   // 输出
                break;                                   // 跳出 while
            }
        }

        int m = 0;
        do {
            m++;
            if (m > 3) break;      // 跳出 do-while
            System.out.println("m = " + m);   // 1 2 3
        } while (true);

        // 跳出 switch
        int day = 2;
        switch (day) {
            case 1:
                System.out.println("周一");
                break;                     // 跳出 switch
            case 2:
                System.out.println("周二");   // ← 输出
                break;                     // ← 执行到这里，跳出 switch
            case 3:
                System.out.println("周三");
                break;
            default:
                System.out.println("其他");
        }
        System.out.println("switch 之后继续执行");

        // break 只跳出 switch，不跳出循环
        for (int i = 1; i <= 3; i++) {
            switch (i) {
                case 2:
                    System.out.println("遇到 2，跳出 switch");
                    break;                  // ← 只跳出 switch，不跳出 for
                default:
                    System.out.println("i = " + i);
            }
            System.out.println("i=" + i + " 这一轮结束");
        }
    }
}
