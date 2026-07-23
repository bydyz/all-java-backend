package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class indexOf {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning. Do you hate too?");
        System.out.println(buffer);

        int index = buffer.indexOf("hate");
        System.out.println(index);
        System.out.println(buffer);
        System.out.println(buffer.indexOf("y"));






        System.out.println();
        // 在当前字符序列[fromIndex,最后]中查询 str 的第一次出现下标
        System.out.println(buffer.indexOf("hate", 7));
    }
}
