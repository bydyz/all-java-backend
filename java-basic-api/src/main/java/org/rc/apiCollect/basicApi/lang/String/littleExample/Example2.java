package org.rc.apiCollect.basicApi.lang.String.littleExample;

import java.util.Random;
import java.util.Scanner;

public class Example2 {
    public static void main(String[] args) {
        //随机生成验证码，验证码由 0-9，A-Z,a-z 的字符组成
        char[] array = new char[26*2+10];
        for (int i = 0; i < 10; i++) {
            array[i] = (char)('0' + i);
        }
        for (int i = 10, j = 0; i < 10 + 26; i++, j++) {
            array[i] = (char)('A' + j);
        }
        for (int i = 10 + 26, j = 0; i < array.length; i++, j++) {
            array[i] = (char)('a' + j);
        }
        System.out.println(array);          // 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz

        String code = "";
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            System.out.println(rand.nextInt(array.length));
            code += array[rand.nextInt(array.length)];
        }
        System.out.println("验证码：" + code);

        //将用户输入的单词全部转为小写，如果用户没有输入单词，重新输入
        Scanner input = new Scanner(System.in);
        System.out.print("请输入验证码：");
        String inputCode = input.nextLine();
        if(!code.equalsIgnoreCase(inputCode)){
            System.out.println("验证码输入不正确");
        } else {
            System.out.println("验证码输入正确");
        }

    }
}
