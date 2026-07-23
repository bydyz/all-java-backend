package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class replace {
    public static void main(String[] args) {
        String a = "Hi! man, how are you? are you a man? ha? please answer my question.";

        String b = a.replace("man", "woman");           // 全替换
        System.out.println(a);
        System.out.println(b);

        String c = a.replace('?', '#');           // 全替换
        System.out.println(a);
        System.out.println(c);    }
}
