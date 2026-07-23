package org.rc.apiCollect.basicApi.io.File;

import java.io.File;

public class File7 {
    public static void main(String[] args) {
        // 如果文件或目录不存在，那么 exists()、isFile()和 isDirectory()都是返回true

        File f = new File("e:\\0\\1\\1.txt");
        File f2 = new File("e:/0/1/1-1");
        // 判断是否存在
        System.out.println("e:\\0\\1\\1.txt: "+f.exists());
        System.out.println("e:/0/1/1-1: "+f2.exists());
        // 判断是文件还是目录
        System.out.println("e:/0/1/1-1 文件?: "+f2.isFile());
        System.out.println("e:/0/1/1-1?: "+f2.isDirectory());
    }
}
