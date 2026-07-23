package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class compareToIgnoreCase {
    public static void main(String[] args) {
        String str1 = "Hello";
        String str2 = "World";
        String str3 = "hello";


        int result1 = str1.compareToIgnoreCase(str2); // 返回 -15
        System.out.println(result1);

        int result2 = str1.compareToIgnoreCase(str3); // 返回 0
        System.out.println(result2);

        int result3 = str3.compareToIgnoreCase(str1); // 返回 0
        System.out.println(result3);

        int result4 = str1.compareToIgnoreCase(str1); // 返回 0
        System.out.println(result4);
    }
}
