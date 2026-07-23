package org.rc.apiCollect.basicApi.lang.String.littleExample;

public class Example4 {
    public static void main(String[] args) {
        Example4 a = new Example4();

        System.out.println(a.reverse1("abcdefg", 2, 3));
        System.out.println(a.reverse2("abcdefg", 2, 3));
        System.out.println(a.reverse3("abcdefg", 2, 3));
    }
    // 将字符串中指定部分进行反转。

    // 方式一：
    public String reverse1(String str, int start, int end) {// start:2,end:5
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
        String newStr = str.substring(0, start);    // ab
        for (int i = end; i >= start; i--) {
            newStr += str.charAt(i);
        } // abfedc
        newStr += str.substring(end + 1);
        return newStr;
    }
    // 方式三：推荐 （相较于方式二做的改进）
    public String reverse3(String str, int start, int end) {    // ArrayList list = new ArrayList(80);
        StringBuffer s = new StringBuffer(str.length());
        s.append(str.substring(0, start));// ab
        for (int i = end; i >= start; i--) {
            s.append(str.charAt(i));
        }
        s.append(str.substring(end + 1));
        return s.toString();
    }

}
