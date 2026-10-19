package org.rc.otherKnowledge.Variable_usage;

public class setVariableAndExplain {
    public static void main(String[] args) {
        // ==================== 1. byte —— 字节型 ====================
        // 存储空间：1 字节 = 8 位（bit）
        // 数据范围：-2^7 ~ 2^7 - 1，即 -128 ~ 127
        byte byteVar = 127;
        System.out.println("byte    : " + byteVar
                + "    范围 [" + Byte.MIN_VALUE + ", " + Byte.MAX_VALUE + "]");

        // ==================== 2. short —— 短整型 ====================
        // 存储空间：2 字节 = 16 位
        // 数据范围：-2^15 ~ 2^15 - 1，即 -32768 ~ 32767
        short shortVar = 32767;
        System.out.println("short   : " + shortVar
                + "    范围 [" + Short.MIN_VALUE + ", " + Short.MAX_VALUE + "]");

        // ==================== 3. int —— 整型（默认整数类型） ====================
        // 存储空间：4 字节 = 32 位
        // 数据范围：-2^31 ~ 2^31 - 1，即 -2147483648 ~ 2147483647
        // 整数常量默认是 int，所以 long x = 10000000000; 会报错，要写 10000000000L。
        int intVar = 2147483647;
        System.out.println("int     : " + intVar
                + "    范围 [" + Integer.MIN_VALUE + ", " + Integer.MAX_VALUE + "]");

        // ==================== 4. long —— 长整型 ====================
        // 存储空间：8 字节 = 64 位
        // 数据范围：-2^63 ~ 2^63 - 1，
        //           即 -9223372036854775808 ~ 9223372036854775807
        // 注意：long 字面量必须加后缀 L（或 l），否则超出 int 范围会编译报错
        long longVar = 9223372036854775807L;
        System.out.println("long    : " + longVar
                + "    范围 [" + Long.MIN_VALUE + ", " + Long.MAX_VALUE + "]");

        // ==================== 5. float —— 单精度浮点型 ====================
        // 存储空间：4 字节 = 32 位
        //           按 IEEE 754 单精度布局：1 位符号 + 8 位指数 + 23 位尾数
        // 数据范围：绝对值最大约 3.4028235E38
        //           最小正非零值约 1.4E-45（Float.MIN_VALUE 指的是它，不是最小负数）
        // 有效十进制精度：约 6 ~ 7 位
        // 注意：float 字面量必须加后缀 F（或 f），否则会被当作 double
        float floatVar = 3.14159F;
        System.out.println("float   : " + floatVar
                + "    范围 [-" + Float.MAX_VALUE + ", " + Float.MAX_VALUE + "]"
                + "，最小正数 " + Float.MIN_VALUE);

        // ==================== 6. double —— 双精度浮点型（默认浮点类型） ====================
        // 存储空间：8 字节 = 64 位
        //           按 IEEE 754 双精度布局：1 位符号 + 11 位指数 + 52 位尾数
        // 数据范围：绝对值最大约 1.7976931348623157E308
        //           最小正非零值约 4.9E-324
        // 有效十进制精度：约 15 ~ 17 位
        // Java 的浮点型常量默认为 double 型。     意思是：在 Java 源码里，你直接写出来的“小数/浮点字面量”，编译器默认把它当成 double 类型，而不是 float。只有加了后缀 F 或 f，才表示 float：
        // float f = 3.14;   // 编译错误！因为 3.14 是 double，而 double 不能自动“窄化”成 float，会报：incompatible types: possible lossy conversion from double to float
        double doubleVar = 3.141592653589793;
        System.out.println("double  : " + doubleVar
                + "    范围 [-" + Double.MAX_VALUE + ", " + Double.MAX_VALUE + "]"
                + "，最小正数 " + Double.MIN_VALUE);

        // ==================== 7. char —— 字符型 ====================
        // 存储空间：2 字节 = 16 位（无符号）
        // 数据范围：0 ~ 65535，即 '\u0000' ~ '\uffff'（Unicode 编码，UTF-16 码元）
        // 注意：char 是无符号的，不能为负；参与算术运算时会自动提升为 int
        char charVar = 'A';
        // Java 中的所有字符都使用 Unicode 编码，故一个字符可以存储一个字母，一个汉字，或其他书面语的一个字符。
        char charVar1 = '\u0023';   // \u0023 表示 '#'
        // Java 中还允许使用转义字符‘\’来将其后的字符转变为特殊字符型常量。
        char c3 = '\n'; // '\n'表示换行符
        System.out.println("char    : " + charVar
                + "    (对应数值 " + (int) charVar + ")    范围 [0, 65535]");

        // ==================== 8. boolean —— 布尔型 ====================
        // 存储空间：Java 语言规范没有规定精确大小，JVM 规范只规定取值 true / false
        //           在 HotSpot 虚拟机上的常见实现：
        //             - 作为局部变量 / 成员变量：通常占 4 字节（与 int 同宽，便于栈操作）
        //             - 作为 boolean[] 数组元素：每个元素占 1 字节
        // 数据范围：只有 true 和 false 两个值
        // 不可以使用 0 或非 0 的整数替代 false 和 true，
        boolean booleanVar = true;
        System.out.println("boolean : " + booleanVar
                + "    取值 {true, false}（大小由 JVM 实现决定）");

        // ---------- 附：用包装类常量验证各类型所占位数（单位：bit） ----------
        System.out.println("\n---- 各类型所占位数（bit）----");
        System.out.println("Byte.SIZE    = " + Byte.SIZE);      // 8
        System.out.println("Short.SIZE   = " + Short.SIZE);     // 16
        System.out.println("Integer.SIZE = " + Integer.SIZE);   // 32
        System.out.println("Long.SIZE    = " + Long.SIZE);      // 64
        System.out.println("Float.SIZE   = " + Float.SIZE);     // 32
        System.out.println("Double.SIZE  = " + Double.SIZE);    // 64
        System.out.println("Character.SIZE = " + Character.SIZE); // 16
        // Boolean 没有 SIZE 常量（JVM 未规定其大小）
    }
}
