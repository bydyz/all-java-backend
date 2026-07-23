package org.rc.apiCollect.basicApi.util.Collection.List.CSon.ArrayList;

import java.util.ArrayList;
import java.util.Scanner;

public class ExampleTwo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList stuList = new ArrayList();
        for (;;) {
            System.out.println("选择（录入 1 ；结束 0）");
            int x = scanner.nextInt();//根据 x 的值，判断是否需要继续循环
            if (x == 1) {
                System.out.println("姓名");
                String name = scanner.next();
                System.out.println("年龄");
                int age = scanner.nextInt();
                Student stu = new Student(age, name);
                // ArrayList实例 的add
                stuList.add(stu);
            } else if (x == 0) {
                // break 结束 for 循环
                break;
            } else {
                System.out.println("输入有误，请重新输入");
            }
        }
        // 此处用的 Object
        for (Object stu : stuList) {
            System.out.println(stu);
        }
    }
}

class Student {
    private int age;
    private String name;
    public Student() {
    }

    public Student(int age, String name) {
        super();
        this.age = age;
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Student [age=" + age + ", name=" + name + "]";
    }
}
