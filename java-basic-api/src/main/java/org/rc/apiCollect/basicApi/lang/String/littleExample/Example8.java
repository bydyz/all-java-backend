package org.rc.apiCollect.basicApi.lang.String.littleExample;

import org.junit.jupiter.api.Test;

public class Example8 {
    // JDK13 的新特性
    // 使用"""作为文本块的开始符和结束符，在其中就可以放置多行的字符串，不需要进行任何转义。因此，文本块将提高 Java 程序的可读性和可写性。
    //
    // 基本使用：
    //     """
    //     line1
    //     line2
    //     line3
    //     """
    // 相当于：
    //     "line1\nline2\nline3\n"


    // 如果字符串末尾不需要行终止符，则结束分隔符可以放在最后一行内容上。例
    // 如：
    //     """
    //     line1
    //     line2
    //     line3"""
    // 相当于
    //     "line1\nline2\nline3"


    // """
    // <html>
    //  <body>
    //  <p>Hello, world</p>
    //  </body>
    // </html>
    // """;
    //
    // """
    // SELECT id,NAME,email
    // FROM customers
    // WHERE id > 4
    // ORDER BY email DESC
    // """;
    //
    // JSON 字符串
    //     原有方式：
    //     String myJson = "{\n" +
    //             " \"name\":\"Song Hongkang\",\n" +
    //             " \"address\":\"www.atguigu.com\",\n" +
    //             " \"email\":\"shkstart@126.com\"\n" +
    //             "}";
    //     使用新特性：
    //     String myJson1 = """
    //                      {
    //                      "name":"Song Hongkang",
    //                      "address":"www.atguigu.com",
    //                      "email":"shkstart@126.com"
    //                      }""";

    @Test
    public void test5(){
        String sql1 = """
                     SELECT id,NAME,email
                     FROM customers
                     WHERE id > 4
                     ORDER BY email DESC
                     """;
        System.out.println(sql1);

        // \    取消换行操作
        // \s   表示一个空格
        String sql2 = """
                     SELECT id,NAME,email \
                     FROM customers\s\
                     WHERE id > 4 \
                     ORDER BY email DESC
                     """;
        System.out.println(sql2);
    }

}
