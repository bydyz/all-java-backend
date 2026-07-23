package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class lastIndexOf {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning. Do you hate too?");
        System.out.println(buffer);

        System.out.println(buffer.lastIndexOf("hate"));
        System.out.println(buffer);



        // 在当前字符序列[0k, fromIndex]中查询 str 的第一次出现下标
        System.out.println(buffer.lastIndexOf("hate", 10));
        System.out.println(buffer);

        System.out.println(buffer.lastIndexOf("hate", 28));
        System.out.println(buffer);
    }
}
