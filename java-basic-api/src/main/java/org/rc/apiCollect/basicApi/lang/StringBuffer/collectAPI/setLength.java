package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class setLength {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning");
        System.out.println(buffer);
        System.out.println(buffer.length());

        buffer.setLength(800);
        System.out.println(buffer);
        System.out.println(buffer.length());
    }
}
