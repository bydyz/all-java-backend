package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class trim {
    public static void main(String[] args) {
        // 去掉字符串前后空白符
        String a = "  a bd vb  ";
        String b = a.trim();
        System.out.println(b);          // a bd vb
    }
}
