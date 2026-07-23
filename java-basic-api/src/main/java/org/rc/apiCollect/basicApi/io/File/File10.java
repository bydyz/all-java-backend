package org.rc.apiCollect.basicApi.io.File;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

public class File10 {
    @Test
    public void testPrintSubFile(){
        // 1.创建目录对象
        File dir = new File("E:\\0\\1");
        // 2.打印目录的子文件
        printSubFile(dir);
    }
    // 利用递归     从左到右打印树的所有 文件    文件就相当于叶子节点
    public static void printSubFile(File dir) {
        // 打印目录的子文件
        File[] subfiles = dir.listFiles();
        // System.out.println("000   " + Arrays.toString(subfiles));
        for (File f : subfiles) {
            if (f.isDirectory()) {// 文件目录
                printSubFile(f);
            } else {// 文件
                System.out.println("111   " + f.getAbsolutePath());
            }
        }
    }



    @Test
    public void testListAllFiles(){
        // 1.创建目录对象
        File dir = new File("E:\\0\\1");
        // 2.打印目录的子文件
        listAllSubFiles(dir);
    }
    // //练习 3：(方式 2)
    public void listAllSubFiles(File file) {
        if (file.isFile()) {
            System.out.println(file);
        } else {
            File[] all = file.listFiles();
            // 如果 all[i]是文件，直接打印
            // 如果 all[i]是目录，接着再获取它的下一级
            for (File f : all) {
                listAllSubFiles(f);// 递归调用：自己调用自己就叫递归
            }
        }
    }



    @Test
    public void testGetDirectorySize(){
        // 1.创建目录对象
        File dir = new File("E:\\0\\1");
        // 2.打印目录的子文件
        System.out.println(getDirectorySize(dir));
    }
    // 拓展 1：求指定目录所在空间的大小
    public long getDirectorySize(File file) {
        // file 是文件，那么直接返回 file.length()
        // file 是目录，把它的下一级的所有 file 大小加起来就是它的总大小
        long size = 0;
        if (file.isFile()) {
            size = file.length();
        } else {
            File[] all = file.listFiles();// 获取 file 的下一级
            // 累加 all[i]的大小
            for (File f : all) {
                size += getDirectorySize(f);// f 的大小;
            }
        }
        return size;
    }



    @Test
    public void testDeleteDirectory(){
        // 1.创建目录对象
        File dir = new File("E:\\0\\3");
        // 2.打印目录的子文件
        deleteDirectory(dir);
    }
    // 拓展 2：删除指定的目录
    public void deleteDirectory(File file) {
        // 如果 file 是文件，直接 delete
        // 如果 file 是目录，先把它的下一级干掉，然后删除自己
        if (file.isDirectory()) {
            File[] all = file.listFiles();
            // 循环删除的是 file 的下一级
            for (File f : all) {// f 代表 file 的每一个下级
                deleteDirectory(f);
            }
        }
        // 删除自己
        file.delete();
    }
}
