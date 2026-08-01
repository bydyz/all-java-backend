package org.rc.algorithmicProblem;

public class CalculateStringNum {
    public static void main(String[] args) {
        CalculateStringNum one = new CalculateStringNum();
        int a = one.getCount("1abc22abc333abc444abc555abc", "abc");
        System.out.println(a);
    }

    /**
     * 计算子字符串在主字符串中出现的次数
     *
     * @param mainStr 主字符串，需要在其中搜索子字符串
     * @param subStr  子字符串，需要在主字符串中搜索的目标
     * @return 子字符串在主字符串中出现的次数，如果主字符串长度小于子字符串则返回0
     */
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
