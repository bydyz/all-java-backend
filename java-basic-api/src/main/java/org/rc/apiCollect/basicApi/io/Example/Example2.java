package org.rc.apiCollect.basicApi.io.Example;

// import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Example2 {
    public static void main(String[] args)throws Exception {
        //- 静态方法：IOUtils.copy(InputStream in,OutputStream out)传递字节流，实现文件复制。
        // IOUtils.copy(new FileInputStream("E:\\0\\2.txt"), new FileOutputStream("E:\\1\\2.txt"));


    }

    @Test
    public void test0() {
        //- 静态方法：IOUtils.closeQuietly(任意流对象)悄悄的释放资源，自动处理 close()方法抛出的异常。
        // FileWriter fw = null;
        // try {
        //     fw = new FileWriter("E:\\1\\2.txt");
        //     fw.write("hahah");
        // } catch (IOException e) {
        //     e.printStackTrace();
        // } finally {
        //     IOUtils.closeQuietly(fw);
        // }
    }

}
