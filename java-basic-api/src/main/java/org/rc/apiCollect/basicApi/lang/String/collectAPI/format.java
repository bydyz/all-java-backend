package org.rc.apiCollect.basicApi.lang.String.collectAPI;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

public class format {
    // String.format 是 Java 中用于格式化字符串的一个非常强大的工具。它允许你创建格式化的字符串，类似于 C 语言中的 printf 函数。
    // 通过指定格式说明符和相应的参数，你可以控制输出的外观。

    // String formattedString = String.format(format, arguments...);
    // format：一个包含普通文本和格式说明符（以 % 开头）的字符串。
    // arguments：与格式说明符对应的参数列表，按照顺序一一对应。

    // 格式说明符
    // 格式说明符通常由百分号 % 开始，后跟一些可选标志、宽度、精度等，最后是转换字符。以下是常见格式说明符的组成部分：
    //     标志 (Flags)：修饰输出格式，如左对齐 -、加号 + 显示符号、空格   表示正数前加空格等。
    //     宽度 (Width)：指定最小字段宽度。
    //     精度 (Precision)：对于浮点数，表示小数点后的位数；对于字符串，表示最大长度。
    //     转换 (Conversion)：指定数据类型的转换方式，如 d 表示十进制整数、f 表示浮点数、s 表示字符串等。
    
    @Test
    public void test0() {
        int number = 42;
        String name = "Alice";

        // 使用 %d 表示十进制整数，%s 表示字符串
        String result = String.format("Hello, %s! Your lucky number is %d.", name, number);

        System.out.println(result); // 输出: Hello, Alice! Your lucky number is 42.
    }
    
    @Test
    public void test1() {
        double pi = 3.14159;

        // 使用 %.2f 指定保留两位小数
        String formattedPi = String.format("The value of PI is approximately %.2f.", pi);

        System.out.println(formattedPi); // 输出: The value of PI is approximately 3.14.
    }

    @Test
    public void test2() {
        int num1 = 7;
        int num2 = -8;

        // 使用 + 显示符号，0 补零，宽度为 3
        String formattedNums = String.format("|%+3d| |%04d|", num1, num2);
        System.out.println(formattedNums); // 输出: | +7| |-0008|

        // 使用 + 显示符号，0 补零，宽度为 3
        String formattedNums1 = String.format("|%3d| |%04d|", num1, num2);
        System.out.println(formattedNums1); // 输出: |  7| |-0008|

    }
    
    @Test
    public void test3() {
        LocalDate date = LocalDate.now();

        System.out.println("Origin date: " + date);

        // 使用特定的日期格式
        String formattedDate = String.format("Today's date is %tF.", date);

        System.out.println(formattedDate); // 输出: Today's date is YYYY-MM-DD.
    }
    
    @Test
    public void test4() {
        // String.format 支持根据不同的区域设置（Locale）进行格式化，这对于处理不同地区的日期、货币等信息非常有用。

        double price = 1234.56;

        // 英式英语格式
        String usFormattedPrice = String.format(Locale.US, "Price in US format: $%,.2f", price);
        // 德式德语格式
        String deFormattedPrice = String.format(Locale.GERMAN, "Preis im deutschen Format: %.2f €", price);

        System.out.println(usFormattedPrice); // 输出: Price in US format: $1,234.56
        System.out.println(deFormattedPrice); // 输出: Preis im deutschen Format: 1234,56 €
    }

    // 常见转换字符
    //     %b 或 %B：布尔值，true 或 false
    //     %c：字符
    //     %d：十进制整数
    //     %e 或 %E：科学记数法表示的浮点数
    //     %f：定点表示法的浮点数
    //     %g 或 %G：根据数值大小选择 %f 或 %e/%E
    //     %h 或 %H：哈希码
    //     %n：换行符（根据平台）
    //     %o：八进制整数
    //     %s 或 %S：字符串
    //     %t 或 %T：日期/时间格式（需要配合子模式）
    //     %x 或 %X：十六进制整数
}
