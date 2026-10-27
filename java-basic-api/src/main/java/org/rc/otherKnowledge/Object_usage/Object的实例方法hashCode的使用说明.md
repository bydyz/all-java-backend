# Java `Object.hashCode()` 详解

## 一、方法定义与契约

`java.lang.Object` 中的声明极其简单，是一个 `native` 方法：

```java
public class Object {
    public native int hashCode();
}
```

但它的 Javadoc 定义了**三条必须遵守的契约**：

| 契约                    | 含义                                                                   |
|-----------------------|----------------------------------------------------------------------|
| **一致性**               | 在同一个对象上，只要用于 `equals` 比较的信息没有被修改，多次调用 `hashCode()` 必须返回同一个整数         |
| **equals ⇒ hashCode** | 如果 `a.equals(b)` 为 `true`，那么 `a.hashCode() == b.hashCode()` **必须**成立 |
| **hashCode ⇏ equals** | 如果 `equals` 为 `false`，两个对象的 `hashCode` **允许**相同（只是会影响哈希表性能）          |

违反第 2 条会导致 `HashMap` / `HashSet` 出现“存进去却取不出来”的经典 bug。

---

## 二、JVM 层实现（HotSpot）

`Object.hashCode()` 的 native 实现链路为：

```
Object.hashCode()  →  JVM_IHashCode
                   →  ObjectSynchronizer::FastHashCode
                   →  get_next_hash(Thread*, oop)
```

`get_next_hash` 的核心逻辑如下（OpenJDK `synchronizer.cpp`，简化版）：

```cpp
static inline intptr_t get_next_hash(Thread * Self, oop obj) {
  intptr_t value = 0;
  if (hashCode == 0) {
    // 0. 全局 Park-Miller 伪随机数生成器
    value = os::random();
  } else if (hashCode == 1) {
    // 1. 对象地址 + STW 期间随机数（对 STW 幂等）
    intptr_t addrBits = cast_from_oop<intptr_t>(obj) >> 3;
    value = addrBits ^ (addrBits >> 5) ^ GVars.stwRandom;
  } else if (hashCode == 2) {
    // 2. 永远返回 1（仅用于敏感性测试）
    value = 1;
  } else if (hashCode == 3) {
    // 3. 全局自增序列
    value = ++GVars.hcSequence;
  } else if (hashCode == 4) {
    // 4. 直接使用对象地址
    value = cast_from_oop<intptr_t>(obj);
  } else {
    // 5. Marsaglia xor-shift（默认算法）
    unsigned t = Self->_hashStateX;
    t ^= (t << 11);
    Self->_hashStateX = Self->_hashStateY;
    Self->_hashStateY = Self->_hashStateZ;
    Self->_hashStateZ = Self->_hashStateW;
    unsigned v = Self->_hashStateW;
    v = (v ^ (v >> 19)) ^ (t ^ (t >> 8));
    Self->_hashStateW = v;
    value = v;
  }
  value &= markWord::hash_mask;   // 截断为 31 位
  if (value == 0) value = 0xBAD;  // 0 留给"未计算"状态
  return value;
}
```

算法由虚拟机参数 `-XX:hashCode=N` 控制，**默认 `N=5`**（每个线程持有独立的 xor-shift 状态，性能好且分布均匀）。

### 关键点：hash 值会被缓存到对象头

`FastHashCode` 生成 hash 后，会通过 CAS 把它写入对象的 **Mark Word**。64 位 JVM 无锁状态下 Mark Word 的结构大致为：

```
| unused (25) | hashCode (31) | unused (1) | age (4) | biased_lock (1) | tag (2) |
```

（JDK 15+ 废弃偏向锁后布局略有不同，但 **hash 依然是 31 位** 被缓存。）

这带来三个重要结论：

1. **同一对象多次调用 `hashCode()` 结果不变**——因为第二次直接读对象头，不再重新生成。
2. **对象被 GC 移动后 hash 不变**——GC 复制对象时会连同 Mark Word 一起拷贝。
3. **返回值范围是 `[0, 2³¹-1]`**，非负（`String` 等重写后可能为负）。

---

## 三、Java 层验证代码

### 3.1 一致性：同一对象多次调用返回相同值

```java
public class ConsistencyDemo {
    public static void main(String[] args) {
        Object o = new Object();
        int h1 = o.hashCode();
        int h2 = o.hashCode();
        int h3 = o.hashCode();
        System.out.println(h1 + " " + h2 + " " + h3);
        System.out.println("一致? " + (h1 == h2 && h2 == h3));
    }
}
```

输出（数值随运行变化）：

```
1872038399 1872038399 1872038399
一致? true
```

### 3.2 GC 移动对象后 hash 不变

```java
public class GcDemo {
    public static void main(String[] args) {
        Object o = new Object();
        int before = o.hashCode();
        for (int i = 0; i < 10; i++) {
            System.gc();               // 触发多次 Full GC，对象很可能被移动
            byte[] junk = new byte[1024 * 1024]; // 制造内存压力
        }
        int after = o.hashCode();
        System.out.println("before=" + before + ", after=" + after);
        System.out.println("相同? " + (before == after)); // 恒为 true
    }
}
```

### 3.3 不同对象通常 hash 不同，但碰撞必然存在

```java
public class CollisionDemo {
    public static void main(String[] args) {
        Object a = new Object();
        Object b = new Object();
        System.out.println("a.hashCode() = " + a.hashCode());
        System.out.println("b.hashCode() = " + b.hashCode());
        System.out.println("a.equals(b) = " + a.equals(b));          // false
        System.out.println("hash 相同? " + (a.hashCode() == b.hashCode())); // 通常 false
    }
}
```

hash 空间只有 2³¹，按鸽巢原理，对象足够多时（约 7.7 万个对象就有 50% 概率碰撞，生日悖论）必然出现相同 hash。此时 `equals`
依然返回 `false`，这正是第三条契约允许的情况。

> 想人为制造“所有对象 hash 相同”的极端情况，可用 `-XX:hashCode=2` 启动（JDK 8
> 可用，属于诊断参数），此时所有对象的 `hashCode()` 都是 `1`。

### 3.4 `hashCode()` **不是**内存地址

这是最常见的误解。默认算法是 xor-shift 伪随机数（或线程相关状态），与地址无关。可做如下对比：

```java
public class NotAddressDemo {
    public static void main(String[] args) {
        Object o = new Object();
        System.out.println("hashCode        = " + o.hashCode());
        System.out.println("identityHashCode= " + System.identityHashCode(o));
        System.out.println("toString        = " + o); // toString 里的十六进制也不是地址
    }
}
```

即便使用 `-XX:hashCode=4`（直接用地址）的算法，地址与 hash 也**没有稳定的换算关系**，因为：

- 对象会被 GC 移动，而缓存的 hash 不会变；
- 32/64 位地址被截断成 31 位。

### 3.5 重写 `hashCode()` 后的 `identityHashCode`

`System.identityHashCode(obj)` **无视重写**，永远返回 JVM 默认实现算出的值：

```java
public class IdentityDemo {
    static class MyKey {
        @Override public int hashCode() { return 42; }
    }
    public static void main(String[] args) {
        MyKey k = new MyKey();
        System.out.println("k.hashCode()             = " + k.hashCode());            // 42
        System.out.println("System.identityHashCode = " + System.identityHashCode(k)); // 随机值
        System.out.println("两次 identity 相同? " + (System.identityHashCode(k)
                                              == System.identityHashCode(k)));        // true
    }
}
```

### 3.6 只重写 `equals` 不重写 `hashCode` 的经典坑

```java
import java.util.*;

public class BadPoint {
    private final int x, y;

    public BadPoint(int x, int y) { this.x = x; this.y = y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BadPoint)) return false;
        BadPoint p = (BadPoint) o;
        return x == p.x && y == p.y;
    }
    // 故意不重写 hashCode()

    public static void main(String[] args) {
        Map<BadPoint, String> map = new HashMap<>();
        map.put(new BadPoint(1, 2), "hello");

        BadPoint probe = new BadPoint(1, 2);
        System.out.println("equals     = " + probe.equals(new BadPoint(1, 2))); // true
        System.out.println("map.get    = " + map.get(probe));                   // null ❌
        System.out.println("map.size   = " + map.size());                       // 1

        Set<BadPoint> set = new HashSet<>();
        set.add(new BadPoint(1, 2));
        set.add(new BadPoint(1, 2));   // 没有去重
        System.out.println("set.size   = " + set.size());                       // 2 ❌
    }
}
```

原因：`HashMap` 查找分两步——先用 `hashCode()` 定位桶，再用 `equals()` 比较桶内元素。两个"相等"的对象 hash 不同，落在不同桶里，自然找不到。

### 3.7 正确重写示例

```java
import java.util.Objects;

public class GoodPoint {
    private final int x, y;

    public GoodPoint(int x, int y) { this.x = x; this.y = y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;                    // 1. 引用相同
        if (o == null || getClass() != o.getClass()) return false; // 2. 类型相同
        GoodPoint p = (GoodPoint) o;                   // 3. 逐字段比较
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);   // 等价于 31 * (31 * 1 + x) + y
    }

    // 手工版本（避免可变参数数组开销，性能更好）
    public int hashCodeManual() {
        int result = Integer.hashCode(x);
        result = 31 * result + Integer.hashCode(y);
        return result;
    }

    public static void main(String[] args) {
        Map<GoodPoint, String> map = new HashMap<>();
        map.put(new GoodPoint(1, 2), "hello");
        System.out.println(map.get(new GoodPoint(1, 2)));  // hello ✅

        Set<GoodPoint> set = new HashSet<>();
        set.add(new GoodPoint(1, 2));
        set.add(new GoodPoint(1, 2));
        System.out.println(set.size());                    // 1 ✅
    }
}
```

> **为什么用 31？** 31 是奇素数，`31 * i == (i << 5) - i` 可被 JIT 优化为移位减法；同时 31 的乘法结果在 32 位内不易溢出碰撞，是
> Joshua Bloch 在《Effective Java》中推荐的系数。

### 3.8 参照物：JDK 自带类的 `hashCode()` 实现

**`String`**（基于内容，与 `equals` 一致）：

```java
// JDK 源码
public int hashCode() {
    int h = hash;              // 懒计算并缓存到字段
    if (h == 0 && value.length > 0) {
        char[] val = value;
        for (int i = 0; i < value.length; i++) {
            h = 31 * h + val[i];   // s[0]*31^(n-1) + ... + s[n-1]
        }
        hash = h;
    }
    return h;
}
```

```java
System.out.println("a".hashCode());     // 97
System.out.println("abc".hashCode());   // 96354
System.out.println("Aa".hashCode());    // 2112
System.out.println("BB".hashCode());    // 2112  ← 著名的字符串 hash 碰撞
System.out.println("Aa".equals("BB"));  // false  ← 允许，契约第三条
```

**`Integer`**（就是值本身）：

```java
System.out.println(Integer.valueOf(123).hashCode()); // 123
```

**`Arrays.asList` / `List`**（用 `31` 逐个组合元素 hash）：

```java
System.out.println(java.util.List.of("a", "b").hashCode()); // 31*(31+97)+98 = 4066
```

---

## 四、`hashCode` 在哈希表中的实际作用

以 `HashMap` 为例（JDK 8）：

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16); // 扰动：高 16 位参与低位
}

// putVal / getNode 中定位桶：
int index = (n - 1) & hash(key);   // n 为 2 的幂，等价于 hash % n
```

- 用 `hashCode()` 决定落在哪个桶（**O(1)**）；
- 桶内用 `equals()` 判断是否同一个 key；
- 冲突多时链表转红黑树（阈值 8），但前提是 **hash 分布均匀**。

因此好的 `hashCode()` 应尽量均匀分布；而 `hashCode()` 全返回常量（如 `return 1;`）虽然不违反契约，但会把 `HashMap` 退化成链表。

---

## 五、常见误区汇总

| 误区                           | 事实                                                   |
|------------------------------|------------------------------------------------------|
| `hashCode()` 就是内存地址          | 默认是 xor-shift 伪随机数，仅 `-XX:hashCode=4` 时才与地址相关，且不保证稳定 |
| `hashCode()` 每次调用都重新计算       | 首次计算后缓存在 Mark Word，后续直接读取                            |
| `hashCode` 相同就说明对象相等         | 只能说明落在同一桶，必须再用 `equals` 判定                           |
| 重写 `equals` 可以不重写 `hashCode` | 违反契约，`HashMap`/`HashSet` 会出错                         |
| 用可变字段计算 `hashCode`           | 对象放进 `HashSet` 后改字段，就再也找不回来了                         |
| 默认 `hashCode()` 可能为负         | JVM 默认实现只取 31 位，非负；重写后（如 `String`）可以为负               |

---

## 六、最佳实践速记

1. **重写 `equals` 必须重写 `hashCode`**，反之亦然（否则 `equals` 语义可疑）。
2. `hashCode()` 只用参与 `equals` 比较的字段计算，且这些字段应尽量**不可变**。
3. 保证 `equals` 相等时 `hashCode` 相等；尽量让不相等的对象 hash 分散。
4. 用 `Objects.hash(...)` 简化写法，性能敏感时手动用 `31 * result + field`。
5. `String`、`Integer`、`List` 等不可变类型可直接复用其 `hashCode()`。
6. 排查哈希问题时，可用 `System.identityHashCode(obj)` 观察 JVM 默认 hash，从而判断是"自定义 hash 有问题"还是"JVM 默认
   hash 碰撞"。
