package org.rc.algorithmicProblem;

import java.io.PrintStream;

public class DemoTwo {
    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        // methodOne(a, b);
        methodTwo(a, b);
        System.out.println("a=" + a);
        System.out.println("b=" + b);
    }
    // 在不改变原本题目的前提下，如何写这个函数才能让main函数中输出a=100，b=200？


    // 方法一：偷天换日
    public static void methodOne(int a, int b) {
        a = a * 10;
        b = b * 20;
        System.out.println("a=" + a);
        System.out.println("b=" + b);
        // exit()方法属于System类，System这个类是在java.lang包中，我们从API文档中找到这个方法（java.lang包在系统运行中自动加载，不需要通过import这个方法，System这个类是在java.lang包中，java.lang包在系统运行中自动加载，不需要通过import语句导入，所以可以直接调用该方法）。
        System.exit(0);
    }


    //法二：利用PrintStream 类重写
    public static void methodTwo(int a, int b) {
        PrintStream ps = new PrintStream(System.out) {
            @Override
            public void println(String x) {
                if ("a=10".equals(x)) {
                    x = "a=100";
                } else if ("b=20".equals(x)) {
                    x = "b=200";
                }
                super.println(x);
            }
        };
        System.setOut(ps);
    }
}


