package org.rc.apiCollect.basicApi.lang.StringBuffer.littleExample;

public class efficiencyTest {
    public static void main(String[] args) {
        //初始设置
        long startTime = 0L;
        long endTime = 0L;
        String text = "";
        StringBuffer buffer = new StringBuffer();
        StringBuilder builder = new StringBuilder();

        //开始对比
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            buffer.append(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("StringBuffer 的执行结果：" + buffer);
        System.out.println("StringBuffer 的执行时间：" + (endTime - startTime));



        System.out.println();



        startTime = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            builder.append(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("StringBuilder 的执行结果：" + builder);
        System.out.println("StringBuilder 的执行时间：" + (endTime - startTime));



        System.out.println();



        startTime = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            text = text + i;
        }
        endTime = System.currentTimeMillis();
        System.out.println("String 的执行结果：" + text);
        System.out.println("String 的执行时间：" + (endTime - startTime));

    }
}
