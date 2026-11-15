package org.rc.otherKnowledge.class_usage.interface_usage.conflict_1;

public interface I {
    default void hello() {
        System.out.println("I");
    }
}
