package org.rc.apiCollect.basicApi.lang.Math;

public class ceil {
    public static void main(String[] args) {
        // public static double abs(double a) ：返回 double 值的绝对值。

        double d1 = Math.ceil(-5.2); //d1 的值为 -5.0
        double d2 = Math.ceil(5.2); //d2 的值为 6.0
        double d3 = Math.ceil(5); //d3 的值为 5.0

        System.out.println(d1);
        System.out.println(d2);
        System.out.println(d3);
    }
}
