package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class equals {
    public static void main(String[] args) {
        String a = "omg";
        String b = "omg";
        String c = a;

        System.out.println(a.equals(b));        // true
        System.out.println(a.equals(c));        // true


        String d = "mmm";
        String e = "mmm";
        String f = d;

        System.out.println(d.equals(e));        // true
        System.out.println(d.equals(f));        // true
    }
}
