package org.rc.algorithmicProblem;

public class HandleTrim {
    public String myTrim(String str) {
        if (str != null) {
            int start = 0;// 用于记录从前往后首次索引位置不是空格的位置的索引
            int end = str.length() - 1;// 用于记录从后往前首次索引位置不是空格的位置的索引
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


    public static void main(String[] args) {
        HandleTrim item = new HandleTrim();

        System.out.println("".length());   // 0
        System.out.println(item.myTrim(""));
        System.out.println(item.myTrim("  se v  se   "));

    }

}
