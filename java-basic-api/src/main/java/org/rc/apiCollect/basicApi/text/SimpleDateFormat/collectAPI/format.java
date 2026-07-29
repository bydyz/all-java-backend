package org.rc.apiCollect.basicApi.text.SimpleDateFormat.collectAPI;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class format {
    @Test
    public void test01() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(dateFormat1.format(currentDate));
    }
}
