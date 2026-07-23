package org.rc.apiCollect.basicApi.lang.Comparable;

import java.util.Arrays;

public class Example3 {
    public static void main(String[] args) {
        Goods[] all = new Goods[4];
        all[0] = new Goods("《红楼梦》", 100);
        all[1] = new Goods("《西游记》", 80);
        all[2] = new Goods("《三国演义》", 140);
        all[3] = new Goods("《水浒传》", 120);
        System.out.println(Arrays.toString(all));

        Arrays.sort(all);
        // 没有重写 toString 时，下面用默认的 toString 类似 com.base.lang.Comparable.Goods@3d075dc0
        System.out.println(Arrays.toString(all));
    }

}

class Goods implements Comparable {
    private final String name;
    private final double price;
    public Goods(String name, double price) {
        this.name = name;
        this.price = price;
    }

    //按照价格，比较商品的大小

    @Override
    public String toString() {
        return "{name: " + this.name + "," + "price: " + this.price + "}";
    }
    @Override
    public int compareTo(Object o) {
        if(o instanceof Goods other) {
            if (this.price > other.price) {
                return 1;
            } else if (this.price < other.price) {
                return -1;
            }
            return 0;
        }
        throw new RuntimeException("输入的数据类型不一致");
    }
    //构造器、getter、setter、toString()方法略
}