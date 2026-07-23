package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class charAt {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning!");
        System.out.println(buffer);

        // charAt 不修改原字符串
        char char1 = buffer.charAt(buffer.length() - 1);
        System.out.println(char1);
        System.out.println(buffer);
    }
}
