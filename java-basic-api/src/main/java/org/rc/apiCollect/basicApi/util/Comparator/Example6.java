package org.rc.apiCollect.basicApi.util.Comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Example6 {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 30));
        people.add(new Person("Bob", 25));
        people.add(new Person("Charlie", 35));

        System.out.println("Before sorting: " + people);

        // 按年龄降序排序
        people.sort(Comparator.comparingInt(Person::getAge).reversed());

        System.out.println("After sorting by age in descending order: " + people);
    }
}
