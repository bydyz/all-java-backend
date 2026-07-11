package org.rc.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 演示 @ToString 和 @EqualsAndHashCode 注解
 */
@ToString(of = {"name", "price"})  // 只包含 name 和 price 字段
@EqualsAndHashCode(of = {"id"})    // 只用 id 字段进行比较
public class Product {

    private String id;
    private String name;
    private double price;
    private String category;

    public Product(String id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
