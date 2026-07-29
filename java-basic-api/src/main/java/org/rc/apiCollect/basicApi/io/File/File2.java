package org.rc.apiCollect.basicApi.io.File;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class File2 {
    @Test
    public void test01() throws IOException {
        File f1 = new File("E:\\0\\1\\1-1\\a.txt"); //绝对路径
        System.out.println("文件/目录的名称：" + f1.getName());
        System.out.println("文件/目录的构造路径名：" + f1.getPath());
        System.out.println("文件/目录的绝对路径名：" + f1.getAbsolutePath());
        System.out.println("文件/目录的父目录名：" + f1.getParent());
    }

    @Test
    public void test02() throws IOException{
        // 根路径  当前项目所在盘的根路径
        File f2 = new File("/Java/forJavaIO/1.txt");    //绝对路径，从根路径开始
        if (f2.exists()) {
            System.out.println("File exists.");
        } else {
            System.out.println("File does not exist.");
        }
        System.out.println("文件/目录的名称：" + f2.getName());
        System.out.println("文件/目录的构造路径名：" + f2.getPath());
        System.out.println("文件/目录的绝对路径名：" + f2.getAbsolutePath());
        System.out.println("文件/目录的父目录名：" + f2.getParent());
    }

    @Test
    public void test03() throws IOException {
        // 在 user.dir 上加
        // IDEA 中，main 中的文件的相对路径，是相对于"当前工程"
        // IDEA 中，单元测试方法中的文件的相对路径，是相对于"当前 module"

        File f3 = new File("HelloIO.java"); //相对路径
        System.out.println("user.dir = " + System.getProperty("user.dir"));
        System.out.println("文件/目录的名称：" + f3.getName());
        System.out.println("文件/目录的构造路径名：" + f3.getPath());
        System.out.println("文件/目录的绝对路径名：" + f3.getAbsolutePath());
        System.out.println("文件/目录的父目录名：" + f3.getParent());
    }

    @Test
    public void test04() throws IOException{
        // 在 user.dir 上加
        // IDEA 中，main 中的文件的相对路径，是相对于"当前工程"
        // IDEA 中，单元测试方法中的文件的相对路径，是相对于"当前 module"

        File f5 = new File("HelloIO.java");//相对路径
        System.out.println("user.dir =" + System.getProperty("user.dir"));
        System.out.println("文件/目录的名称：" + f5.getName());
        System.out.println("文件/目录的构造路径名：" + f5.getPath());
        System.out.println("文件/目录的绝对路径名：" + f5.getAbsolutePath());
        System.out.println("文件/目录的父目录名：" + f5.getParent());
    }
}
