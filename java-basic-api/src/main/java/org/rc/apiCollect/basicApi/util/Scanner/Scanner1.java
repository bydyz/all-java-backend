package org.rc.apiCollect.basicApi.util.Scanner;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Scanner1 {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        // 再main方法中，文件会从当前项目的目录添加
        PrintStream ps = new PrintStream("1.txt");
        while(true){
            System.out.print("请输入一个单词：");
            String str = input.nextLine();
            if("stop".equals(str)){
                break;
            }
            ps.println(str);
        }
        input.close();
        ps.close();
    }
}
