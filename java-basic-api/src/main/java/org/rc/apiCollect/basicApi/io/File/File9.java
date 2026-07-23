package org.rc.apiCollect.basicApi.io.File;
// 练习 1：利用 File 构造器，new 一个文件目录 file
//     1) 在其中创建多个文件和目录
//     2) 编写方法，实现删除 file 中指定文件的操作
//
// 练习 2：判断指定目录下是否有后缀名为.jpg 的文件。如果有，就输出该文件名称


import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class File9 {
    //方法 1：
    @Test
    public void test1(){
        File srcFile = new File("e:\\0\\1");
        System.out.println("000   " + srcFile);
        String[] fileNames = srcFile.list();
        System.out.println("111   " + fileNames);
        System.out.println("222   " + Arrays.toString(fileNames));
        for(String fileName : fileNames){
            if(fileName.endsWith(".jpg")){
                System.out.println(fileName);
            }
        }
    }

    //方法 2：
    @Test
    public void test2(){
        File srcFile = new File("e:\\0\\1");
        System.out.println("000   " + srcFile);
        File[] listFiles = srcFile.listFiles();
        System.out.println("111   " + listFiles);
        System.out.println("222   " + Arrays.toString(listFiles));
        for(File file : listFiles){
            if(file.getName().endsWith(".jpg")){
                System.out.println(file.getAbsolutePath());
            }
        }
    }

    //方法 3：
    /*
     * File 类提供了两个文件过滤器方法
     * public String[] list(FilenameFilter filter)
     * public File[] listFiles(FileFilter filter)
     */
    @Test
    public void test3(){
        File srcFile = new File("e:\\0\\1");
        File[] subFiles = srcFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                System.out.println("000   " + dir + "   " + name);
                return name.endsWith(".jpg");
            }
        });
        System.out.println("111   " + Arrays.toString(subFiles));
        for(File file : subFiles){
            System.out.println(file.getAbsolutePath());
        }
    }
}
