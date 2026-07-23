package org.rc.apiCollect.basicApi.io.File;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

public class File6 {
    @Test
    public void test01() {
        File dir = new File("e:/0/1/1-1");
        // 返回一个 String 数组，表示该 File 目录中的所有子文件或目录。
        String[] subs = dir.list();
        System.out.println(subs);
        System.out.println(Arrays.toString(subs));
        for (String sub : subs) {
            System.out.println(sub);
        }
    }

    @Test
    public void test02() {
        File dir = new File("e:/0/1/1-1");
        // 返回一个 File 数组，表示该 File 目录中的所有的子文件或目录。
        File[] file = dir.listFiles();
        System.out.println(file);
        System.out.println(Arrays.toString(file));
        for (File sub : file) {
            System.out.println(sub);
        }
    }
}
