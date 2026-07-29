package org.rc.apiCollect.aboutWrapperClass.collectAPI;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class compareTo {
    // 在 Java 中，compareTo 方法是 Comparable 接口的一部分，用于比较两个对象的顺序。当一个类实现了 Comparable 接口，它必须提供 compareTo 方法的实现，以定义对象的自然排序顺序。

    // 方法签名  public int compareTo(T o)
         // o: 要比较的另一个对象。

        // 如果当前对象（this）小于参数对象（o），返回一个负整数。
        // 如果当前对象（this）等于参数对象（o），返回 0。
        // 如果当前对象（this）大于参数对象（o），返回一个正整数。

    @Test
    public void test01() {
        Integer num1 = 3;
        Integer num2 = 7;
        Integer num3 = 5;

        // 是默认有重写吗？？？？？？
        boolean isBetween = (num1.compareTo(num2) < 0) && (num2.compareTo(num3) > 0);
        System.out.println(isBetween);      // 输出: false
    }


    public static void main(String[] args) {
        Person p1 = new Person("Alice", 30);
        Person p2 = new Person("Bob", 25);
        Person p3 = new Person("Charlie", 30);

        int result = p1.compareTo(p2); // 返回 1，因为 30 > 25
        System.out.println("Comparison result: " + result);

        result = p1.compareTo(p3); // 返回 0，因为 30 == 30
        System.out.println("Comparison result: " + result);
    }


    @Test
    public void test02() {
        Person[] people = {new Person("Dave", 40), new Person("Eve", 20)};
        Arrays.sort(people); // 使用自然排序顺序

        for (Person person : people) {
            System.out.println(person);
        }
            // 输出:
            // Eve (20)
            // Dave (40)
    }

    // 实现 Comparable 接口的类必须确保 compareTo 方法是一致的，即对于任何三个对象 x、y 和 z，如果 x.compareTo(y) <= 0 且 y.compareTo(z) <= 0，则 x.compareTo(z) <= 0。
    // compareTo 方法应该是对称的，即 x.compareTo(y) 应该等于 -(y.compareTo(x))。
    // compareTo 方法应该对 null 值敏感。如果传入的参数为 null，应该抛出 NullPointerException。
    // compareTo 方法应该与 equals 方法保持一致，即如果 x.equals(y) 返回 true，则 x.compareTo(y) 应该返回 0。
}


class Person implements Comparable<Person> {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age, other.age); // 根据年龄比较
    }

    @Override
    public String toString() {
        return name + " (" + age + ")";
    }
}