package org.rc.apiCollect.basicApi.io.BufferedAbout;

import org.junit.jupiter.api.Test;

import java.io.*;

public class Buffered1 {
    //方法 1：使用 FileInputStream\FileOutputStream 实现非文本文件的复制
    public void copyFileWithFileStream(String srcPath,String destPath){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //1. 造文件-造流
            fis = new FileInputStream(new File(srcPath));
            fos = new FileOutputStream(new File(destPath));

            //2. 复制操作（读、写）
            byte[] buffer = new byte[100];
            int len;//每次读入到 buffer 中字节的个数
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            System.out.println("复制成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //3. 关闭资源
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Test
    public void test1(){
        String srcPath = "C:\\Users\\shkstart\\Desktop\\01-复习.mp4";
        String destPath = "C:\\Users\\shkstart\\Desktop\\01-复习 2.mp4";
        long start = System.currentTimeMillis();
        copyFileWithFileStream(srcPath,destPath);
        long end = System.currentTimeMillis();
        System.out.println("花费的时间为：" + (end - start));//7677 毫秒
    }






    //方法 2：使用 BufferedInputStream\BufferedOuputStream 实现非文本文件的复制
    public void copyFileWithBufferedStream(String srcPath,String destPath){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            //1. 造文件
            File srcFile = new File(srcPath);
            File destFile = new File(destPath);
            //2. 造流
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(fos);
            //3. 读写操作
            int len;
            byte[] buffer = new byte[100];
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            System.out.println("复制成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4. 关闭资源(如果有多个流，我们需要先关闭外面的流，再关闭内部的流)
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Test
    public void test2(){
        String srcPath = "C:\\Users\\shkstart\\Desktop\\01-复习.mp4";
        String destPath = "C:\\Users\\shkstart\\Desktop\\01-复习 2.mp4";
        long start = System.currentTimeMillis();
        copyFileWithBufferedStream(srcPath,destPath);
        long end = System.currentTimeMillis();
        // 看来，加上缓冲流，时间会大大减少。那么，对于什么时候使用缓冲流有个标准吗
        System.out.println("花费的时间为：" + (end - start));//415 毫秒
    }
}
