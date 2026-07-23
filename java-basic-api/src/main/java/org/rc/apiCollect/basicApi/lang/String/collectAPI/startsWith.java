package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class startsWith {
    public static void main(String[] args) {
        String a = "Hi! man, how are you? are you a man? ha? please answer my question.";

        // startsWith()     接受的传参需要是 String
        Boolean c = a.startsWith("H");
        Boolean d = a.startsWith("Hi");

        System.out.println(c);          // true
        System.out.println(d);          // true



        Boolean e = a.startsWith(" man", 4);          // false
        Boolean f = a.startsWith("man", 4);          // true
        System.out.println(e);
        System.out.println(f);
    }
}
