package org.rc.apiCollect.basicApi.util.Arrays.littleExample;

public class numOfEven {
    public static void main(String[] args) {
        int [] array = { 1, 3, 2, 16, 21, 28, 38, 6, 15, 24};
        int numOfEven = 0;
        for (int arrayItem: array) {
            if(arrayItem % 2 == 0) {
                numOfEven++;
            }
        }
        System.out.println("数组中偶数的个数为：" + numOfEven);
    }
}
