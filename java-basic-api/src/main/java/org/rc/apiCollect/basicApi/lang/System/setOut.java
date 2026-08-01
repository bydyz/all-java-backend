package org.rc.apiCollect.basicApi.lang.System;

import java.io.FileOutputStream;
import java.io.PrintStream;

// System.setOut(PrintStream out) 的作用： 重新分配标准输出流（System.out），可以将输出重定向到文件、自定义流等。
public class setOut {
    public static void main(String[] args) {
        // System.setOut() 方法用于重新分配标准输出流
        // 可以将输出重定向到文件、其他流等

        // 示例1：将输出重定向到文件
        try {
            // 保存原始的标准输出流
            PrintStream originalOut = System.out;
            
            // 创建文件输出流
            FileOutputStream fileOut = new FileOutputStream("output.txt");
            // 创建打印流
            PrintStream filePrintStream = new PrintStream(fileOut);
            
            // 将标准输出重定向到文件
            System.setOut(filePrintStream);
            
            // 这些输出将写入文件，而不是控制台
            System.out.println("这行文本将写入文件");
            System.out.println("标准输出已被重定向");
            
            // 恢复原始的标准输出流
            System.setOut(originalOut);
            System.out.println("标准输出已恢复到控制台");
            
            // 关闭文件流
            filePrintStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 示例2：重定向到自定义输出流（用于捕获输出）
        PrintStream customOut = new PrintStream(System.out) {
            @Override
            public void println(String x) {
                // 在输出前添加时间戳
                super.println("[" + System.currentTimeMillis() + "] " + x);
            }
        };
        
        // 临时重定向到自定义流
        PrintStream originalOut2 = System.out;
        System.setOut(customOut);
        System.out.println("这条消息带有时间戳");
        
        // 恢复原始输出
        System.setOut(originalOut2);
        System.out.println("恢复正常输出");
    }
}