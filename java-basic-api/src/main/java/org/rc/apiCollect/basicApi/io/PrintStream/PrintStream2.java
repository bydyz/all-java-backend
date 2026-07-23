package org.rc.apiCollect.basicApi.io.PrintStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class PrintStream2 {
    public static void main(String[] args) {
        PrintStream ps = null;
        try {
            FileOutputStream fos = new FileOutputStream(new File("e:\\1\\1.txt"));
            // 创建打印输出流,设置为自动刷新模式(写入换行符或字节 '\n' 时都会刷新输出缓冲区)
            ps = new PrintStream(fos, true);
            if (ps != null) {// 把标准输出流(控制台输出)改成文件       从原先的显示到控制台，到输出到文件，即下方的 System.out.print 会打印到文件里
                System.setOut(ps);
            }
            for (int i = 0; i <= 255; i++) { // 输出 ASCII 字符
                System.out.print((char) i);
                if (i % 50 == 0) { // 每 50 个数据一行
                    System.out.println(); // 换行
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }
}
