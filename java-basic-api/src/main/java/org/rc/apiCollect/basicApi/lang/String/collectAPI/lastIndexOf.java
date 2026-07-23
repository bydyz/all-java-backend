package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class lastIndexOf {
    public static void main(String[] args) {
        // 从后往前找当前字符串中 xx 第一次出现的下标
        String a = "Hi! man, how are you? are you a man? ha? please answer my question.";

        int b = a.lastIndexOf("man");
        System.out.println(b);          // 32

        int c = a.lastIndexOf('h');
        System.out.println(c);          // 37

        int d = a.lastIndexOf("a");
        System.out.println(d);          // 48

        int e = a.lastIndexOf("q");
        System.out.println(e);          // 58


        // 从规定的下表处，从后往前找当前字符串中 xx 第一次出现的下标
        int f = a.lastIndexOf("man", 35);
        System.out.println(a.charAt(32));
        System.out.println(a.charAt(33));
        System.out.println(a.charAt(34));
        System.out.println(a.charAt(35));
        System.out.println(f);          // 4
    }
}
