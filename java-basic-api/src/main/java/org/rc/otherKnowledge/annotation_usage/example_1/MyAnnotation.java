package org.rc.otherKnowledge.annotation_usage.example_1;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)                 // 运行时可读
@Target({ElementType.TYPE, ElementType.METHOD})     // 可修饰的位置
@Documented                                          // 生成 javadoc
@Inherited                                           // 类继承时子类可见（仅类）
public @interface MyAnnotation {
    String value() default "";                       // 名字为 value 可省略 key
    int priority() default 0;
    Class<?>[] handlers() default {};
    RetentionPolicy policy() default RetentionPolicy.CLASS;
    Nested nested() default @Nested;                 // 嵌套注解需要默认值
}
