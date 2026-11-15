package org.rc.otherKnowledge.annotation_usage.example_1;

import java.lang.annotation.RetentionPolicy;

@MyAnnotation(value = "demo", priority = 1)
public class Demo {

    @MyAnnotation("methodLevel")          // value 可省略 key
    public void doSomething() {}

    @MyAnnotation(
            priority = 5,
            handlers = {Runnable.class},
            policy = RetentionPolicy.RUNTIME
    )
    public void complex() {}
}
