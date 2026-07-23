package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class indexOf {
    public static void main(String[] args) {
        String a = "Hi! man, how are you? are you a man";

        int b = a.indexOf("man");
        System.out.println(b);          // 4

        int c = a.indexOf('h');
        System.out.println(c);          // 9

        int d = a.indexOf("y");
        System.out.println(d);          // 17

        int e = a.indexOf("q");
        System.out.println(e);          // -1


        // 返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始搜索
        int f = a.indexOf("y", 13);
        System.out.println(a.charAt(13));
        System.out.println(f);          // 17

        int g = a.indexOf("y", 20);
        System.out.println(a.charAt(20));
        System.out.println(g);          // 26
    }
}
