package org.rc.apiCollect.basicApi.util.Comparator;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

public class Example1 {

    // 思考
    //     – 当元素的类型没有实现 java.lang.Comparable 接口而又不方便修改代码（例如：一些第三方的类，你只有.class 文件，没有源文件）
    //     – 如果一个类，实现了 Comparable 接口，也指定了两个对象的比较大小的规则，但是此时此刻我不想按照它预定义的方法比较大小，但是我又不能
    //         随意修改，因为会影响其他地方的使用，怎么办？
    //
    // JDK 在设计类库之初，也考虑到这种情况，所以又增加了一个 java.util.Comparator 接口。强行对多个对象进行整体排序的比较。
    //     – 重写 compare(Object o1,Object o2)方法，比较 o1 和 o2 的大小：如果方法返回正整数，则表示 o1 大于 o2；
    //         如果返回 0，表示相等；返回负整数，表示 o1 小于 o2。
    //     – 可以将 Comparator 传递给 sort 方法（如 Collections.sort 或 Arrays.sort），从而允许在排序顺序上实现精确控制。


    public static void main(String[] args) {
        Student[] arr = new Student[5];
        arr[0] = new Student(3, "张三", 90, 23);
        arr[1] = new Student(1, "熊大", 100, 22);
        arr[2] = new Student(5, "王五", 75, 25);
        arr[3] = new Student(4, "李四", 85, 24);
        arr[4] = new Student(2, "熊二", 85, 18);
        System.out.println("所有学生：");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
        System.out.println("按照成绩排序，成绩相同再比较id");
        StudentScoreComparator sc = new StudentScoreComparator();
        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i; j++) {
                if (sc.compare(arr[j], arr[j + 1]) > 0) {
                    Student temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
    
    @Test
    public void test1() {
        Student[] students = new Student[5];
        students[0] = new Student(3, "张三", 90, 23);
        students[1] = new Student(1, "熊大", 100, 22);
        students[2] = new Student(5, "王五", 75, 25);
        students[3] = new Student(4, "李四", 85, 24);
        students[4] = new Student(2, "熊二", 85, 18);
        System.out.println(Arrays.toString(students));
        //定制排序
        StudentScoreComparator sc = new StudentScoreComparator();
        Arrays.sort(students, sc);
        System.out.println("排序之后：");
        System.out.println(Arrays.toString(students));
    }


}

class StudentScoreComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Student s1 = (Student) o1;
        Student s2 = (Student) o2;
        int result = s1.getScore() - s2.getScore();
        return result != 0 ? result : s1.getId() - s2.getId();
    }
}

class Student {
    private int id;
    private String name;
    private int score;
    private int age;
    public Student(int id, String name, int score, int age) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.age = age;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", age=" + age +
                '}';
    }
}