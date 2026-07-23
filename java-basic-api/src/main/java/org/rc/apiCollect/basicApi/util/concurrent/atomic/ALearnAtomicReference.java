package org.rc.apiCollect.basicApi.util.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class ALearnAtomicReference {
    public static void main(String[] args) {
        // 创建一个包含 Integer 对象的 AtomicReference 实例
        AtomicReference<Integer> atomicRef = new AtomicReference<>(0);

        // 使用 compareAndSet 方法递增计数器
        for (int i = 0; i < 10; i++) {
            int currentValue;
            int newValue;
            do {
                currentValue = atomicRef.get();
                newValue = currentValue + 1;
                // 执行 CAS 操作。只有当当前引用值等于预期值时，才会将引用值设置为新值，并返回 true；否则返回 false。
                // boolean compareAndSet(V expectedValue, V newValue)
                // expectedValue: 预期的当前值，即预期值。
                // newValue: 根据对比情况是否赋予引用值的新值(如果当前值等于预期值，则将引用设置为该新值。)
            } while (!atomicRef.compareAndSet(currentValue, newValue));

            System.out.println("Counter: " + newValue);
        }

        // 输出最终值
        System.out.println("Final counter value: " + atomicRef.get());

        // 注意事项
        // ABA 问题：如果某个值从 A 变成 B 再变回 A，那么仅依赖于 compareAndSet 的逻辑可能会忽略这种变化。
        //      对于这种情况，可以考虑使用带有版本号的对象或 AtomicStampedReference。
        // 性能影响：尽管 AtomicReference 提供了高性能的原子操作，但在高竞争的情况下，频繁的 CAS 失败可能导致性能下降。
        //      此时，可能需要评估是否采用其他同步策略。
    }
}

// AtomicReference 是 Java 并发包 (java.util.concurrent.atomic) 中的一个类，它提供了一种线程安全的方式来处理引用类型（对象）的原子操作。
// 与 AtomicInteger 或 AtomicLong 类似，AtomicReference 允许你对对象引用进行原子更新，而不需要使用同步机制如 synchronized 块或显式的锁。
//
// 主要特性
//     原子性：所有提供的方法都是原子性的，意味着它们在多线程环境下是线程安全的，并且不会被其他线程中断。
//     volatile 语义：内部使用的引用字段具有 volatile 的内存可见性保证，确保不同线程之间的一致性。
//     CAS 操作：利用比较并交换（Compare-And-Swap, CAS）算法来实现高效的无锁编程。
//
// 构造函数
// public AtomicReference(V initialValue)
// 参数：
// initialValue：初始值，可以为 null。
//
// 常用方法
// 1. get()
//     获取当前引用的值。
//     V get()
// 2. set(V newValue)
//     以非原子方式设置新的引用值。虽然此方法本身不是原子操作，但由于内部引用字段是 volatile，因此仍然具备内存可见性保证。
//     void set(V newValue)
// 3. lazySet(V newValue)
//     类似 set()，但使用更宽松的内存排序规则。适用于不需要立即看到更新结果的情况，通常用于写后读操作较少的场景。
//     void lazySet(V newValue)
// 4. compareAndSet(V expectedValue, V newValue)
//     执行 CAS 操作。只有当当前引用值等于预期值时，才会将引用值设置为新值，并返回 true；否则返回 false。
//     boolean compareAndSet(V expectedValue, V newValue)
//     expectedValue: 预期的当前值，即预期值。
//     newValue: 根据对比情况是否赋予引用值的新值(如果当前值等于预期值，则将引用设置为该新值。)
// 5. weakCompareAndSet(V expectedValue, V newValue)
//     类似于 compareAndSet()，但它允许 JVM 在某些情况下“失败”，即使条件满足。这主要用于避免可能发生的 ABA 问题，但在大多数情况下行为相同。
//     boolean weakCompareAndSet(V expectedValue, V newValue)
// 6. getAndSet(V newValue)
//     以原子方式设置新的引用值，并返回旧值。
//     V getAndSet(V newValue)