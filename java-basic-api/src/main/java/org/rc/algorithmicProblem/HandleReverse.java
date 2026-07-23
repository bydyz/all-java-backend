package org.rc.algorithmicProblem;

import java.util.ArrayList;

public class HandleReverse {
    // 方式一：
    public String reverse1(String str, int start, int end) {    // start:2, end:5
        if (str != null) {
            char[] charArray = str.toCharArray();
            for (int i = start, j = end; i < j; i++, j--) {
                char temp = charArray[i];
                charArray[i] = charArray[j];
                charArray[j] = temp;
            }
            return new String(charArray);
        }
        return null;
    }

    // 方式二：
    public String reverse2(String str, int start, int end) {
        String newStr = str.substring(0, start);
        for (int i = end; i >= start; i--) {
            newStr += str.charAt(i);
        }
        newStr += str.substring(end + 1);
        return newStr;
    }

    // 方式三：推荐 （相较于方式二做的改进）
    public String reverse3(String str, int start, int end) {
        // ArrayList list = new ArrayList(80);
        StringBuffer s = new StringBuffer(str.length());
        s.append(str, 0, start);
        for (int i = end; i >= start; i--) {
            s.append(str.charAt(i));
        }
        s.append(str.substring(end + 1));
        return s.toString();
    }

}
