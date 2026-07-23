package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class concat {
    public static void main(String[] args) {
        String a = "Hi, i like you, ";
        String b = a.concat("can you be my girlfriend.");

        // concat()  不会影响原有的字符串
        System.out.println(a);
        System.out.println(b);
    }
}
