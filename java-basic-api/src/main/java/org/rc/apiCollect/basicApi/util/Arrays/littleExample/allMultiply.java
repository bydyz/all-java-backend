package org.rc.apiCollect.basicApi.util.Arrays.littleExample;

public class allMultiply {
    public static void main(String[] args) {
        int [] array = { 2, 6, 13, 8, 9};
        int multiplyValue = 1;
        for (int arrayItem : array) {
            multiplyValue *= arrayItem;
        }
        System.out.print("数组所有数的乘积为：" + multiplyValue);
    }
}
