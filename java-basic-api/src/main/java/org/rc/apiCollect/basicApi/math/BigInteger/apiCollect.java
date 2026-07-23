package org.rc.apiCollect.basicApi.math.BigInteger;

import java.math.BigInteger;

public class apiCollect {
    public static void main(String[] args) {
        // long bigNum = 123456789123456789123456789L;     // 数字太大
        BigInteger b1 = new BigInteger("12345678912345678912345678");
        BigInteger b2 = new BigInteger("78923456789123456789123456789");
        //System.out.println("和：" + (b1+b2));//错误的，无法直接使用+进行求和
        System.out.println("和：" + b1.add(b2));
        System.out.println("减：" + b1.subtract(b2));
        System.out.println("乘：" + b1.multiply(b2));
        System.out.println("除：" + b2.divide(b1));
        System.out.println("余：" + b2.remainder(b1));
    }
}
