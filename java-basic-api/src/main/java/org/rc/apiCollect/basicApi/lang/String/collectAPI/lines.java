package org.rc.apiCollect.basicApi.lang.String.collectAPI;

import java.util.stream.Collectors;

// lines 方法返回一个流，其中包含由该字符串中的行分隔符分隔的每一行作为单独的元素。这对于解析多行文本非常有用。
public class lines {
    public static void main(String[] args) {
        String multiLineText = "First line\nSecond line\nThird line";
        long lineCount = multiLineText.lines().count();
        String joinedLines = multiLineText.lines().collect(Collectors.joining(" | "));

        System.out.println("Number of lines: " + lineCount); // 输出: Number of lines: 3
        System.out.println("Joined lines: " + joinedLines); // 输出: Joined lines: First line | Second line | Third line
    }
}
