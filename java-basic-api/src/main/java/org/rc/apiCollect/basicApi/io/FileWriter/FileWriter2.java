package org.rc.apiCollect.basicApi.io.FileWriter;

import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

public class FileWriter2 {
    //注意：应该使用 try-catch-finally 处理异常。这里出于方便阅读代码，使用了 throws 的方式
    @Test
    public void test() throws IOException {
        // 使用文件名称创建流对象
        FileWriter fw = new FileWriter("fw.txt");
        // 写出数据，通过 flush
        fw.write('刷'); // 写出第 1 个字符
        fw.flush();
        fw.write('新'); // 继续写出第 2 个字符，写出成功
        fw.flush();
        // 写出数据，通过 close
        fw.write('关'); // 写出第 1 个字符
        fw.close();

        // fw.write('闭'); // 继续写出第 2 个字符,【报错】java.io.IOException: Stream closed
        // fw.close();
    }

}
