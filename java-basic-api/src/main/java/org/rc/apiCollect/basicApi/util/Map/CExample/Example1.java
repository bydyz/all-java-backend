package org.rc.apiCollect.basicApi.util.Map.CExample;

import java.util.*;

public class Example1 {
    public static void main(String[] args) {
        //创建一个 HashMap 用于保存歌手和其歌曲集
        HashMap singers = new HashMap();


        //声明一组 key,value
        String singer1 = "周杰伦";
        ArrayList songs1 = new ArrayList();
        songs1.add("双节棍");
        songs1.add("本草纲目");
        songs1.add("夜曲");
        songs1.add("稻香");
        System.out.println("111   " + songs1);
        //添加到 map 中
        singers.put(singer1, songs1);
        System.out.println("222   " + singers);


        //声明一组 key,value
        String singer2 = "陈奕迅";
        List songs2 = Arrays.asList("浮夸", "十年", "红玫瑰", "好久不见", "孤勇者");
        System.out.println("333   " + songs2);
        //添加到 map 中
        singers.put(singer2, songs2);
        System.out.println("444   " + singers);


        //遍历 map
        Set entrySet = singers.entrySet();
        for(Object obj : entrySet){
            Map.Entry entry = (Map.Entry)obj;
            String singer = (String) entry.getKey();
            // 上面一个是ArrayList  一个是List
            List songs = (List) entry.getValue();
            System.out.println("歌手：" + singer);
            System.out.println("歌曲有：" + songs);
        }
    }
}
