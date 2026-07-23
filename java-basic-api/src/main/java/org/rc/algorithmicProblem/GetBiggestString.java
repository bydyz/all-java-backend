package org.rc.algorithmicProblem;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class GetBiggestString {
    // 返回一个最大长度的相同子串
    public String getMaxSameSubString(String str1, String str2) {
        if (str1 != null && str2 != null) {
            String maxStr = (str1.length() > str2.length()) ? str1 : str2;
            String minStr = (str1.length() > str2.length()) ? str2 : str1;
            int len = minStr.length();
            for (int i = 0; i < len; i++) {     // 此层循环决定要去几个字符
                // 字符长度为len             0 - len
                // 字符长度为len - 1         0 - len-1  1 - len
                // 字符长度为len - 2         0 - len-2  1 - len-1  2 - len
                // ...      因此要二层循环
                for (int x = 0, y = len - i; y <= len; x++, y++) {
                    if (maxStr.contains(minStr.substring(x, y))) {
                        return minStr.substring(x, y);
                    }
                }
            }
        }
        return null;
    }


    // 返回 list
    public String[] getMaxSameSubString1(String str1, String str2) {
        if (str1 != null && str2 != null) {
            StringBuffer sBuffer = new StringBuffer();
            String maxString = (str1.length() > str2.length()) ? str1 : str2;
            String minString = (str1.length() > str2.length()) ? str2 : str1;
            int len = minString.length();
            for (int i = 0; i < len; i++) {
                for (int x = 0, y = len - i; y <= len; x++, y++) {
                    String subString = minString.substring(x, y);
                    if (maxString.contains(subString)) {
                        sBuffer.append(subString + ",");
                    }
                }
                System.out.println(sBuffer);
                if (sBuffer.length() != 0) {
                    break;
                }
            }
            String[] split = sBuffer.toString().replaceAll(",$", "").split("\\,");
            return split;
        }
        return null;
    }


    // 如果存在多个长度相同的最大相同子串：使用 ArrayList
    // public List<String> getMaxSameSubString2(String str1, String str2) {
    //     if (str1 != null && str2 != null) {
    //         List<String> list = new ArrayList<String>();
    //         String maxString = (str1.length() > str2.length()) ? str1: str2;
    //         String minString = (str1.length() > str2.length()) ? str2: str1;
    //
    //         int len = minString.length();
    //         for (int i = 0; i < len; i++) {
    //             for (int x = 0, y = len - i; y <= len; x++, y++) {
    //                 String subString = minString.substring(x, y);
    //                 if (maxString.contains(subString)) {
    //                     list.add(subString);
    //                 }
    //             }
    //             if (list.size() != 0) {
    //             break;
    //             }
    //         }
    //         return list;
    //     }
    //
    //     return null;
    // }



    @Test
    public void testGetMaxSameSubString() {
        String str1 = "abcwerthelloyuiodef";
        String str2 = "cvhellobnmiodef";
        String[] strs = getMaxSameSubString1(str1, str2);
        System.out.println(Arrays.toString(strs));
    }

    @Test
    public void test01() {
        String a = "omg666,omg666,omg666,omg666,omg666,omg666,";

        String b = a.replaceAll(",$", "");
        System.out.println(b);

        String[] split = b.split("\\,");
        System.out.println(Arrays.toString(split));
    }
}
