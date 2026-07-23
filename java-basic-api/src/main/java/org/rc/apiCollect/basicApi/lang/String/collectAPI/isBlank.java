package org.rc.apiCollect.basicApi.lang.String.collectAPI;

// isBlank 方法用于检查一个字符串是否为空白字符串（即仅包含空白字符或完全为空）。
// 这比 isEmpty 更加严格，因为它不仅考虑了空字符串，还包括只包含空格、制表符等空白字符的情况。
public class isBlank {
    public static void main(String[] args) {
        String empty = "";
        String spaces = "   ";
        String text = "Hello, World!";
        String a = "\n";
        String b = " a ";

        System.out.println(empty.isBlank()); // true
        System.out.println(spaces.isBlank()); // true
        System.out.println(text.isBlank()); // false
        System.out.println(a.isBlank()); // true
        System.out.println(b.isBlank()); // false
    }
}
