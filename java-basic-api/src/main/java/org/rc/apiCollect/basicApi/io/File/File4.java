package org.rc.apiCollect.basicApi.io.File;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class File4 {
    // String pathname2 = "D:\\aaa\\bbb.txt";
    // 如果 aaa 目录和 bbb.txt 文件都不存在，你可以通过以下步骤在 Java 中创建它们：
    //
    // 步骤 1: 检查目录是否存在
    // 首先，检查 aaa 目录是否存在。如果不存在，创建它。
    //
    // 步骤 2: 创建目录
    // 使用 File 类的 mkdir() 或 mkdirs() 方法来创建目录。mkdir() 创建单个目录，而 mkdirs() 可以创建所有必需的父目录。
    //
    // 步骤 3: 创建文件
    // 一旦目录存在，使用 File 类的 createNewFile() 方法来创建文件。
    @Test
    public void test2() {
        String pathname2 = "E:\\0\\1\\1\\a.txt";
        File file2 = new File(pathname2);
        System.out.println("000   " + file2);

        // 获取目录路径
        String directoryPath = file2.getParent();
        System.out.println("111   " + directoryPath);       // 111   E:\Java\forJavaIO\a\a
        File directory = new File(directoryPath);
        System.out.println("222   " + directory);           // 222   E:\Java\forJavaIO\a\a

        // 检查目录是否存在
        if (!directory.exists()) {
            // 目录不存在，创建目录
            boolean isDirectoryCreated = directory.mkdirs();
            if (isDirectoryCreated) {
                System.out.println("Directory created successfully.");
            } else {
                System.out.println("Failed to create the directory.");
            }
        }

        // 检查文件是否存在
        if (!file2.exists()) {
            // 文件不存在，创建文件
            try {
                boolean isFileCreated = file2.createNewFile();
                if (isFileCreated) {
                    System.out.println("File created successfully.");
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file.");
                e.printStackTrace();
            }
        }

        // 使用 mkdirs() 方法可以确保创建所有必需的中间目录，而不仅仅是最后一个目录。
        // createNewFile() 方法只在文件不存在时创建文件，并返回 true。如果文件已存在，它将返回 false，并且不会覆盖现有文件。
        // createNewFile() 方法可能会抛出 IOException，因此在调用时需要处理这个异常。
        // 确保你有足够的权限在指定的路径创建目录和文件。
        // 考虑到文件系统的大小写敏感性，创建文件时使用的路径应该与现有路径的大小写匹配。
    }
}
