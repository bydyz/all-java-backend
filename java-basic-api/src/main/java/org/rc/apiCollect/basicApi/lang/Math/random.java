package org.rc.apiCollect.basicApi.lang.Math;

public class random {
    public static void main(String[] args) {
        // 生成随机数：Math.random()，返回值类型 double ，值为 [0, 1);
        System.out.println(Math.random());

        System.out.println();
        double a = Math.random();
        System.out.println(a * 9);      // [0, 9);
        System.out.println((int) (a * 9 + 1));      // 1 - 9 的正整数
    }
}
