package org.rc.apiCollect.basicApi.io.FileReader;

// 读取 hello.txt 文件中的字符数据，并显示在控制台上

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileReader1 {
    //实现方式 1
    @Test
    public void test1() throws IOException {
        //1. 创建 File 类的对象，对应着物理磁盘上的某个文件
        File file = new File("hello.txt");      // E:\Java\project\java-idea\BasicAPI\hello.txt

        //2. 创建 FileReader 流对象，将 File 类的对象作为参数传递到 FileReader 的构造器中
        FileReader fr = new FileReader(file);

        //3. 通过相关流的方法，读取文件中的数据
        // int data = fr.read(); //每调用一次读取一个字符
        // while (data != -1) {
        //     System.out.print((char) data);
        //     data = fr.read();
        // }

        int data;
        // fr.read(); 每调用一次读取一个字符
        while ((data = fr.read()) != -1) {
            System.out.println(data);
            // 此处用的 print 而不是 println
            // System.out.print((char) data);
        }

        //4. 关闭相关的流资源，避免出现内存泄漏
        fr.close();
    }



    //实现方式 2：在方式 1 的基础上改进，使用 try-catch-finally 处理异常。保证流是可以关闭的
    @Test
    public void test2() {
        FileReader fr = null;
        try {
            //1. 创建 File 类的对象，对应着物理磁盘上的某个文件
            File file = new File("hello.txt");

            //2. 创建 FileReader 流对象，将 File 类的对象作为参数传递到 FileReader 的构造器中
            fr = new FileReader(file);

            //3. 通过相关流的方法，读取文件中的数据
            /*
             * read():每次从对接的文件中读取一个字符。并将此字符返回。
             * 如果返回值为-1,则表示文件到了末尾，可以不再读取。
             * */
            // int data = fr.read();
            // while(data != -1){
            // System.out.print((char)data);
            // data = fr.read();
            // }
            int data;
            while ((data = fr.read()) != -1) {
                System.out.println((char) data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4. 关闭相关的流资源，避免出现内存泄漏
            try {
                if (fr != null)
                    fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    //实现方式 3：调用 read(char[] cbuf),每次从文件中读取多个字符
    @Test
    public void test3() {
        FileReader fr = null;
        try {
            //1. 创建 File 类的对象，对应着物理磁盘上的某个文件
            File file = new File("hello.txt");
            //2. 创建 FileReader 流对象，将 File 类的对象作为参数传递到 FileReader 的构造器中
            fr = new FileReader(file);
            //3. 通过相关流的方法，读取文件中的数据
            char[] cbuf = new char[5];
            /*
            * read(char[] cbuf) : 每次将文件中的数据读入到 cbuf 数组中，
            并返回读入到数组中的
            * 字符的个数。
            * */
            int len; //记录每次读入的字符的个数
            while ((len = fr.read(cbuf)) != -1) {
                //处理 char[]数组即可

                //错误：       似乎多了个r
                // for(int i = 0;i < cbuf.length;i++){
                //     System.out.print(cbuf[i]);
                // }

                //错误：       似乎多了个r
                // String str = new String(cbuf);
                // System.out.print(str);

                //正确：
                // for(int i = 0;i < len;i++){
                //     System.out.print(cbuf[i]);
                // }

                //正确：
                String str = new String(cbuf, 0, len);
                System.out.print(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4. 关闭相关的流资源，避免出现内存泄漏
            try {
                if (fr != null)
                    fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
