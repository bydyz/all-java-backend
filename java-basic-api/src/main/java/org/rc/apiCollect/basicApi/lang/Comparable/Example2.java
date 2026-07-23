package org.rc.apiCollect.basicApi.lang.Comparable;

import java.util.Arrays;

public class Example2 {
    public static void main(String[] args) {
        Student2[] students = new Student2[3];
        students[0] = new Student2("张三", 96);
        students[1] = new Student2("李四", 85);
        students[2] = new Student2("王五", 98);

        // Arrays.toString 中 的数组的item表示需要依仗Student2的 toString方法
        System.out.println(Arrays.toString(students));

        // Arrays.sort 需要依仗Student2的 compareTo方法
        Arrays.sort(students);

        System.out.println(Arrays.toString(students));
    }
}

class Student2 implements Comparable {
    private String name;
    private int score;
    public Student2(String name, int score) {
        this.name = name;
        this.score = score;
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
    @Override
    public String toString() {
        return "Student2{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
    @Override
    public int compareTo(Object o) {
        return this.score - ((Student2)o).score;
    }
}
