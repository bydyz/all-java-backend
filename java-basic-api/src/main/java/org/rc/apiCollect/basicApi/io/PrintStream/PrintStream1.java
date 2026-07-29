package org.rc.apiCollect.basicApi.io.PrintStream;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintStream1 {
    @Test
    public void test0() throws FileNotFoundException {
        PrintStream ps = new PrintStream("io.txt");
        ps.println("hello");
        ps.println(1);
        ps.println(1.5);
        ps.close();
    }



    @Test
    public void test1() {
        PrintStream ps = null;
        try {
            FileOutputStream fos = new FileOutputStream(new File("e:\\IO\\text.txt"));
            // 创建打印输出流,设置为自动刷新模式(写入换行符或字节 '\n' 时都会刷新输出缓冲区)
            ps = new PrintStream(fos, true);
            if (ps != null) {   // 把标准输出流(控制台输出)改成文件
                System.setOut(ps);
            }
            for (int i = 0; i <= 255; i++) {    // 输出 ASCII 字符
                System.out.print((char) i);
                if (i % 50 == 0) {  // 每 50 个数据一行
                    System.out.println();   // 换行
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



    @Test
    public void test2() {
        //测试工具类是否好用
        Logger.log("调用了 System 类的 gc()方法，建议启动垃圾回收");
        Logger.log("调用了 TeamView 的 addMember()方法");
        Logger.log("用户尝试进行登录，验证失败");
    }

}

// 日志工具
class Logger {
    /*
    记录日志的方法。
    */
    public static void log(String msg) {
        try {
            // 指向一个日志文件
            PrintStream out = new PrintStream(new FileOutputStream("log.txt", true));
            // 改变输出方向
            System.setOut(out);
            // 日期当前时间
            Date nowTime = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
            String strTime = sdf.format(nowTime);
            System.out.println(strTime + ": " + msg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

