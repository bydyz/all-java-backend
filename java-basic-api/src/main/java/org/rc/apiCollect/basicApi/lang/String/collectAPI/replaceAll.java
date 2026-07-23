package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class replaceAll {
    public static void main(String[] args) {
        String a = "hello244world.java;A887";

        String b = a.replaceAll("[a-z]", "");
        System.out.println(a);
        System.out.println(b);

        String c = a.replaceAll("[a-zA-Z]", "");
        System.out.println(a);
        System.out.println(c);

        String d = a.replaceAll("[^a-z]", "");
        System.out.println(a);
        System.out.println(d);



        System.out.println();

        String str2 = "12hello34world5java7891mysql456";
        //把字符串中的数字替换成,，如果结果中开头和结尾有，的话去掉
        String string1 = str2.replaceAll("\\d+", ",");      // 换 数字 为 ,
        System.out.println(string1);
        String string2 = str2.replaceAll("^,|,$", "");      // 换 开头或结尾的, 为
        System.out.println(string2);
        String string3 = str2.replaceAll("\\d+", ",").replaceAll("^,|,$", "");
        System.out.println(string3);    }
}
