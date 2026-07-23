package org.rc.apiCollect.basicApi.util.Arrays.littleExample;

public class sumAndAvg {
    public static void main(String[] args) {
        int [] array = {16, 12, 8, 3, 21, 18};
        int sum = 0, avg;
        for (int arrayItem: array) {
            sum += arrayItem;
        }
        avg = sum/(array.length);
        System.out.print("总和为：" + sum + ";" + "平均值为：" + avg);
    }
}
