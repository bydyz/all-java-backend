package org.rc.apiCollect.basicApi.lang.String;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

public class DUseConstructors {
    @Test
    public void test01() {
        String a1;
        String a2 = "";
    }

    @Test
    public void test02() {
        String a1 = "aaa";
        String a2 = "aaa";
    }

    @Test
    public void test03() {
        // 通过当前参数中的字符数组来构造新的 String。
        char[] a = { 'a', 'b', 'm', 'j' };
        String b = new String(a);
        System.out.println(b);
    }

    @Test
    public void test04() {
        // 通过字符数组的一部分来构造新的 String。
        char[] a = { 'a', 'b', 'm', 'j', '1', 'p', '6' };
        String b = new String(a, 1, 3);
        System.out.println(b);
    }

    @Test
    public void test05() throws UnsupportedEncodingException {
        byte[] a = { 97, 98, 99 };
        String b = new String(a, "UTF-8");
        System.out.println(b);
    }

    public static void main(String[] args) {
        byte[] a = { 97, 98, 99 };
        try {
            // String b = new String(a, "UTF-8");
            // String b = new String(a, "ISO-8859-1");
            String b = new String(a, StandardCharsets.ISO_8859_1);
            System.out.println(b);
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
            // 或者其他适当的错误处理
        }
    }
}
