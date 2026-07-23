package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class replace {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning");
        System.out.println(buffer);

        // 替换[start,end)范围的字符序列为 str
        StringBuffer buffer2 = buffer.replace(2, 6, "like");
        System.out.println(buffer);
        System.out.println(buffer2);

        // buffer.replace 返回的 和 原字符串 等同
        System.out.println(buffer == buffer2);
        System.out.println(buffer.equals(buffer2));
    }
}
