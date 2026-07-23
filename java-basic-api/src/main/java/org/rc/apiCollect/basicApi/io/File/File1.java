package org.rc.apiCollect.basicApi.io.File;

import java.io.File;

public class File1 {
    public static void main(String[] args) {
        String pathname = "D:\\aaa.txt";
        // 无论该路径下是否存在文件或者目录，都不影响 File 对象的创建。
        File file1 = new File(pathname);
        System.out.println("000   " + file1);       // "D:\\aaa.txt"

        // 文件路径名
        String pathname2 = "D:\\z-aaa\\bbb.txt";
        File file2 = new File(pathname2);
        System.out.println("111   " + file2);

        // 通过父路径和子路径字符串
        String parent = "d:\\z-aaa";
        String child = "bbb.txt";
        File file3 = new File(parent, child);
        System.out.println("222   " + file3);

        // 通过父级 File 对象和子路径字符串
        File parentDir = new File("d:\\z-aaa");
        String childFile = "bbb.txt";
        File file4 = new File(parentDir, childFile);
        System.out.println("333   " + file4);
    }

}
