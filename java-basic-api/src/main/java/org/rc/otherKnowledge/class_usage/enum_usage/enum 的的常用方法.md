# Java enum 常用方法详解

Java 的 `enum` 本质是一个继承自 `java.lang.Enum` 的 `final` 类（若存在常量体则生成子类）。它的方法分为三类：

1. **编译器自动生成**的静态方法：`values()`、`valueOf(String)`
2. **继承自 `Enum`** 的实例/静态方法
3. **自定义**的字段、方法、抽象方法、接口实现

下面用一个完整的订单状态枚举贯穿全部示例。

```java
public enum OrderStatus {
    CREATED(1, "已创建"),
    PAID(2, "已支付"),
    SHIPPED(3, "已发货"),
    DELIVERED(4, "已签收"),
    CANCELLED(5, "已取消");

    private final int code;
    private final String desc;

    OrderStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() { return code; }
    public String getDesc() { return desc; }

    /** 自定义查找：根据业务码反查枚举 */
    public static OrderStatus fromCode(int code) {
        for (OrderStatus s : values()) {
            if (s.code == code) return s;
        }
        throw new IllegalArgumentException("未知状态码: " + code);
    }

    /** 重写 toString 只影响打印，不影响 name()/valueOf() */
    @Override
    public String toString() { return desc; }
}
```

---

## 一、编译器自动生成的两个静态方法

| 方法                              | 说明                                                                                |
|---------------------------------|-----------------------------------------------------------------------------------|
| `static E[] values()`           | 返回所有常量数组，**顺序 = 声明顺序**；每次调用返回的是 `$VALUES.clone()` 副本                              |
| `static E valueOf(String name)` | 按**常量名**（即 `name()`）精确匹配，大小写敏感；找不到抛 `IllegalArgumentException`，参数为 `null` 抛 `NPE` |

```java
OrderStatus[] all = OrderStatus.values();
System.out.println(Arrays.toString(all));
// [已创建, 已支付, 已发货, 已签收, 已取消]（调用的是被重写的 toString）

OrderStatus s = OrderStatus.valueOf("PAID");
System.out.println(s.name());   // PAID      ← name() 不受 toString 影响
System.out.println(s);          // 已支付

// 底层等价写法（Enum 类提供的静态泛型方法）
OrderStatus s2 = Enum.valueOf(OrderStatus.class, "PAID");

// 错误示例
// OrderStatus.valueOf("paid");  // IllegalArgumentException: No enum constant OrderStatus.paid
```

> ⚠️ **坑**：`values()` 每次都会 `clone` 数组，热点代码可缓存成 `private static final OrderStatus[] VALUES = values();`。

---

## 二、继承自 `java.lang.Enum` 的方法

| 方法                                                       | 说明                                     |
|----------------------------------------------------------|----------------------------------------|
| `final String name()`                                    | 常量名，与源码声明完全一致                          |
| `final int ordinal()`                                    | 声明序号，从 0 开始                            |
| `String toString()`                                      | 默认返回 `name()`，**可重写**                  |
| `final boolean equals(Object)`                           | 与 `==` 等价（引用相等）                        |
| `final int hashCode()`                                   | 基于对象标识（identity），不是 `name` 的哈希         |
| `final int compareTo(E o)`                               | 按 `ordinal` 比较，可自然排序                   |
| `final Class<E> getDeclaringClass()`                     | 返回枚举类型本身（区别于 `getClass()`）             |
| `protected final Object clone()`                         | 抛出 `CloneNotSupportedException`，枚举不可克隆 |
| `static <T extends Enum<T>> T valueOf(Class<T>, String)` | 见上                                     |

```java
OrderStatus s = OrderStatus.SHIPPED;

System.out.println(s.name());     // SHIPPED
System.out.println(s.ordinal());  // 2
System.out.println(s == OrderStatus.SHIPPED);            // true
System.out.println(s.equals(OrderStatus.SHIPPED));       // true

// compareTo 按 ordinal
System.out.println(OrderStatus.CREATED.compareTo(OrderStatus.PAID)); // -1

// 自然排序
List<OrderStatus> list = new ArrayList<>(Arrays.asList(OrderStatus.values()));
Collections.sort(list);  // 按声明顺序

// 使用 EnumSet 的排序特性也能体现 ordinal 顺序
```

### `getDeclaringClass()` vs `getClass()`（重要区别）

当枚举常量带有**常量体**（`{ ... }`，即匿名子类）时，`getClass()` 返回匿名子类，而 `getDeclaringClass()` 返回枚举类型本身。因此
**判断两个枚举是否同一类型要用 `getDeclaringClass()`**。

```java
public enum Op {
    PLUS  { @Override public int apply(int a, int b) { return a + b; } },
    MINUS { @Override public int apply(int a, int b) { return a - b; } };

    public abstract int apply(int a, int b);
}

Op.PLUS.getClass().getName();           // Op$1
Op.PLUS.getDeclaringClass().getName();  // Op
System.out.println(Op.PLUS.apply(3, 2)); // 5
```

### `switch` 中的使用

`switch` 对枚举走的是 `ordinal` + `$SwitchMap` 合成表，因此非常高效：

```java
switch (s) {
    case CREATED: case PAID:
        System.out.println("进行中");
        break;
    case DELIVERED:
        System.out.println("已完成");
        break;
    default:
        System.out.println("其他");
}
```

---

## 三、自定义方法/字段 + 抽象方法 + 接口实现

```java
public interface Describable {
    String describe();
}

public enum PayType implements Describable {
    ALIPAY("支付宝") {
        @Override public double fee(double amount) { return amount * 0.006; }
        @Override public String describe() { return "第三方支付"; }
    },
    WECHAT("微信") {
        @Override public double fee(double amount) { return amount * 0.006; }
        @Override public String describe() { return "第三方支付"; }
    },
    BANK("银行卡") {
        @Override public double fee(double amount) { return amount * 0.01; }
        @Override public String describe() { return "银行渠道"; }
    };

    private final String label;
    PayType(String label) { this.label = label; }

    public String getLabel() { return label; }

    /** 每个常量各自实现：策略模式 */
    public abstract double fee(double amount);

    /** 公共逻辑 */
    public boolean isThirdParty() { return this == ALIPAY || this == WECHAT; }
}

System.out.println(PayType.ALIPAY.fee(1000));       // 6.0
System.out.println(PayType.BANK.describe());        // 银行渠道
System.out.println(PayType.WECHAT.isThirdParty());  // true
```

---

## 四、`EnumSet` / `EnumMap`（枚举专用集合）

```java
// EnumSet：内部用位向量（long[]），比 HashSet 快且省内存
EnumSet<OrderStatus> active = EnumSet.of(OrderStatus.CREATED, OrderStatus.PAID);
EnumSet<OrderStatus> all    = EnumSet.allOf(OrderStatus.class);
EnumSet<OrderStatus> none   = EnumSet.noneOf(OrderStatus.class);
EnumSet<OrderStatus> range  = EnumSet.range(OrderStatus.CREATED, OrderStatus.SHIPPED);
EnumSet<OrderStatus> other  = EnumSet.complementOf(active);

System.out.println(range);  // [已创建, 已支付, 已发货]

// EnumMap：内部是数组，key 必须为同一枚举类型
EnumMap<OrderStatus, Integer> counter = new EnumMap<>(OrderStatus.class);
for (OrderStatus st : OrderStatus.values()) counter.put(st, 0);
counter.merge(OrderStatus.PAID, 1, Integer::sum);
System.out.println(counter.get(OrderStatus.PAID)); // 1
```

---

## 五、注意事项与最佳实践

1. **不要用 `ordinal()` 做持久化/协议编号**。在中间插入常量会导致所有序号平移；应自定义 `code` 字段并配 `fromCode()`（如示例）。
2. **`toString()` 与 `valueOf()` 不对称**。重写 `toString()` 后仍然只能用 `name()` 值调用 `valueOf`；需要"按显示名反查"
   必须自己实现。
3. **枚举比较用 `==` 即可**。`equals` 被 final 限定为引用相等，且枚举实例全局唯一（线程安全、可安全用于 `switch`、`EnumMap`
   、单例）。
4. **`values()` 有数组拷贝开销**，高频路径建议缓存 `VALUES` 数组。
5. **枚举不能被反射/克隆创建新实例**：`Constructor.newInstance` 对枚举抛 `IllegalArgumentException`，`clone()` 抛异常，这也是
   Java 单例的最佳实现方式（天然防反射、防序列化破坏）。
6. **判断枚举类型时用 `getDeclaringClass()`**，避免常量体匿名子类导致的误判。
7. **枚举不能继承类**（已隐式继承 `Enum`），但可**实现接口**、可定义**抽象方法**做策略分发。
8. `valueOf(String)` 找不到时抛的是 **`IllegalArgumentException`** 而非返回 `null`，需要健壮解析时用 `fromXxx`
   或 `EnumUtils.getEnum(...)`（Apache Commons Lang）。

---

## 六、方法速查表

| 方法                            | 类型           | 是否可重写 | 典型用途          |
|-------------------------------|--------------|-------|---------------|
| `values()`                    | 编译器生成 static | 否     | 遍历、查找、初始化     |
| `valueOf(String)`             | 编译器生成 static | 否     | 按常量名解析        |
| `name()`                      | Enum final   | 否     | 拿稳定标识（序列化、日志） |
| `ordinal()`                   | Enum final   | 否     | 声明顺序（谨慎持久化）   |
| `toString()`                  | Object       | **是** | 展示文案          |
| `equals()` / `hashCode()`     | Enum final   | 否     | 可直接用 `==`     |
| `compareTo(E)`                | Enum final   | 否     | 按声明顺序排序       |
| `getDeclaringClass()`         | Enum final   | 否     | 判断枚举类型        |
| `clone()`                     | Enum final   | 否     | 总抛异常          |
| `Enum.valueOf(Class, String)` | Enum static  | 否     | 泛型解析          |
| 自定义 `fromCode()`              | 自定义          | —     | 业务码映射         |

掌握这些方法，枚举就不仅能做"常量集合"，还能作为**类型安全的策略模式、状态机、单例**来使用。
