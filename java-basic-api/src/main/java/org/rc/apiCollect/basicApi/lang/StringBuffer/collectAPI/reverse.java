package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class reverse {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning");
        System.out.println(buffer);

        // reverse 也是 包前不包后
        StringBuffer buffer2 = buffer.reverse();
        System.out.println(buffer);
        System.out.println(buffer2);

        // buffer.reverse 返回的 和 原字符串 等同
        System.out.println(buffer == buffer2);
        System.out.println(buffer.equals(buffer2));
    }
}
