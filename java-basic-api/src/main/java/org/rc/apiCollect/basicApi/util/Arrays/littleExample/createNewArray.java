package org.rc.apiCollect.basicApi.util.Arrays.littleExample;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

// 创建一个长度为 6 的 int 型数组，要求数组元素的值都在 1-30 之间，且是随机赋值。同时，要求元素的值各不相同。

// Math.random()  会获取 大于等于0.0 小于等于1.0 的double值

public class createNewArray {
     @Test
     public void wayOne() {
        int[] array = new int[6];
        // for (int arrayItem : array) {
        //     // 取值为 5-78  (Math.random()*73) + 5
        //     // 取值为 5-78 的任意整数  (int) (Math.random()*73) + 5
        //     arrayItem = (int) (Math.random() * 30) + 1;
        // }
        for (int i = 0; i < array.length; i++) {
            array[i] = (int)(Math.random()*30) + 1;
            boolean flag = false;
            while (true) {
                for (int j = 0; j < i; j++) {
                    if (array[i] == array[j]) {
                        flag = true;
                        break;
                    }
                }
                if(flag) {
                    array[i] = (int)(Math.random()*30) + 1;
                    flag = false;
                    continue;
                }
                break;
            }
        }
         System.out.print(Arrays.toString(array));
    }

    @Test
     public void wayTwo() {
         int[] array = new int[6];
         for(int i = 0; i < 6; i++) {
            array[i] = (int)(Math.random() * 29) + 1;

            boolean flag = false;
            while(true) {
                for(int j = 0; i < j; j++) {
                    if(array[j] == array[i]) {
                        flag = true;
                        break;
                    }
                }
                if(flag) {
                    array[i] = (int)(Math.random() * 30);
                    continue;
                }
                break;
            }
         }
         System.out.print(Arrays.toString(array));
     }
}
