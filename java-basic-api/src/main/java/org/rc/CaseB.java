package org.rc;

import java.util.Scanner;

public class CaseB {
    public static void main(String[] args) {
        double balance = 0.0;
        Scanner scan = new Scanner(System.in);
        boolean isFlag = true;
        do{
            System.out.println("=========ATM========");
            System.out.println("\t1、存款");
            System.out.println("\t2、取款");
            System.out.println("\t3、显示余额");
            System.out.println("\t4、退出");
            System.out.print("请选择(1-4)：");
            int selection = scan.nextInt();
            switch(selection){
                case 1:
                    System.out.print("要存款的额度为：");
                    double addMoney = scan.nextDouble();
                    if(addMoney > 0){
                        balance += addMoney;
                    }
                    break;
                case 2:
                    System.out.print("要取款的额度为：");
                    double minusMoney = scan.nextDouble();
                    if(minusMoney > 0 && balance >= minusMoney){
                        balance -= minusMoney;
                    }else{
                        System.out.println("您输入的数据非法或余额不足");
                    }
                    break;
                case 3:
                    System.out.println("当前的余额为：" + balance);
                    break;
                case 4:
                    System.out.println("欢迎下次进入此系统。^_^");
                    isFlag = false;
                    break;
                default:
                    System.out.println("请重新选择！");
                    break;
            }
        }while(isFlag);
        scan.close();
    }
}
