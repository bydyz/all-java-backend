package org.rc.apiCollect.basicApi.lang.String.collectAPI;

// strip 方法用于去除字符串两端的空白字符。它类似于 trim 方法，但更加严格，因为它会移除所有的 Unicode 空白字符，而不仅仅是 ASCII 空白字符。
public class strip {
    public static void main(String[] args) {
        String textWithSpaces = "   Hello, World!   ";
        String strippedText = textWithSpaces.strip();

        System.out.println("Original: '" + textWithSpaces + "'");
        System.out.println("Stripped: '" + strippedText + "'");
    }
}
