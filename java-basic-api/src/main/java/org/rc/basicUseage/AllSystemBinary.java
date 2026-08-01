package org.rc.basicUseage;

// 所有进制数
public class AllSystemBinary {
    public static void main(String[] args) {
        int A1 = 123;        // 十进制数
        int A2 = 0b101;      // 二进制数（以0b开头）
        int A3 = 0127;       // 八进制数（以0开头）
        int A4 = 0x12aF;     // 十六进制数（以0x开头）

        System.out.print(A1 + "\t\t\t" + A2 + "\t\t\t" + A3 + "\t\t\t" + "\t\t\t" + A4 + "\n\n\n");

        int B1 = 0b10000000; // 二进制数（以0b开头）
        System.out.print(B1);
    }
}
