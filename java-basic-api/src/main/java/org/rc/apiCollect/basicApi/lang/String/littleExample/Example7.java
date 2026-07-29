package org.rc.apiCollect.basicApi.lang.String.littleExample;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Example7 {
    @Test
    public void testSort() {
        String str = "abcwerthelloyuiodef";
        char[] arr = str.toCharArray();
        Arrays.sort(arr);
        String newStr = new String(arr);
        System.out.println(newStr);
    }
}
