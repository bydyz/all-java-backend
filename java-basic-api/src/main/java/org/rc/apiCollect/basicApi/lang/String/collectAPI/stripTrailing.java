package org.rc.apiCollect.basicApi.lang.String.collectAPI;

// stripTrailing 方法用于去除字符串末尾的空白字符。
public class stripTrailing {
    public static void main(String[] args) {
        String textWithTrailingSpaces = "Hello, World!   ";
        String strippedTrailing = textWithTrailingSpaces.stripTrailing();

        System.out.println("Original: '" + textWithTrailingSpaces + "'");
        System.out.println("Stripped Trailing: '" + strippedTrailing + "'");
    }
}
