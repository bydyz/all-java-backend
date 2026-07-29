package org.rc.apiCollect.basicApi.util.Map.CExample;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

public class Example2 {
    @Test
    public void test0() {
        System.out.println("夜曲".compareTo("双节棍"));
    }
    @Test
    public void test1() {
        Singer singer1 = new Singer("周杰伦");
        Singer singer2 = new Singer("陈奕迅");

        Song song1 = new Song("双节棍");
        Song song2 = new Song("本草纲目");
        Song song3 = new Song("夜曲");
        Song song4 = new Song("浮夸");
        Song song5 = new Song("十年");
        Song song6 = new Song("孤勇者");

        HashSet h1 = new HashSet();// 放歌手一的歌曲
        h1.add(song1);
        h1.add(song2);
        h1.add(song3);

        HashSet h2 = new HashSet();// 放歌手二的歌曲
        h2.add(song4);
        h2.add(song5);
        h2.add(song6);

        HashMap hashMap = new HashMap();// 放歌手和他对应的歌曲
        hashMap.put(singer1, h1);
        hashMap.put(singer2, h2);
        for (Object obj : hashMap.keySet()) {
            System.out.println(obj + "=" + hashMap.get(obj));
        }
    }
}
