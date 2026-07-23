package org.rc.apiCollect.basicApi.io.InOutStream;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class InOutStream1 {
    public static void main(String[] args) {
        System.out.println("请输入信息(退出输入 e 或 exit):");
        // 把"标准"输入流(键盘输入)这个字节流包装成字符流,再包装成缓冲流        System.in 既是从键盘输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        try {
            while ((s = br.readLine()) != null) { // 读取用户输入的一行数据 --> 阻塞程序
                if ("e".equalsIgnoreCase(s) || "exit".equalsIgnoreCase(s)) {
                    System.out.println("安全退出!!");
                    break;
                }
                // 将读取到的整行字符串转成大写输出
                System.out.println("-->:" + s.toUpperCase());
                System.out.println("继续输入信息");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close(); // 关闭过滤流时,会自动关闭它包装的底层节点流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Test
    public void test0() {
        // JDK9 的新特性
        // try 的前面可以定义流对象，try 后面的()中可以直接引用流对象的名称。在 try
        // 代码执行完毕后，流对象也可以释放掉，也不用写 finally 了。

        InputStreamReader reader = new InputStreamReader(System.in);
        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        try (reader; writer) {
            //reader 是 final 的，不可再被赋值
            // reader = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
