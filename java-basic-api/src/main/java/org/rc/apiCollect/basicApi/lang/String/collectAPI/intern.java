package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class intern {
    public static void main(String[] args) {
        // 结果在常量池中共享
        String a = "abc";
        String b = a + "mn";
        String c = "abc" + "mn";
        String d = (a + "mn").intern();

        System.out.println(b == c);     // false
        System.out.println(b == d);     // false
        System.out.println(c == d);     // true
    }
}
