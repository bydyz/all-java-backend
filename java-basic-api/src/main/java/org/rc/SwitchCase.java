package org.rc;

import java.util.Scanner;

public class SwitchCase {
    public static void main(String[] args) {
        int score = 75;
        switch(score / 10) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                System.out.println("exam not pass");
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                System.out.println("exam pass");
                break;
            default:
                System.out.println("the score is wrong!");
                break;
        };
        System.out.print("\n\n\n");






        Scanner scan = new Scanner(System.in);
        System.out.println("Please input month: ");
        int month = scan.nextInt();

        System.out.println("Please input day: ");
        int day = scan.nextInt();

        System.out.println(month + "." + day);
        scan.close();
        System.out.print("\n\n\n");







        for(int i = 1; i <= 100; i++) {
            int a = (int)(Math.random()*6 + 1);
            System.out.println("the" + i + ":" + a);
        }

    }
}
