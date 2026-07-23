package org.rc.apiCollect.basicApi.io.File;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class File3 {
    @Test
    public void test1() throws IOException {
        // 文件路径名
        // 如果 aaa.txt 文件存在于指定的路径上，你可以执行以下操作：
        //     读取文件内容。
        //     获取文件的属性，如大小、最后修改时间等。
        //     检查文件的可读性、可写性等。
        // 如果 aaa.txt 文件不存在于指定的路径上，以下是可能的操作和结果：
        //     尝试读取文件将导致 FileNotFoundException。
        //     可以尝试创建文件，使用 file1.createNewFile() 方法。
        //     可以检查文件是否存在，使用 file1.exists() 方法。

        // 使用 file1.exists() 可以检查文件是否存在。
        // 使用 file1.createNewFile() 尝试创建文件，如果文件已经存在，则此方法返回 false。
        // File 类提供的方法通常不抛出异常，它们通过返回布尔值或其他状态码来指示操作的成功或失败。
        // 如果你需要处理文件的输入输出，可能需要使用 java.io.FileInputStream、java.io.FileOutputStream 或其他相关的 I/O 类。
        // 确保在访问文件系统时处理异常，如 FileNotFoundException。
        String pathname = "D:\\aaa.txt";
        File file1 = new File(pathname);
        System.out.println("000   " + file1);
        noExitAndCreat(file1);
    }
    // 只是针对 每个盘符下的 直接子文件
    public void noExitAndCreat(File file) throws IOException {
        // 检查文件是否存在
        if (file.exists()) {
            System.out.println("File exists.");
            // 可以执行读取文件等操作
        } else {
            System.out.println("File does not exist.");
            // 文件不存在，可以选择创建文件
            if (!file.createNewFile()) {
                System.out.println("Failed to create the file.");
            } else {
                System.out.println("File created successfully.");
            }
        }
    }
}
