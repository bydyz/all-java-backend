package org.rc.apiCollect.basicApi.io.BufferedAbout;

import org.junit.jupiter.api.Test;

import java.io.*;

public class Buffered2 {
    @Test
    public void testReadLine()throws IOException {
        // 读取的时候没有文件则会报错  java.io.FileNotFoundException     写入则不会有这种问题
        // 创建流对象
        BufferedReader br = new BufferedReader(new FileReader("in.txt"));
        // 定义字符串,保存读取的一行文字
        String line;
        // 循环读取,读取到最后返回 null
        while ((line = br.readLine())!=null) {
            System.out.println(line);
        }
        // 释放资源
        br.close();
    }



    @Test
    public void testNewLine()throws IOException{
        // 创建流对象
        BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt"));
        // 写出数据
        bw.write("尚");
        // 写出换行
        bw.newLine();
        bw.write("硅");
        bw.newLine();
        bw.write("谷");
        bw.newLine();
        // 释放资源
        bw.close();
    }
}
