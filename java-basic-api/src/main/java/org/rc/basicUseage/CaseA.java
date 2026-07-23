package org.rc.basicUseage;

public class CaseA {
    public static void main(String[] args) {
        int theNumber = 682;
        int bai = theNumber / 100;
        int shi = theNumber % 100 / 10;
        int ge = theNumber % 10;
        System.out.print(bai + "\t\t" + shi + "\t\t" + ge);
    }
}
