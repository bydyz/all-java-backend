package org.rc.apiCollect.basicApi.util.Map.CExample;

import java.util.Scanner;
import java.util.Set;

public class Example3 {
    public static void main(String[] args) {
        System.out.println("000   " + 0);
        Set keySet = CityMap.model.keySet();
        System.out.println("222   " + keySet);
        for(Object s : keySet) {
            // 奇怪  似乎   黑龙江 后的 \t  空格更大
            System.out.println(s + "\t");
        }
        System.out.println();
        System.out.println("请选择你所在的省份：");
        Scanner scan = new Scanner(System.in);

        // 省份不在可选范围需要重新选  待完成
        // while(true) {
        //     String province = scan.next();
        // }
        String province = scan.next();

        String[] citys = (String[]) CityMap.model.get(province);
        for(String city : citys) {
            System.out.print(city + "\t");
        }
        System.out.println();
        System.out.println("请选择你所在的城市：");

        // 城市不在可选范围需要重新选  待完成
        // while(true) {
        //     String city = scan.next();
        // }
        String city = scan.next();

        System.out.println("信息登记完毕");
    }
}
