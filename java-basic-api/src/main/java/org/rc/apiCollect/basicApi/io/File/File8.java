package org.rc.apiCollect.basicApi.io.File;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class File8 {
    @Test
    public void test0() {
        // 文件的创建
        File f = new File("aaa.txt");
        System.out.println("aaa.txt 是否存在: " + f.exists());
        try {
            // 针对当前 Moudule 所在的 文件目录 进行创建
            System.out.println("aaa.txt 是否创建: " + f.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("aaa.txt 是否存在: " + f.exists());

        // 文件的删除
        System.out.println("aaa.txt 删除：" + f.delete());
    }

    @Test
    public void test1() {
        // 目录的创建
        File f2= new File("newDir");
        System.out.println("newDir 是否存在:" + f2.exists());
        // 针对当前 Moudule 所在的 文件目录 进行创建
        System.out.println("newDir 是否创建:" + f2.mkdir());
        System.out.println("newDir 是否存在:" + f2.exists());

        // delete 方法，如果此 File 表示目录，则目录必须为空才能删除。
        // 目录的删除
        System.out.println("newDir 删除：" + f2.delete());
    }

    @Test
    public void test2() {
        File f2= new File("newDir");
        System.out.println("newDir 是否创建:" + f2.mkdir());

        // 创建文件目录。如果此文件目录存在，就不创建了。如果此文件目录的上层目录不存在，也不创建。
        // 创建一级目录
        File f3= new File("newDir-0\\newDir-1");
        System.out.println("newDir-0\\newDir-1 创建：" + f3.mkdir());
        File f4= new File("newDir\\newDir-1");
        System.out.println("newDir\\newDir-1 创建：" + f4.mkdir());

        // 要删除一个文件目录，请注意该文件目录内不能包含文件或者文件目录。
        System.out.println("newDir目录 删除：" + f2.delete());

        // 目录的删除
        System.out.println("newDir-1 删除：" + f4.delete());
        System.out.println("newDir目录 删除：" + f2.delete());
    }

    @Test
    public void test3() {
        // 创建多级目录
        File f5= new File("newDira\\newDirb");
        System.out.println("newDira\\newDirb 创建：" + f5.mkdirs());
    }
}
