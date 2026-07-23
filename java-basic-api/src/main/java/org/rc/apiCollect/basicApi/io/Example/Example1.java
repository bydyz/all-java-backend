package org.rc.apiCollect.basicApi.io.Example;
// 实现图片加密解密操作。

// buffer[i] = (byte) (buffer[i] ^ 5);
// 这行代码执行了一个位运算，具体是异或运算。异或运算的特点是：
//
//         如果两个比特位相同，结果是 0。
//         如果两个比特位不同，结果是 1。

// 加密
//         当你第一次对数据执行异或操作时，你实际上是在加密数据。例如，如果原始数据的某个字节是 11001010，与 5 进行异或后可能得到 10100011。
//
// 解密
//         由于异或运算是可逆的，如果你对已经加密的数据再次执行相同的异或操作（使用相同的密钥 5），你将得到原始数据。例如，再次对 10100011 执行异或 5 将恢复为 11001010。



// 异或加密是一种非常基础的加密方法，容易受到攻击，不应用于需要高安全性的场景。
// 由于异或操作的特性，加密和解密使用完全相同的操作，这意味着加密过程是自解密的。
// 异或加密对密钥的管理要求较高，密钥如果泄露，加密的数据将很容易被解密。
// 异或加密通常用于简单的数据隐藏，而不是严格的数据保护。
//
// 在实际的文件加密和解密中，通常会使用更复杂的加密算法和安全措施，例如对称密钥加密（如 AES）或非对称密钥加密（如 RSA），并结合初始化向量（IV）和密钥管理策略来提高安全性。

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Example1 {
    /*
     * 图片的加密
     * */
    @Test
    public void test1(){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File file1 = new File("pony.jpg");
            File file2 = new File("pony_secret.jpg");
            fis = new FileInputStream(file1);
            fos = new FileOutputStream(file2);
            //方式 1：每次读入一个字节，效率低
            // int data;
            // while((data = fis.read()) != -1){
            // fos.write(data ^ 5);
            // }
            //方式 2：每次读入一个字节数组，效率高
            int len;
            byte[] buffer = new byte[1024];
            while((len = fis.read(buffer)) != -1){
                for(int i = 0; i < len; i++){
                    buffer[i] = (byte) (buffer[i] ^ 5);
                }
                fos.write(buffer,0, len);
            }
            System.out.println("加密成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /*
     * 图片的解密
     * */
    @Test
    public void test2(){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File file1 = new File("pony_secret.jpg");
            File file2 = new File("pony_unsecret.jpg");
            fis = new FileInputStream(file1);
            fos = new FileOutputStream(file2);
            //方式 1：每次读入一个字节，效率低
            // int data;
            // while((data = fis.read()) != -1){
            // fos.write(data ^ 5);
            // }
            //方式 2：每次读入一个字节数组，效率高
            int len;
            byte[] buffer = new byte[1024];
            while((len = fis.read(buffer)) != -1){
                for(int i = 0;i < len;i++){
                    buffer[i] = (byte) (buffer[i] ^ 5);
                }
                fos.write(buffer,0,len);
            }
            System.out.println("解密成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
