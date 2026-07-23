package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class setCharAt {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning!");
        System.out.println(buffer);

        // setCharAt 没有返回值
        buffer.setCharAt(buffer.length() - 1, '?');
        System.out.println(buffer);
    }
}
