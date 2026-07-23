package org.rc.apiCollect.basicApi.lang.Math;

public class round {
    public static void main(String[] args) {
        // 传入 double 得到 long
        double a = 100.86;
        System.out.println(Math.round(a));


        // 传入 float 得到 int
        float b = 100.26321F;
        System.out.println(Math.round(b));


        // Math.round 方法的工作原理如下：
        //     它首先将参数加上 0.5。
        //     然后它对结果进行向下取整（即，转换为最接近的整数值，但不大于该值）。
        //     最后，根据输入的类型返回相应的整数类型 (int 或 long)。
    }
}
