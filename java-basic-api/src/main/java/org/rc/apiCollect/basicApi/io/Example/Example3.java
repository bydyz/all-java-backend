package org.rc.apiCollect.basicApi.io.Example;

// import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class Example3 {

    // 整个目录的复制，自动进行递归遍历
    // 参数: src:要复制的文件夹路径    dest:要将文件夹粘贴到哪里去
    @Test
    public void test0() {
        // try{
        //     FileUtils.copyDirectoryToDirectory(new File("E:\\0\\1"),new File("E:\\1"));
        // } catch(IOException e){
        //     e.printStackTrace();
        // }
    }


    // 将内容 content 写入到 file 中
    @Test
    public void test1() {
        // try{
        //     FileUtils.writeStringToFile(new File("E:\\1\\1.txt"), "哎呀，666");
        // } catch(IOException e){
        //     e.printStackTrace();
        // }
    }


    // 读取文件内容，并返回一个String
    @Test
    public void test2() {
        // try{
        //     String a = FileUtils.readFileToString(new File("E:\\1\\2.txt"));
        //     System.out.println(a);
        // } catch(IOException e){
        //     e.printStackTrace();
        // }
    }


    // 文件复制
    @Test
    public void test3() {
        // try{
        //     // 第二个参数是目标文件  可不存在
        //     FileUtils. copyFile(new File("E:\\0\\1\\1-1\\1.txt"), new File("E:\\1\\1\\1-1\\3.txt"));
        // } catch(IOException e){
        //     e.printStackTrace();
        // }
    }
}
