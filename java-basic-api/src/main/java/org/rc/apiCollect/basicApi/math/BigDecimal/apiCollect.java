package org.rc.apiCollect.basicApi.math.BigDecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class apiCollect {
    public static void main(String[] args) {
        BigDecimal bd = new BigDecimal("12435.351");
        BigDecimal bd2 = new BigDecimal("11");

        // System.out.println(bd.divide(bd2));
        System.out.println(bd.divide(bd2, RoundingMode.HALF_UP));
        System.out.println(bd.divide(bd2, 15, RoundingMode.HALF_UP));

    }
}
