package org.rc.apiCollect.basicApi.text.SimpleDateFormat.collectAPI;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class parse {
    public static void main(String[] args) {
        String str1 = "2024-06-08 10:30:00";

        // 解析所需要  SimpleDateFormat 的参数 须与时间格式一致，且参数可少，否则会报错。
        SimpleDateFormat strParse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 1.用 try...catch... 包裹可能产生问题的代码，不然会编译不通过
        try {
            Date date = strParse1.parse(str1);
            System.out.println(date);

            String str2 = strParse1.format(date);
            System.out.println(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    // 2. throws ParseException 包裹可能产生问题的代码，不然会编译不通过
    @Test
    public void test01() throws ParseException {
        String str = "2022 年 06 月 06 日 16 时 03 分 14 秒 545 毫秒 星期四 +0800";
        // SimpleDateFormat sf = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH 时 mm 分 ss 秒 SSS 毫秒 E Z");
        // SimpleDateFormat sf = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH 时 mm 分 ss 秒");
        // SimpleDateFormat sf = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
        // SimpleDateFormat sf = new SimpleDateFormat("yyyy 年 MM 月");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy 年");

        // 报错
        // SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sf.parse(str);
        System.out.println(d);

    }
}
