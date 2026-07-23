package org.rc.apiCollect.basicApi.time.ZoneId;

import java.time.ZoneId;
import java.util.Set;

public class getAvailableZoneIds {
    public static void main(String[] args) {
// 需要知道一些时区的 id
        // Set<String>是一个集合，容器
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        // 快捷模板 iter
        for (String availableZoneId : availableZoneIds) {
            System.out.println(availableZoneId);
        }

    }
}
