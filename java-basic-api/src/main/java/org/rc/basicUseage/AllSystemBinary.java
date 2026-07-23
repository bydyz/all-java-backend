package org.rc.basicUseage;

public class AllSystemBinary {
    public static void main(String[] args) {
        int A1 = 123;
        int A2 = 0b101;
        int A3 = 0127;
        int A4 = 0x12aF;

        System.out.print(A1 + "\t\t\t" + A2 + "\t\t\t" + A3 + "\t\t\t" + "\t\t\t" + A4 + "\n\n\n");

        int B1 = 0b10000000;
        System.out.print(B1);
    }
}
