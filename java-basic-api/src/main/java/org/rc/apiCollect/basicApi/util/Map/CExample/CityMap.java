package org.rc.apiCollect.basicApi.util.Map.CExample;

import java.util.HashMap;
import java.util.Map;

public class CityMap {
    public static Map model = new HashMap();

    static {
        System.out.println("111   " + 1);
        model.put("北京", new String[] {"北京"});
        model.put("上海", new String[] {"上海"});
        model.put("天津", new String[] {"天津"});
        model.put("重庆", new String[] {"重庆"});
        model.put("黑龙江", new String[] {"哈尔滨","齐齐哈尔","牡丹江","大庆","伊春","双鸭山","绥化"});
        model.put("吉林", new String[] {"长春","延边","吉林","白山","白城","四平","松原"});
        model.put("河北", new String[] {"石家庄","张家口","邯郸","邢台","唐山","保定","秦皇岛"});
    }
}
