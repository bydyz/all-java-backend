package org.rc.apiCollect.basicApi.time.LocalDate;

import java.time.LocalDate;

public class minusDays {
    public static void main(String[] args) {
        LocalDate a = LocalDate.of(2024, 6, 9);
        System.out.println(a);

        LocalDate b = a.minusDays(20);
        System.out.println(b);
    }
}
