package org.rc.apiCollect.basicApi.text.NumberFormat.collectAPI;

import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class getInstance {
    @Test
    public void test1() {
        NumberFormat a = NumberFormat.getInstance();
        try {
            Number number = a.parse("10000.345");
            System.out.println("China parsed number: " + number); // 输出：German parsed number: 1234.56
        } catch (ParseException e) {
            System.out.println("Error parsing German number: " + e.getMessage());
        }
    }

    @Test
    public void test2() {
        // NumberFormat.getInstance() 默认使用系统的默认区域设置来解析数字。不同的区域设置有不同的数字格式规则，例如：
        //     在美国（Locale.US），千位分隔符是逗号（,），小数点是句点（.）。
        //     在德国（Locale.GERMANY），千位分隔符是句点（.），小数点是逗号（,）。
        //     你可以通过传递特定的 Locale 来获取适用于不同地区的 NumberFormat 实例：

        // 使用德国的区域设置解析数字
        NumberFormat germanFormat = NumberFormat.getInstance(Locale.GERMANY);
        String germanValueStr = "1.234,56";
        try {
            Number number = germanFormat.parse(germanValueStr);
            System.out.println("German parsed number: " + number); // 输出：German parsed number: 1234.56
        } catch (ParseException e) {
            System.out.println("Error parsing German number: " + e.getMessage());
        }

        // Number number = NumberFormat.getInstance().parse("123.45");
        // double doubleValue = number.doubleValue(); // 转换为 double
        // int intValue = number.intValue();           // 转换为 int
    }
}
