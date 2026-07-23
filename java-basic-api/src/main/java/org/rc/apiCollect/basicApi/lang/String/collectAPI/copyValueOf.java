package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class copyValueOf {
    public static void main(String[] args) {
        char[] data = {'h','e','l','l','o','j','a','v','a'};
        String s1 = String.copyValueOf(data);
        String s2 = String.copyValueOf(data,0,5);
        int num = 123456;
        String s3 = String.valueOf(num);

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);

    }
}
