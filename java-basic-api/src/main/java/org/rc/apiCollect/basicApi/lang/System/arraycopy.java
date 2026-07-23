package org.rc.apiCollect.basicApi.lang.System;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class arraycopy {
    @Test
    public void test01(){
        int[] arr1 = {1,2,3,4,5};
        int[] arr2 = new int[10];
        /*
        * arraycopy 总共五个参数 要复制到的数组可以是源数组，不是源数组时则不影响源数组
        *
        * 第一个   要复制的源数组
        * 第二个   要复制的源数组 开始复制的下标
        * 第三个   要复制到的数组
        * 第四个   要复制到的数组 将要放置的下标
        * 第五个   复制的数目
        * */
        System.arraycopy(arr1,1,arr2,3, 2);
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));
    }
    @Test
    public void test02(){
        int[] arr = {1,2,3,4,5};
        System.arraycopy(arr,0,arr,1,arr.length-1);
        System.out.println(Arrays.toString(arr));       // [1, 1, 2, 3, 4]
    }
    @Test
    public void test03(){
        int[] arr = {1,2,3,4,5};
        System.arraycopy(arr,1,arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));       // [2, 3, 4, 5, 5]
    }

}
