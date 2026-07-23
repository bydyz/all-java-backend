package org.rc.algorithmicProblem;

public class CalculateStringNum {
    public int getCount(String mainStr, String subStr) {
        if (mainStr.length() >= subStr.length()) {
            int count = 0;
            int index = 0;

            // while((index = mainStr.indexOf(subStr)) != -1){
            //     count++;
            //     // 找到一个subStr后，将mainStr设置为找到subStr后面的字符串
            //     mainStr = mainStr.substring(index + subStr.length());
            // }

            // 改进：
            while ((index = mainStr.indexOf(subStr, index)) != -1) {
                index += subStr.length();
                count++;
            }
            return count;
        } else {
            return 0;
        }
    }

}
