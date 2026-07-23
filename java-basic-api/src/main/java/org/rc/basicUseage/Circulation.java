package org.rc.basicUseage;

public class Circulation {
    public static void main(String[] args) {
        int num = 1;
        for(System.out.print("a"); num < 3; System.out.print("c"), num++) {
            System.out.print("b");
        };
    }
}
