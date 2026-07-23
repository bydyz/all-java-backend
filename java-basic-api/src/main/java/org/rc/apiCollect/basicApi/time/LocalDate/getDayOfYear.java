package org.rc.apiCollect.basicApi.time.LocalDate;

import java.time.LocalDate;

public class getDayOfYear {
    public static void main(String[] args) {
        LocalDate a = LocalDate.of(2024, 6, 9);
        System.out.println(a);

        System.out.println(a.getDayOfYear());
    }
}
