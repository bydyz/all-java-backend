package org.rc.basicUseage;

public class UseOperate {
    public static void main(String[] args) {
        System.out.println(true + "come on!");
        System.out.println("s" + 55);
        System.out.print('\n');
        char a = 'g';
        System.out.println(a + 5);



        System.out.println("5+5=" + 5 + 5);
        byte a1 = 5 + 5;
        System.out.println("5+5=" + a1);

        byte bb1 = 127;
        bb1++;
        System.out.println("bb1 = " + bb1);



        int b2 = 2;
        b2 = b2++;
        System.out.println(b2);
        System.out.println();



        short b3 = 1;
        short b4 = 2;
        System.out.println(b3++ == b4);
        System.out.println(++b3 == b4);
        System.out.println("----------------------------");

        System.out.println();



        short c1 = 1;
        short c2 = 2;
        boolean c3 = c1++ == c2;
        boolean c5 = ++c1 == c2;
        // System.out.print(c3);
        // System.out.print("\t");
        // System.out.print(c5);
        System.out.print(c3 + "\t\t" + c5);
    }
}
