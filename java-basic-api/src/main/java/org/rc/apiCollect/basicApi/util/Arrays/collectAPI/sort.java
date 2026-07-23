package org.rc.apiCollect.basicApi.util.Arrays.collectAPI;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

public class sort {
    // Arrays.sort 是 Java 中的一个方法，用于对数组进行排序。这个方法属于 java.util.Arrays 类，提供了多种重载版本以支持不同类型的数组排序。


    // 对基本数据类型数组进行排序
        // public static void sort(int[] a)
        // public static void sort(double[] a)

    // 对对象数组进行排序，使用自然顺序
        // public static void sort(Object[] a)

    // 对对象数组进行排序，使用指定的比较器
        // public static void sort(Object[] a, Comparator<? super T> c)


    // 对于基本数据类型数组（如 int[]、double[] 等），只有一个数组参数。
    // 对于对象数组，可以只传入数组参数，使用对象的自然顺序进行排序；或者传入数组和 Comparator 对象，使用指定的比较器进行排序。


    // Arrays.sort 方法没有返回值（即返回类型为 void），它直接在原数组上进行排序。即影响源数组

    @Test
    public void test01() {
        int[] intArray = {4, 2, 5, 1, 3};
        Arrays.sort(intArray);
        System.out.println(Arrays.toString(intArray)); // 输出: [1, 2, 3, 4, 5]
    }





    // 对象数组按自然顺序排序
    @Test
    public void test02() {
        String[] stringArray = {"Banana", "Apple", "Orange"};
        Arrays.sort(stringArray);
        System.out.println(Arrays.toString(stringArray)); // 输出: [Apple, Banana, Orange]
    }





    // 对象数组使用比较器排序
    @Test
    public void test03() {
        String[] stringArray = {"Banana", "Apple", "Orange"};
        Arrays.sort(stringArray, new Comparator<String>() {
            @Override
            // public int compare(String o1, String o2) {
            //     return o2.compareTo(o1); // 降序排序
            // }
            public int compare(String o1, String o2) {
                return o1.compareTo(o2); // 升序排序
            }
        });
        System.out.println(Arrays.toString(stringArray)); // 输出: [Orange, Banana, Apple]
    }

    // Arrays.sort 方法会对原数组进行修改，不会创建新的数组。
    // 对于对象数组，如果使用自然顺序排序，数组元素必须实现 Comparable 接口。
    // 使用 Comparator 可以提供更灵活的排序方式，不要求数组元素实现 Comparable 接口。
    // 从 Java 7 开始，Arrays.sort 对基本数据类型的排序方法采用了双路快速排序算法（Dual-Pivot Quicksort），对于对象数组则采用了 TimSort 算法（一种改进的归并排序）。







    // 在Java中，使用Arrays.sort方法并结合Comparator接口可以实现自定义排序逻辑。Comparator接口定义了一个compare方法，该方法接受两个参数并返回一个整数，以确定它们的排序顺序。
    public static void main(String[] args) {
        Person[] people = {
                new Person("John", 45),
                new Person("Jane", 30),
                new Person("Doe", 25)
        };

        // 使用Comparator进行排序
        Arrays.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return Integer.compare(p1.age, p2.age); // 升序排序
            }
        });

        // 打印排序后的数组
        System.out.println(Arrays.toString(people));
        // 输出: [Doe: 25, Jane: 30, John: 45]
    }

    // 我们创建了一个Person数组，并希望根据每个人的age属性进行升序排序。我们通过创建一个匿名内部类实现Comparator<Person>接口，并在compare方法中定义了排序逻辑。






    // Java 8 引入了Lambda表达式  使用Lambda表达式简化Comparator
    // 我们用Lambda表达式(p1, p2) -> Integer.compare(p1.age, p2.age)替换了匿名内部类，使得代码更加简洁。
        // Comparator接口的compare方法返回的整数表示第一个参数与第二个参数的比较结果。如果返回值小于0，表示第一个参数排在第二个参数前面；如果返回值大于0，表示第一个参数排在第二个参数后面；如果返回值等于0，表示两个参数相等，它们在排序中的位置可以互换。
        // 使用Comparator可以轻松实现复杂的排序逻辑，而不需要修改原始对象类。
        // Lambda表达式提供了一种更简洁的方式来实现Comparator接口，特别是当比较逻辑简单时。
    @Test
    public void test04() {
        Person[] people = {
                new Person("John", 45),
                new Person("Jane", 30),
                new Person("Doe", 25)
        };

        // 使用Lambda表达式进行排序
        Arrays.sort(people, (p1, p2) -> Integer.compare(p1.age, p2.age));

        // 打印排序后的数组
        System.out.println(Arrays.toString(people));
        // 输出: [Doe: 25, Jane: 30, John: 45]
    }







    // Java 8 引入了Lambda表达式  使用Lambda表达式简化Comparator
    // 我们用Lambda表达式(p1, p2) -> Integer.compare(p1.age, p2.age)替换了匿名内部类，使得代码更加简洁。
        // Comparator接口的compare方法返回的整数表示第一个参数与第二个参数的比较结果。如果返回值小于0，表示第一个参数排在第二个参数前面；如果返回值大于0，表示第一个参数排在第二个参数后面；如果返回值等于0，表示两个参数相等，它们在排序中的位置可以互换。
        // 使用Comparator可以轻松实现复杂的排序逻辑，而不需要修改原始对象类。
        // Lambda表达式提供了一种更简洁的方式来实现Comparator接口，特别是当比较逻辑简单时。
    @Test
    public void test05() {
        Person[] people = {
                new Person("John", 45),
                new Person("Jane", 30),
                new Person("Doe", 25)
        };

        // 使用Lambda表达式进行排序
        Arrays.sort(people, (p1, p2) -> Integer.compare(p2.age, p1.age));

        // 打印排序后的数组
        System.out.println(Arrays.toString(people));
        // 输出: [John: 45, Jane: 30, Doe: 25]
    }
}


class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name + ": " + age;
    }
}