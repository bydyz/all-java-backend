package org.rc.apiCollect.basicApi.lang.String.collectAPI;

import java.util.function.Function;

// transform 方法允许你对字符串应用一个函数，并返回一个新的字符串。这是一个非常灵活的方法，可以用于各种字符串转换操作。
public class transform {
    public static void main(String[] args) {
        String originalText = "hello world";
        String transformedText = originalText.transform(Function.identity());

        // 使用 Function 来转换为大写
        String upperCaseText = originalText.transform(String::toUpperCase);

        System.out.println("Original Text: " + originalText);
        System.out.println("Transformed Text (identity): " + transformedText);
        System.out.println("Upper Case Text: " + upperCaseText);
    }
}
