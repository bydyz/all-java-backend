package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class substring {
    public static void main(String[] args) {
        // 返回一个新的字符串，它是此字符串的从 beginIndex 开始截取到最后的一个子字符串。
        // 下标7时第一个man后面的 ,
        String a = "Hi! man, how are you? are you a man";
        String b = a.substring(7);
        System.out.println(b);             // , how are you? are you a man
        System.out.println(a);             // Hi! man, how are you? are you a man     不影响原来的字符串


        String c = a.substring(7, 11);
        System.out.println(c);             // , ho      包前不包后
    }
}
