package org.rc;

public class Main {
    public static void main(String[] args) {
        Person person = new Person();
        person.setName("张三");
        person.setAge(25);
        System.out.println(person);  // 自动调用生成的 toString()
    }
}
