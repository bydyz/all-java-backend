package org.rc.apiCollect.basicApi.util.Arrays.littleExample;

import java.util.Scanner;

public class getWeek {
    public static void main(String[] args) {
        String[] weeks = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        Scanner inputOneToSeven = new Scanner(System.in);
        System.out.print("请输入【1-7】范围的整数：");
        int gotNumber = inputOneToSeven.nextInt();
        if(gotNumber < 1 || gotNumber > 7) {
            System.out.println("你输入的输入非法");
        } else {
            System.out.println("对应的星期为：" + weeks[gotNumber]);
        }
        inputOneToSeven.close();
    }
}
