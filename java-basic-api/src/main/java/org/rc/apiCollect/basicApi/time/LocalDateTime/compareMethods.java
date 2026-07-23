package org.rc.apiCollect.basicApi.time.LocalDateTime;

import java.time.LocalDateTime;

// 比较两个 LocalDateTime
// isEqual(LocalDateTime other)：判断两个 LocalDateTime 是否相等。
// isBefore(LocalDateTime other)：判断是否在另一个 LocalDateTime 之前。
// isAfter(LocalDateTime other)：判断是否在另一个 LocalDateTime 之后。
public class compareMethods {
    public static void main(String[] args) {
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 5, 20, 15, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 5, 21, 15, 30);

        System.out.println("dateTime1 is equal to dateTime2: " + dateTime1.isEqual(dateTime2));         // false
        System.out.println("dateTime1 is before dateTime2: " + dateTime1.isBefore(dateTime2));          // true
        System.out.println("dateTime1 is after dateTime2: " + dateTime1.isAfter(dateTime2));            // false
    }
}
