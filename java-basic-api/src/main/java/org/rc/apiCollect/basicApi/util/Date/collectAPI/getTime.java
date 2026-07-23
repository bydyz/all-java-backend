package org.rc.apiCollect.basicApi.util.Date.collectAPI;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class getTime {
    @Test
    public void test01() {
        Date a = new Date();
        System.out.println(a);

        // 返回自 1970 年 1 月 1 日 00:00:00 GMT 以来到此 Date 对象表示的毫秒数。
        long time = a.getTime();
        System.out.println(time);


        System.out.println();


        // 传入的既是毫秒
        Date b = new Date(Long.MAX_VALUE);
        System.out.println(b);

        long time2 = b.getTime();
        System.out.println(time2);
    }
}
