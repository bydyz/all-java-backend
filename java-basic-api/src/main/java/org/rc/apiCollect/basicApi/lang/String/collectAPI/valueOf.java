package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class valueOf {
    public static void main(String[] args) {
        char[] a = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };

        String b = String.valueOf(a);
        System.out.println(b);

        String c = String.valueOf(a, 2, 6);
        System.out.println(c);
    }
}
