package org.rc.apiCollect.basicApi.util.Arrays.littleExample;

public class getMaxAndFirstIndex {
    public static void main(String[] args) {
        int [] array = {1, 12, 24, 8, 17, 38, 5, 38};
        int maxValue = array[0];
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                maxIndex = i;
            }
        }
        System.out.println("数组中的最大值为" + maxValue + "；" + "其第一次出现的下标为：" + maxIndex);
    }
}
