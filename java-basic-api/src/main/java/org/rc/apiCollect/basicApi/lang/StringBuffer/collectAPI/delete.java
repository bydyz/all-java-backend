package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class delete {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning");
        System.out.println(buffer);

        // delete 也是 包前不包后
        StringBuffer buffer2 = buffer.delete(1, 6);
        System.out.println(buffer);
        System.out.println(buffer2);

        // buffer.delete 返回的 和 原字符串 等同
        System.out.println(buffer == buffer2);
        System.out.println(buffer.equals(buffer2));
    }
}
