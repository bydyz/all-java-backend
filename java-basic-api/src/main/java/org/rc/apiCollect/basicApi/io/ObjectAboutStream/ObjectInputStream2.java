package org.rc.apiCollect.basicApi.io.ObjectAboutStream;

import org.junit.jupiter.api.Test;

import java.io.*;

public class ObjectInputStream2 {
    @Test
    public void save() throws IOException {
        Employee.setCompany("尚硅谷");
        Employee e = new Employee("小谷姐姐", "宏福苑", 23);
        System.out.println("000   " + e);
        // 创建序列化流对象
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("employee.dat"));
        // 写出对象
        oos.writeObject(e);
        // 释放资源
        oos.close();
        System.out.println("Serialized data is saved"); // 姓名，地址被序列化，年龄没有被序列化。
    }
    @Test
    public void reload() throws IOException, ClassNotFoundException {
        // 创建反序列化流
        FileInputStream fis = new FileInputStream("employee.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        // 读取一个对象
        Employee e = (Employee) ois.readObject();
        // 方法也可调用
        System.out.println("000   " + e.getName());
        // 释放资源
        ois.close();
        fis.close();
        // age = 0
        System.out.println(e);
    }
}


class Employee implements Serializable {
    //static final long serialVersionUID = 23234234234L;
    public static String company; //static 修饰的类变量，不会被序列化
    public String name;
    public String address;
    public transient int age; // transient 瞬态修饰成员,不会被序列化
    public Employee(String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }
    public static String getCompany() {
        return company;
    }
    public static void setCompany(String company) {
        Employee.company = company;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                ", company=" + company +
                '}';
    }
}
