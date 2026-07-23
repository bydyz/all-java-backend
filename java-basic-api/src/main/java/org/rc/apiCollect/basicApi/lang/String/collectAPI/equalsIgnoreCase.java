package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class equalsIgnoreCase {
    public static void main(String[] args) {
        String a = "mmm";
        String b = "MMM";
        String c = "Mmm";

        System.out.println(a.equalsIgnoreCase(b));      // true
        System.out.println(a.equalsIgnoreCase(c));      // true
    }
}
