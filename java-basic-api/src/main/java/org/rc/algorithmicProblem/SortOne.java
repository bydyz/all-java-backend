package org.rc.algorithmicProblem;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SortOne {
    public static void main(String[] args) {

    }

    @Test
    public void testSort() {
        String str = "abcwerthelloyuiodef";
        char[] arr = str.toCharArray();
        Arrays.sort(arr);
        String newStr = new String(arr);
        System.out.println(newStr);
    }
}
