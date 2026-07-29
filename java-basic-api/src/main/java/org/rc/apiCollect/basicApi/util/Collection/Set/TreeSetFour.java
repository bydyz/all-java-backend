package org.rc.apiCollect.basicApi.util.Collection.Set;
// 定义一个 Employee 类。 该类包含：private 成员变量 name,age,birthday，其中birthday 为 MyDate1 类的对象； 并为每一个属性定义 getter, setter 方法； 并重写 toString 方法输出 name, age, birthday
//
// MyDate1 类包含: private 成员变量 year,month,day；并为每一个属性定义 getter,setter 方法；
//
// 创建该类的 5 个对象，并把这些对象放入 TreeSet 集合中（下一章：TreeSet 需使用泛型来定义）
//
// 分别按以下两种方式对集合中的元素进行排序，并遍历输出：
//         1). 使 Employee 实现 Comparable 接口，并按 name 排序
//         2). 创建 TreeSet 时传入 Comparator 对象，按生日日期的先后排序。


import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Comparator;
import java.util.TreeSet;

public class TreeSetFour {
    /*
 自然排序：
 创建该类的 5 个对象，并把这些对象放入 TreeSet 集合中
 * 需求 1：使 Employee 实现 Comparable 接口，并按 name 排序
 * */
    @Test
    public void test1(){
        TreeSet set = new TreeSet();
        Employee e1 = new Employee("Tom",23, new MyDate1(1999,7,9));
        Employee e2 = new Employee("Rose",43, new MyDate1(1999,7,19));
        Employee e3 = new Employee("Jack",54, new MyDate1(1998,12,21));
        Employee e4 = new Employee("Jerry",12, new MyDate1(2002,4,21));
        Employee e5 = new Employee("Tony",22, new MyDate1(2001,9,12));
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);

        //遍历
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }

    /*
     * 定制排序：
     * 创建 TreeSet 时传入 Comparator 对象，按生日日期的先后排序。
     * */
    @Test
    public void test2(){
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof Employee e1 && o2 instanceof Employee e2){
                    //对比两个 employee 的生日的大小
                    MyDate1 birth1 = e1.getBirthday();
                    MyDate1 birth2 = e2.getBirthday();

                    //方式 1：
                    // int yearDistance = birth1.getYear() - birth2.getYear();
                    // if(yearDistance != 0){
                    // return yearDistance;
                    // }
                    // int monthDistance = birth1.getMonth() - birth2.getMonth();
                    // if(monthDistance != 0){
                    // return monthDistance;
                    // }
                    //
                    // return birth1.getDay() - birth2.getDay();

                    //方式 2：
                    return birth1.compareTo(birth2);
                }
                throw new RuntimeException("输入的类型不匹配");
            }
        };
        TreeSet set = new TreeSet(comparator);
        Employee e1 = new Employee("Tom",23,new MyDate1(1999,7,9));
        Employee e2 = new Employee("Rose",43,new MyDate1(1999,7,19));
        Employee e3 = new Employee("Jack",54,new MyDate1(1998,12,21));
        Employee e4 = new Employee("Jerry",12,new MyDate1(2002,4,21));
        Employee e5 = new Employee("Tony",22,new MyDate1(2001,9,12));
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);
        //遍历
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}

class MyDate1 implements Comparable{
    private int year;
    private int month;
    private int day;
    public MyDate1() {
    }
    public MyDate1(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    @Override
    public String toString() {
        // return "MyDate1{" +
        // "year=" + year +
        // ", month=" + month +
        // ", day=" + day +
        // '}';
        return year + "年" + month + "月" + day + "日";
    }
    @Override
    public int compareTo(Object o) {
        if(this == o){
            return 0;
        }
        if(o instanceof MyDate1 myDate){
            int yearDistance = this.getYear() - myDate.getYear();
            if(yearDistance != 0){
                return yearDistance;
            }
            int monthDistance = this.getMonth() - myDate.getMonth();
            if(monthDistance != 0){
                return monthDistance;
            }
            return this.getDay() - myDate.getDay();
        }
        throw new RuntimeException("输入的类型不匹配");
    }
}

class Employee implements Comparable{
    private String name;
    private int age;
    private MyDate1 birthday;
    public Employee() {
    }
    public Employee(String name, int age, MyDate1 birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public MyDate1 getBirthday() {
        return birthday;
    }
    public void setBirthday(MyDate1 birthday) {
        this.birthday = birthday;
    }
    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", birthday=" + birthday +
                '}';
    }
    @Override
    public int compareTo(Object o) {
        if(o == this){
            return 0;
        }
        if(o instanceof Employee emp){
            return this.name.compareTo(emp.name);
        }
        throw new RuntimeException("传入的类型不匹配");
    }
}
