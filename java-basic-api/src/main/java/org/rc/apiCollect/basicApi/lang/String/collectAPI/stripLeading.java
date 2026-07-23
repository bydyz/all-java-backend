package org.rc.apiCollect.basicApi.lang.String.collectAPI;

// stripLeading 方法用于去除字符串开头的空白字符。
public class stripLeading {
    public static void main(String[] args) {
        String textWithLeadingSpaces = "   Hello, World!";
        String strippedLeading = textWithLeadingSpaces.stripLeading();

        System.out.println("Original: '" + textWithLeadingSpaces + "'");
        System.out.println("Stripped Leading: '" + strippedLeading + "'");
    }
}
