# 运算规则

* 任意八种基本数据类型的数据与 String 类型只能进行连接“+”运算，且结果一定也是 String 类型

```text
System.out.println("" + 1 + 2);//12

int num = 10;
boolean b1 = true;
String s1 = "abc";

String s2 = s1 + num + b1;
System.out.println(s2); // abc10true

// String s3 = num + b1 + s1;    // 编译不通过，因为 int 类型不能与 boolean 运算。运算顺序是从左到右，因此不同的运算顺序会有不同的结果

String s4 = num + (b1 + s1);    // 编译通过
```

* String 类型不能通过强制类型()转换，转为其他的类型；但可以借助包装类的方法才能转


