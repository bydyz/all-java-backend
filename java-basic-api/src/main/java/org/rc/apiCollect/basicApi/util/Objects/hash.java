package org.rc.apiCollect.basicApi.util.Objects;

import org.junit.jupiter.api.Test;

import java.util.Objects;

public class hash {
    // Objects.hash() 有几个重载版本，最基本的是接受一个参数的版本：   public static int hash(Object... values)
    //     values: 一个或多个对象，用于计算散列码。
    //     返回一个整数散列码，它是传入对象散列码的组合。

    // Objects.hash() 是 Java 中一个非常有用的工具方法，它用于生成一个整数散列码（hash code），通常用于哈希表（如 HashMap、HashSet）中的键。这个方法在 Java 8 中被引入，位于 java.util.Objects 类中。

    // Objects.hash() 方法可以接受任意数量和类型的参数，这使得它非常灵活。
    // 如果传入的参数数组为空，Objects.hash() 返回 0。
    // 散列码的计算方式是将所有参数的散列码按位异或（XOR）组合，如果参数为 null，则使用 0 作为它的散列码。
    // 这个方法常用于自定义对象的 hashCode() 方法实现，特别是当对象由多个字段组成时。
    public static void main(String[] args) {
        // 创建一些对象
        String str = "Hello";
        int num = 123;
        double score = 99.99;

        // 使用 Objects.hash() 生成散列码
        int hashCode = Objects.hash(str, num, score);

        // 打印生成的散列码
        System.out.println("Hash code: " + hashCode);
    }

    @Test
    public void test1() {
        int hashCode1 = Objects.hash();
        System.out.println(hashCode1);      // 1

        int hashCode2 = Objects.hash(null);
        System.out.println(hashCode2);      // 0
    }
}
