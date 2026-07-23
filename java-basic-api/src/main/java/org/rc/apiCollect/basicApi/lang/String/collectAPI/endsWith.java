package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class endsWith {
    public static void main(String[] args) {
        String a = "Hi! man, how are you? are you a man? ha? please answer my question.";

        Boolean c = a.endsWith(".");
        Boolean d = a.endsWith("n.");

        System.out.println(c);          // true
        System.out.println(d);          // true
    }
}
