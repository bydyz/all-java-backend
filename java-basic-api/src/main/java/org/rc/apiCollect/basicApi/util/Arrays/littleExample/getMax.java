package org.rc.apiCollect.basicApi.util.Arrays.littleExample;

public class getMax {
    public static void main(String[] args) {
        int [] array = { 2, 11, 6, 14, 8, 28, 13, 9, 32 };
        int arrayMax = array[0];
        for (int i = 1; i < array.length; i++) {
            if(array[i] > arrayMax) {
                arrayMax = array[i];
            }
        }
        System.out.println("数组中的最大值为：" + arrayMax);
    }
}
