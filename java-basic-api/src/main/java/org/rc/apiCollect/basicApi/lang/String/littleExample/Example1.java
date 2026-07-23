package org.rc.apiCollect.basicApi.lang.String.littleExample;


import java.util.Scanner;

public class Example1 {
    public static void main(String[] args){
        //将用户输入的单词全部转为小写，如果用户没有输入单词，重新输入
        Scanner input = new Scanner(System.in);
        String word;
        while(true){
            System.out.print("请输入单词：");
            word = input.nextLine();
            if(word.trim().length()!=0){
                word = word.toLowerCase();
                break;
            }
        }
        System.out.println(word);
    }

}
