package org.rc.apiCollect.basicApi.lang.String.collectAPI;

import org.junit.jupiter.api.Test;

public class split {
    // 方法签名   public String[] split(String regex)
    // 在 Java 中，split 方法属于 String 类，用于根据指定的分隔符来切分字符串，并返回一个字符串数组。这个方法非常有用于处理字符串，尤其是当你需要解析以特定分隔符分隔的字符串时。

    // split 方法使用正则表达式作为分隔符，这意味着你可以使用复杂的匹配模式来切分字符串。例如，你可以使用 \\s+ 来切分由一个或多个空白字符分隔的字符串。
    // 如果分隔符是一个空字符串 ""，那么输入字符串将被切分成单个字符。
    // 如果原始字符串是 null，那么 split 方法将抛出 NullPointerException。
    // 如果原始字符串是空字符串 ""，那么返回的数组也将是空的，即只包含一个空字符串元素。

    // split 方法还可以接受一个额外的 limit 参数，用于限制返回数组的最大长度，以及如何继续剩余的输入。这个重载版本的方法签名如下：
    //     public String[] split(String regex, int limit)
    //     limit: 指定返回数组的最大长度。如果切分后的子字符串数量超过这个限制，剩余的子字符串将根据正则表达式的贪婪匹配被合并到数组中的最后一个元素。

    public static void main(String[] args) {
        String text = "apple,orange,banana,grape";
        String[] fruits = text.split(",");      // 使用逗号作为分隔符
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
    }


    // split 方法还可以接受一个额外的 limit 参数，用于限制返回数组的最大长度，以及如何继续剩余的输入。这个重载版本的方法签名如下：
    //     public String[] split(String regex, int limit)
    //     limit: 指定返回数组的最大长度。如果切分后的子字符串数量超过这个限制，剩余的子字符串将根据正则表达式的贪婪匹配被合并到数组中的最后一个元素。
    @Test
    public void test01() {
        String text = "a,b,c,d,e,f,g";
        String[] limitedSplit = text.split(",", 3); // 限制数组长度为 3
        for (String part : limitedSplit) {
            System.out.println(part);
        }
    }
    // 在上面的例子中，由于设置了 limit 为 3，所以返回的数组只包含 3 个元素，剩下的部分 "d,e,f,g" 作为数组的最后一个元素。
}
