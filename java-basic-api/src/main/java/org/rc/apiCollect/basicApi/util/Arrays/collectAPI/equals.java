package org.rc.apiCollect.basicApi.util.Arrays.collectAPI;

import java.util.Arrays;

public class equals {
    public static void main(String[] args) {
        // 比较两个数组的元素是否依次相等          在Arrays或者equals上按住ctrl并左键单击，就可看源码
        int [] arr1 = new int[]{1,2,3,4,5};
        int [] arr2 = new int[]{1,2,3,4,5};
        // ==   比较得是变量存储的内容  此处存储的是变量地址，故而比较的也就是变量地址
        System.out.println(arr1 == arr2);  // false
        System.out.println(Arrays.equals(arr1,arr2));   // true
    }
}
