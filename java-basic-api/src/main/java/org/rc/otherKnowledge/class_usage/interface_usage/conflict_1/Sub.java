package org.rc.otherKnowledge.class_usage.interface_usage.conflict_1;

public class Sub extends Base implements I {
    public static void main(String[] args) {
        new Sub().hello();
    }

    @Override
    public void hello() {
        super.hello();

        I.super.hello();
    }
}
