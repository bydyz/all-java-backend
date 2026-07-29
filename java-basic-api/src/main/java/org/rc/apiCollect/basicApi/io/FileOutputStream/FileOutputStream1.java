package org.rc.apiCollect.basicApi.io.FileOutputStream;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutputStream1 {
    //注意：应该使用 try-catch-finally 处理异常。这里出于方便阅读代码，使用了 throws 的方式
    @Test
    public void test01() throws IOException {
        // 使用文件名称创建流对象      没有则创建文件，有则覆盖修改
        FileOutputStream fos = new FileOutputStream("fos.txt");
        // 写出数据
        fos.write(97); // 写出第 1 个字节
        fos.write(98); // 写出第 2 个字节
        fos.write(99); // 写出第 3 个字节
        // 关闭资源
        fos.close();
        /* 输出结果：abc*/
    }



    @Test
    public void test02()throws IOException {
        // 使用文件名称创建流对象
        FileOutputStream fos = new FileOutputStream("fos.txt");
        // 字符串转换为字节数组
        byte[] b = "abcde".getBytes();
        // 写出从索引 2 开始，2 个字节。索引 2 是 c，两个字节，也就是 cd。
        fos.write(b,2,2);
        // 关闭资源
        fos.close();
    }
    //这段程序如果多运行几次，每次都会在原来文件末尾追加 abcde



    @Test
    public void test03()throws IOException {
        // 使用文件名称创建流对象          有true时，会继续向末尾添加字符
        FileOutputStream fos = new FileOutputStream("fos.txt",true);
        // 字符串转换为字节数组
        byte[] b = "abcde".getBytes();
        fos.write(b);
        // 关闭资源
        fos.close();
    }



    //使用 FileInputStream\FileOutputStream，实现对文件的复制
    @Test
    public void test05() {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //1. 造文件-造流
            //复制图片
            // fis = new FileInputStream(new File("pony.jpg"));
            // fos = new FileOutputStream(new File("pony_copy1.jpg"));

            //复制文本文件    若源文件不存在，会报错 java.io.FileNotFoundException
            fis = new FileInputStream(new File("hello.txt"));
            fos = new FileOutputStream(new File("hello1.txt"));

            //2. 复制操作（读、写）
            byte[] buffer = new byte[1024];
            int len;    //每次读入到 buffer 中字节的个数
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                String str = new String(buffer,0,len);
                System.out.print(str);
            }
            System.out.println("复制成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //3. 关闭资源
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
