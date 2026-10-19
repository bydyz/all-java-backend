package org.rc.apiCollect.basicApi.util.Arrays.collectAPI;

import java.util.Arrays;
// Module java.base
// Package java.util

public class toString {
    public static void main(String[] args) {
        String[] stringArray = {"666", "6666", "66666"};
        System.out.println(Arrays.toString(stringArray));   // [666, 6666, 66666]

        int[] intArray = {1, 2, 3, 4, 5};
        System.out.println(Arrays.toString(intArray));   // [1, 2, 3, 4, 5]

        // Object[]
        Object[] objectArray = {intArray, stringArray};
        System.out.println(Arrays.toString(objectArray));   // [[I@7699a589, [Ljava.lang.String;@58372a00]
        System.out.println(Arrays.deepToString(objectArray));   // [[1, 2, 3, 4, 5], [666, 6666, 66666]]
    }
}
