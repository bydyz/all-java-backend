package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class insert {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning");
        System.out.println(buffer);

        StringBuffer buffer2 = buffer.insert(0, "hi! ");
        System.out.println(buffer);
        System.out.println(buffer2);

        // buffer.insert 返回的 和 原字符串 等同
        System.out.println(buffer == buffer2);
        System.out.println(buffer.equals(buffer2));

        buffer.insert(buffer.length() - 1, '?');
        System.out.println(buffer);

        buffer.insert(buffer.length(), '?');
        System.out.println(buffer);
    }
}
