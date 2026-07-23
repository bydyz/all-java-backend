package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class contains {
    public static void main(String[] args) {
        String a = "Hi! man, how are you?";

        Boolean b = a.contains("man");
        System.out.println(b);

        Boolean c = a.contains("man6");
        System.out.println(c);
    }
}
