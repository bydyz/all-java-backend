package org.rc.apiCollect.basicApi.util.Collection.Set;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;

public class HashSetExample {
    @Test
    public void test01(){
        HashSet set = new HashSet();
        set.add("张三");
        set.add("张三");
        set.add("李四");
        set.add("王五");
        set.add("王五");
        set.add("赵六");
        System.out.println("set = " + set); //不允许重复，无序
    }
    @Test
    public void test02(){
        HashSet set = new HashSet();
        set.add(new MyDate(2021,1,1));
        set.add(new MyDate(2021,1,1));
        set.add(new MyDate(2022,2,4));
        set.add(new MyDate(2022,2,4));
        System.out.println("set = " + set); //不允许重复，无序
    }
}


// 对于存放在 Set 容器中的对象，对应的类一定要重写 hashCode()和 equals(Objectobj)方法，以实现对象相等规则
class MyDate {
    private final int year;
    private final int month;
    private final int day;
    public MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // getClass() != o.getClass(): 这行代码检查当前对象和传入对象的类是否相同。getClass() 方法返回对象的运行时类。如果两个对象的类不同，它们不可能是相等的，因此方法返回 false。
        if (o == null || getClass() != o.getClass()) return false;
        MyDate myDate = (MyDate) o;
        return year == myDate.year &&
                month == myDate.month &&
                day == myDate.day;
    }
    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }
    @Override
    public String toString() {
        return "MyDate{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
