package org.rc.apiCollect.basicApi.time.Instant;

import java.time.Instant;

public class toEpochMilli {
    public static void main(String[] args) {
        Instant a = Instant.parse("1970-01-01T02:00:00.674675500Z");

        // public long toEpochMilli()

        // instant     瞬间
        // Converts sth1 to sth2     将 sth1 转换成 sth2
        // the number of sth       sth的数量
        // milliseconds from the epoch of 1970-01-01T00:00:00Z.       从1970-01-01T00:00:00Z开始的毫秒数。
        // the epoch of 1970-01-01T00:00:00Z       1970年1月1日t00:00 . 00z的时代

        long b = a.toEpochMilli();
        System.out.println(b);          // 7200674
    }
}
