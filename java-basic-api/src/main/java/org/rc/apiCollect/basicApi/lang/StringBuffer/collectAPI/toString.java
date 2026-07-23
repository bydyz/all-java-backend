package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class toString {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning");
        System.out.println(buffer);

        String str1 = buffer.toString();
        System.out.println(buffer);
        System.out.println(str1);
    }
}
