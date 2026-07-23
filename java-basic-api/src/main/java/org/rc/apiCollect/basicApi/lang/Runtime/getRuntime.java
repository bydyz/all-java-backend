package org.rc.apiCollect.basicApi.lang.Runtime;

public class getRuntime {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();

        long initialMemory = runtime.totalMemory();     //获取虚拟机初始化时堆内存总量
        long maxMemory = runtime.maxMemory();       //获取虚拟机最大堆内存总量

        String str = "";
        //模拟占用内存
        for (int i = 0; i < 10000; i++) {
            str += i;
        }

        long freeMemory = runtime.freeMemory(); //获取空闲堆内存总量
        // 1 MB = 1024 * 1024 bytes
        // 返回 Java 虚拟机中初始化时的内存总量。此方法返回的值可能随时间的推移而变化，这取决于主机环境。默认为物理电脑内存的 1/64。
        System.out.println("总内存：" + initialMemory / 1024 / 1024 * 64 + "MB");
        // 返回 Java 虚拟机中最大程度能使用的内存总量。默认为物理电脑内存的 1/4。
        System.out.println("总内存：" + maxMemory / 1024 / 1024 * 4 + "MB");
        // Java 虚拟机中的空闲内存量
        System.out.println("空闲内存：" + freeMemory / 1024 / 1024 + "MB") ;

        System.out.println("已用内存：" + (initialMemory-freeMemory) / 1024 / 1024 + "MB");

    }
}
