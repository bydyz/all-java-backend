package org.rc;

public class TwoPrint {
    public static void main(String[] args) {
        // 换行
        System.out.println("HelloWorld println!!");

        // 不换行
        System.out.print("HelloWorld print!!");
        System.out.print("HelloWorld print!!");
    }
}

// .java中含有文字是，利用javac命令会报错，是因为源文件的字符编码不对，在利用.javac时，指定 源文件的字符编码 即可