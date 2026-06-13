package org.rc;

public class NumberCalculate {
    public static void main(String[] args) {
        System.out.println(0.1 + 0.2);

        float ff1 = 123123123f;
        float ff2 = ff1 + 1;
        // 这样写也可，java用双引号    javac -encoding utf-8 a.java
        System.out.print(ff1 + "\n");
        System.out.println(ff1);
        System.out.print(ff1);
        System.out.print("\n");
        System.out.println(ff2);
        System.out.println(ff1 == ff2);
    }
}
