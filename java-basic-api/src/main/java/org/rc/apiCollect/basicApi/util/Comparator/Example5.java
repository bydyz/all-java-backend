package org.rc.apiCollect.basicApi.util.Comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Example5 {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 30));
        people.add(new Person("Bob", 25));
        people.add(new Person("Charlie", 35));
        people.add(new Person("David", 30));

        System.out.println("Before sorting: " + people);

        // 先按年龄升序排序，如果年龄相同则按名字字典序排序
        people.sort(Comparator.comparingInt(Person::getAge)
                .thenComparing(Person::getName));

        System.out.println("After sorting by age and then by name: " + people);
    }
}
