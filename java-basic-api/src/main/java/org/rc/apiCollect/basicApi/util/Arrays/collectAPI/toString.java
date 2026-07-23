package org.rc.apiCollect.basicApi.util.Arrays.collectAPI;

import java.util.Arrays;
// Module java.base
// Package java.util

public class toString {
    public static void main(String[] args) {
        String[] stringArray = { "666", "6666", "66666" };
        System.out.println(Arrays.toString(stringArray));

        int[] intArray = {1, 2, 3, 4, 5};
        System.out.println(Arrays.toString(intArray));

        // Object[]
        Object[] objectArray = {intArray, stringArray};
        System.out.println(Arrays.toString(objectArray));
        System.out.println(Arrays.deepToString(objectArray));
    }
}
