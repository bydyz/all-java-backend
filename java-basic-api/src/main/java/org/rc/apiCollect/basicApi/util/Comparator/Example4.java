package org.rc.apiCollect.basicApi.util.Comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Example4 {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 30));
        people.add(new Person("Bob", 25));
        people.add(new Person("Charlie", 35));

        System.out.println("Before sorting: " + people);

        // 使用 Lambda 表达式定义 Comparator
        people.sort((p1, p2) -> Integer.compare(p1.getAge(), p2.getAge()));

        // 或者更简洁的方式
        people.sort(Comparator.comparingInt(Person::getAge));

        System.out.println("After sorting by age: " + people);
    }
}
