Java 中严格来说有 **8 个基本类型包装类**，都在 `java.lang` 包下：

| 基本类型      | 包装类         | 是否继承 `Number` | 常用缓存                 |
|-----------|-------------|---------------|----------------------|
| `byte`    | `Byte`      | 是             | `-128 ~ 127`         |
| `short`   | `Short`     | 是             | `-128 ~ 127`         |
| `int`     | `Integer`   | 是             | 默认 `-128 ~ 127`，上限可调 |
| `long`    | `Long`      | 是             | `-128 ~ 127`         |
| `float`   | `Float`     | 是             | 无                    |
| `double`  | `Double`    | 是             | 无                    |
| `char`    | `Character` | 否             | `0 ~ 127`            |
| `boolean` | `Boolean`   | 否             | `TRUE/FALSE` 两个常量    |

另外还有 `java.lang.Void`，它表示 `void` 的 `Class` 占位符，通常不算“基本类型包装类”，且不能实例化。

---

## 共同作用与注意点

包装类的主要作用：

1. **让基本类型以对象形式使用**：泛型、集合、反射、序列化等只能操作对象。
2. **可表示 `null`**：数据库字段、JSON 反序列化等场景常用。
3. **提供类型转换和工具方法**：如 `parseInt`、`toBinaryString`、`isDigit`。
4. **支持自动装箱/拆箱**：
   ```java
   Integer a = 100;   // 自动装箱：Integer.valueOf(100)
   int b = a;         // 自动拆箱：a.intValue()
   ```
5. **实现 `Comparable`**，可比较；数字包装类继承 `Number`，提供 `intValue()`、`longValue()` 等方法。

注意：

- 构造器如 `new Integer(1)` 已不推荐，使用 `Integer.valueOf(1)` 或自动装箱。
- `==` 比较的是引用；包装类之间比较值应使用 `equals`，但要注意不同类型不相等：
  ```java
  Integer i = 1;
  Long l = 1L;
  System.out.println(i.equals(l)); // false
  ```
- 包装类为 `null` 时拆箱会抛 `NullPointerException`：
  ```java
  Integer x = null;
  // int y = x; // NPE
  ```
- 缓存范围内 `==` 可能为 `true`，但不要依赖缓存做值比较。

---

## 1. `Byte`

作用：包装 `byte`，范围 `-128 ~ 127`。常用于字节流、二进制数据处理。

常用 API：

- 常量：`MIN_VALUE`、`MAX_VALUE`、`SIZE`、`BYTES`、`TYPE`
- 转换：`parseByte(String)`、`valueOf(byte/String)`、`decode(String)`
- 取值：`byteValue()`、`intValue()`、`longValue()`
- 无符号：`toUnsignedInt(byte)`、`toUnsignedLong(byte)`
- 比较：`compare`、`compareTo`、`equals`

```java
byte b = Byte.parseByte("100");
Byte obj = Byte.valueOf("100");
int u = Byte.toUnsignedInt((byte) -1); // 255
System.out.println(Byte.MIN_VALUE);    // -128
System.out.println(Byte.MAX_VALUE);    // 127
System.out.println(Byte.compare((byte)1, (byte)2)); // -1
```

---

## 2. `Short`

作用：包装 `short`，范围约 `-32768 ~ 32767`。较少单独使用，常见于二进制协议、旧 API。

常用 API：

- 常量：`MIN_VALUE`、`MAX_VALUE`、`SIZE`、`BYTES`、`TYPE`
- 解析/转换：`parseShort`、`valueOf`、`decode`
- 取值：`shortValue()`、`intValue()` 等
- 无符号：`toUnsignedInt(short)`

```java
short s = Short.parseShort("1234");
Short obj = Short.valueOf((short) 10);
System.out.println(Short.MIN_VALUE); // -32768
System.out.println(Short.MAX_VALUE); // 32767
System.out.println(Short.toUnsignedInt((short) -1)); // 65535
```

---

## 3. `Integer`

作用：包装 `int`，最常用包装类。提供大量整数解析、进制转换、位运算工具。

常用 API：

- 常量：`MIN_VALUE`、`MAX_VALUE`、`SIZE=32`、`BYTES=4`
- 解析：`parseInt(String)`、`parseInt(String, radix)`、`parseUnsignedInt`
- 创建：`valueOf(int/String)`、`decode("0x1F")`
- 转字符串：`toString`、`toBinaryString`、`toHexString`、`toOctalString`、`toUnsignedString`
- 位运算：`bitCount`、`highestOneBit`、`lowestOneBit`、`numberOfLeadingZeros`、`rotateLeft`
- 比较/运算：`compare`、`compareUnsigned`、`sum`、`max`、`min`
- 系统属性：`getInteger("属性名")`

```java
int n = Integer.parseInt("123");
int hex = Integer.parseInt("FF", 16);       // 255
int dec = Integer.decode("0x1F");           // 31
String bin = Integer.toBinaryString(7);     // "111"
String hexStr = Integer.toHexString(255);   // "ff"
int bits = Integer.bitCount(7);             // 3
int cmp = Integer.compare(1, 2);            // -1

Integer a = Integer.valueOf(127);
Integer b = Integer.valueOf(127);
System.out.println(a == b); // true，缓存范围内

Integer c = Integer.valueOf(128);
Integer d = Integer.valueOf(128);
System.out.println(c == d); // false，通常不在缓存范围
System.out.println(c.equals(d)); // true
```

`Integer` 缓存默认 `-128 ~ 127`，可通过系统属性 `java.lang.Integer.IntegerCache.high` 调整上限。

---

## 4. `Long`

作用：包装 `long`，用于大整数、时间戳、ID 等。API 与 `Integer` 类似，支持无符号运算。

常用 API：

- 常量：`MIN_VALUE`、`MAX_VALUE`、`SIZE=64`、`BYTES=8`
- 解析：`parseLong`、`parseUnsignedLong`
- 转换：`valueOf`、`decode`、`toUnsignedString`
- 位运算：`bitCount`、`highestOneBit`、`numberOfTrailingZeros`、`rotateRight`
- 无符号运算：`compareUnsigned`、`divideUnsigned`、`remainderUnsigned`
- 比较：`compare`、`compareTo`

```java
long id = Long.parseLong("9223372036854775807");
Long obj = Long.valueOf(100L);
String unsigned = Long.toUnsignedString(-1L); // "18446744073709551615"
long q = Long.divideUnsigned(-1L, 2L);        // 无符号除法
int cmp = Long.compareUnsigned(-1L, 1L);      // 正数
System.out.println(Long.MAX_VALUE);
```

---

## 5. `Float`

作用：包装 `float`，单精度浮点数。注意浮点数精度问题，金融计算不建议直接用 `float/double`。

常用 API：

- 常量：`MAX_VALUE`、`MIN_VALUE`、`POSITIVE_INFINITY`、`NEGATIVE_INFINITY`、`NaN`、`SIZE=32`、`BYTES=4`
- 解析：`parseFloat(String)`
- 创建：`valueOf(float/String)`
- 判断：`isNaN`、`isInfinite`、`isFinite`
- 位级转换：`floatToIntBits`、`intBitsToFloat`、`floatToRawIntBits`
- 比较：`compare`、`compareTo`、`equals`

注意：`Float.MIN_VALUE` 是最小正非零值，不是最小负数；最小负数约为 `-MAX_VALUE`。

```java
float f = Float.parseFloat("3.14");
Float obj = Float.valueOf("3.14");
System.out.println(Float.isNaN(Float.NaN));       // true
System.out.println(Float.isInfinite(Float.POSITIVE_INFINITY)); // true
System.out.println(Float.compare(0.0f, -0.0f));   // 1，0.0f 大于 -0.0f
System.out.println(Float.floatToIntBits(1.0f));
```

---

## 6. `Double`

作用：包装 `double`，双精度浮点数，最常用浮点包装类。API 与 `Float` 类似。

常用 API：

- 常量：`MAX_VALUE`、`MIN_VALUE`、`NaN`、`POSITIVE_INFINITY`、`NEGATIVE_INFINITY`、`SIZE=64`、`BYTES=8`
- 解析：`parseDouble`
- 创建：`valueOf`
- 判断：`isNaN`、`isInfinite`、`isFinite`
- 位转换：`doubleToLongBits`、`longBitsToDouble`
- 比较：`compare`、`compareTo`、`equals`

```java
double d = Double.parseDouble("3.14159");
Double obj = Double.valueOf(2.5);
System.out.println(Double.isNaN(Double.NaN)); // true
System.out.println(Double.isFinite(1.0));     // true
System.out.println(Double.compare(Double.NaN, Double.NaN)); // 0
System.out.println(Double.doubleToLongBits(1.0));
```

`Double.equals` 中 `NaN.equals(NaN)` 为 `true`，`0.0.equals(-0.0)` 为 `false`；`Double.compare(0.0, -0.0)` 返回正数。

---

## 7. `Character`

作用：包装 `char`，表示一个 UTF-16 代码单元，并提供字符分类、大小写转换、数字字符转换、Unicode 辅助方法。

常用 API：

- 常量：`MIN_VALUE='\u0000'`、`MAX_VALUE='\uffff'`、`SIZE=16`、`BYTES=2`
- 判断：`isLetter`、`isDigit`、`isWhitespace`、`isUpperCase`、`isLowerCase`、`isAlphabetic`、`isJavaIdentifierStart`
- 转换：`toUpperCase`、`toLowerCase`、`digit(char, radix)`、`forDigit(int, radix)`、`getNumericValue`
- Unicode：`codePointAt`、`charCount`、`toChars`、`isSurrogatePair`、`highSurrogate`、`lowSurrogate`
- 比较：`compare`、`compareTo`、`equals`

```java
char ch = 'A';
System.out.println(Character.isLetter(ch));      // true
System.out.println(Character.isDigit('7'));      // true
System.out.println(Character.toLowerCase('A'));  // 'a'
System.out.println(Character.digit('F', 16));    // 15
System.out.println(Character.forDigit(15, 16));  // 'f'

int cp = Character.codePointAt("😀", 0);
char[] chars = Character.toChars(cp);
System.out.println(Character.charCount(cp));     // 2
System.out.println(Character.isSurrogatePair(chars[0], chars[1])); // true
```

`Character` 缓存 `0 ~ 127` 的字符对象。

---

## 8. `Boolean`

作用：包装 `boolean`，表示 `true/false`。只有两个实例常量 `Boolean.TRUE`、`Boolean.FALSE`。

常用 API：

- 常量：`TRUE`、`FALSE`、`TYPE`
- 解析：`parseBoolean(String)`：忽略大小写，只有 `"true"` 返回 `true`，其他包括 `"yes"` 都返回 `false`
- 创建：`valueOf(boolean/String)`
- 取值：`booleanValue()`
- 逻辑工具：`logicalAnd`、`logicalOr`、`logicalXor`
- 比较：`compare`、`compareTo`、`equals`

```java
boolean b1 = Boolean.parseBoolean("TRUE");  // true
boolean b2 = Boolean.parseBoolean("yes");   // false
Boolean obj = Boolean.valueOf("false");     // Boolean.FALSE
System.out.println(Boolean.logicalXor(true, false)); // true
System.out.println(Boolean.TRUE.booleanValue());     // true
System.out.println(Boolean.compare(false, true));    // -1
```

---

## 补充：`Number` 与 `Void`

- `Number` 是 `Byte/Short/Integer/Long/Float/Double` 的抽象父类，定义：
  ```java
  byteValue();
  shortValue();
  intValue();
  longValue();
  floatValue();
  doubleValue();
  ```
  例如：
  ```java
  Number n = Integer.valueOf(100);
  double d = n.doubleValue(); // 100.0
  ```

- `Void` 表示 `void.class`，不可实例化，常用于泛型占位，例如 `CompletableFuture<Void>`：
  ```java
  Class<Void> c = Void.TYPE; // void.class
  ```

---

## 总结

- 8 个包装类：`Byte`、`Short`、`Integer`、`Long`、`Float`、`Double`、`Character`、`Boolean`。
- 数字包装类继承 `Number`，提供各种 `xxxValue()`；`Character`、`Boolean` 不继承 `Number`。
- 优先使用 `valueOf`/自动装箱，避免 `new` 包装类构造器。
- 值比较用 `equals`；`==` 只适合与 `null` 比较或明确需要比较引用。
- 注意缓存、`null` 拆箱 NPE、浮点精度、不同类型 `equals` 返回 `false`。
