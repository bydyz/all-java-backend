package org.rc.apiCollect.basicApi.lang.String.littleExample;

public class Example3 {
    public static void main(String[] args) {
        String str = " a ";
        String newStr = myTrim(str);
        System.out.println("---" + newStr + "---");
    }

    // 模拟一个 trim 方法，去除字符串两端的空格。
    public static String myTrim(String str) {
        if (str != null) {
            int start = 0;  // 用于记录从前往后首次索引位置不是空格的位置的索引
            int end = str.length() - 1; // 用于记录从后往前首次索引位置不是空格的位置的索引
            while (start < end && str.charAt(start) == ' ') {
                start++;
            }
            while (start < end && str.charAt(end) == ' ') {
                end--;
            }
            if (str.charAt(start) == ' ') {
                return "";
            }
            return str.substring(start, end + 1);
        }
        return null;
    }
}
