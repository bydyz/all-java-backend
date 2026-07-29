package org.rc.apiCollect.basicApi.lang.String;

import org.junit.jupiter.api.Test;

public class Main {
    String name;

    @Test
    public void test01() {
        Person p1 = new Person();
        p1.name = "Tom";
        Person p2 = new Person();
        p2.name = "Tom";

        System.out.println(p1.name.equals( p2.name));       // true
        System.out.println(p1.name == p2.name);             // true
        System.out.println(p1.name == "Tom");               // true
    }
}
