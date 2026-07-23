package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class append {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning");
        // 调用stringBuilder的append（）方法，每次都会新生成一个对象，
        StringBuffer buffer2 = buffer.append("! me too!");
        System.out.println(buffer);                     // I hate learning! me too!
        System.out.println(buffer2);                    // I hate learning! me too!

        // buffer.append 返回的 和 原字符串 等同
        System.out.println(buffer == buffer2);          // true
        System.out.println(buffer.equals(buffer2));     // true
    }
}
