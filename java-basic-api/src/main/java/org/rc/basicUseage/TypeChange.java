package org.rc.basicUseage;

public class TypeChange {
    public static void main(String[] args) {
        int i = 'Q';
        System.out.print(i + "\n");
        double d = 10;
        System.out.println(d);
        long num = 1345678;
        System.out.println(num);
        // long num1 = 12345678987654321;
        // System.out.println(num1);
        long num2 = 12345678987654321L;
        System.out.println(num2);
        byte bigB1 = 118;
        System.out.println(bigB1);
        // byte bigB2 = 130;
        // System.out.println(bigB2);
        System.out.print("\n");

        int i2 = 1;
        byte b = 2;
        double d2 = 1.6;
        System.out.println(i2 + b + d2);
        System.out.print("\n");

        byte m1 = 1;
        byte m2 = 2;
        // byte m3 = m1 + m2;
        int m3 = m1 + m2;
        char c1 = '0';
        char c2 = 'A';
        int n = c1 + c2;
        System.out.println(c1 + c2);

        System.out.print("\n");
        System.out.print("\n");
        System.out.print("\n");
        System.out.print("\n");
        System.out.print("\n");









        int A1 = (int)3.14;
        System.out.println(A1);

        double A2 = 1.2;
        int A3 = (int)A2;
        // System.out.print(A2, A3);
        System.out.print(A2 + "\t\t");
        System.out.print(A3 + "\n");

        int A5 = 200;
        byte A6 = (byte)A5;
        System.out.print(A5 + "\t\t\t" + A6 + "\n\n\n\n\n\n");









        short B1 = 5;
        System.out.print(B1 + "\n");
        short B3 = 3;
        System.out.println(B3 + "\n\n\n\n\n");
        // short B2 = B1 - 2;
        // System.out.print(B2 + "");








        byte C1 = 3;
        // C1 = C1 + 4;
        C1 = (byte)(C1 + 4);
        System.out.print(C1 +"\n\n\n\n\n");







        byte D1 = 5;
        short D2 = 3;
        // short D3 = D1 + D2;
        // System.out.println(D3);
        int D4 = D1 + D2;
        System.out.println(D4 + "\n\n\n\n\n");
    }
}
