package org.rc.apiCollect.basicApi.time.Instant;

import java.time.Instant;

public class ofEpochMilli {
    public static void main(String[] args) {
        // 返回在 1970-01-01 00:00:00 基础上加上指定毫秒数之后的 Instant 类的对象
        // 1000ms = 1s
        Instant a = Instant.ofEpochMilli(1000);
        System.out.println(a);
    }
}
