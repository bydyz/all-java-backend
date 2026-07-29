package org.rc.apiCollect.basicApi.io.FileWriter;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class FileWriter1 {

    //注意：应该使用 try-catch-finally 处理异常。这里出于方便阅读代码，使用了 throws 的方式
    @Test
    public void test01()throws IOException {
        // 使用文件名称创建流对象
        FileWriter fw = new FileWriter(new File("fw.txt"));
        
        // 写出数据     每次关闭后，再重新写入时，都是不断地从头到尾写入字符
        fw.write(97);       // 写出第 1 个字符
        fw.write('b');      // 写出第 2 个字符
        fw.write('C');      // 写出第 3 个字符
        fw.write(30000);    // 写出第 4 个字符，中文编码表中 30000 对应一个汉字。
        
        //关闭资源
        fw.close();
    }



    //注意：应该使用 try-catch-finally 处理异常。这里出于方便阅读代码，使用了 throws 的方式
    @Test
    public void test02()throws IOException {
        // 使用文件名称创建流对象
        FileWriter fw = new FileWriter(new File("fw.txt"));
        // 字符串转换为字节数组
        char[] chars = "尚硅谷".toCharArray();
        System.out.println("000   " + Arrays.toString(chars));
        // 写出字符数组
        fw.write(chars); // 尚硅谷
        // 写出从索引 1 开始，2 个字符。
        fw.write(chars,1,2); // 硅谷
        // 关闭资源
        fw.close();
    }



    //注意：应该使用 try-catch-finally 处理异常。这里出于方便阅读代码，使用了 throws 的方式
    @Test
    public void test03()throws IOException {
        // 使用文件名称创建流对象
        FileWriter fw = new FileWriter("fw.txt");
        // 字符串
        String msg = "尚硅谷";
        // 写出字符数组       每次关闭后，再重新写入时，都是不断地从头到尾写入字符
        fw.write(msg); //尚硅谷
        // 写出从索引 1 开始，2 个字符。
        fw.write(msg,1,2); // 硅谷
        // 关闭资源
        fw.close();
    }

    @Test
    public void test04(){
        FileWriter fw = null;
        try {
            //1. 创建 File 的对象
            File file = new File("fw.txt");
            //2. 创建 FileWriter 的对象，将 File 对象作为参数传递到 FileWriter 的构造器中

            //如果输出的文件已存在，则会对现有的文件进行覆盖
            // fw = new FileWriter(file);
            // fw = new FileWriter(file,false);

            //如果输出的文件已存在，则会在现有的文件末尾写入数据
            fw = new FileWriter(file,true);

            //3. 调用相关的方法，实现数据的写出操作
            //write(String str) / write(char[] cbuf)
            fw.write("I love you,");
            fw.write("you love him.");
            fw.write("so sad".toCharArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4. 关闭资源，避免内存泄漏
            try {
                if (fw != null)
                    // 在 JDK7 之前，我们这样处理资源的关闭：
                    fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    @Test
    public void test5() {
        // JDK7 的新特性
        // 在 try 的后面可以增加一个()，在括号中可以声明流对象并初始化。try 中的代码
        // 执行完毕，会自动把流对象释放，就不用写 finally 了。

        // 说明：
        // 1、在 try()中声明的资源，无论是否发生异常，无论是否处理异常，都会自动关闭资源对象，不用手动关闭了。
        // 2、这些资源实现类必须实现 AutoCloseable 或 Closeable 接口，实现其中的lose()方法。Closeable 是 AutoCloseable 的子接口。
        //     Java7 几乎把所有的“资源类”（包括文件 IO 的各种类、JDBC 编程的 Connection、Statement 等接口…）
        //     都进行了改写，改写后资源类都实现了 AutoCloseable 或 Closeable 接口，并实现了 close()方法。
        // 3、写到 try()中的资源类的变量默认是 final 声明的，不能修改。

        try (
                FileWriter fw = new FileWriter("d:/1.txt");
                BufferedWriter bw = new BufferedWriter(fw);
        ) {
            bw.write("hello");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
