package org.rc.apiCollect.basicApi.lang.Comparable;

public class Example1 {
    public static void main(String[] args) {
        Student[] arr = new Student[5];
        arr[0] = new Student(3,"张三",90,23);
        arr[1] = new Student(1,"熊大",100,22);
        arr[2] = new Student(5,"王五",75,25);
        arr[3] = new Student(4,"李四",85,24);
        arr[4] = new Student(2,"熊二",85,18);

        //单独比较两个对象
        System.out.println(arr[0].compareTo(arr[1]));
        System.out.println(arr[1].compareTo(arr[2]));
        System.out.println(arr[2].compareTo(arr[2]));

        System.out.println("所有学生：");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        System.out.println("按照学号排序：");
        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < arr.length-i; j++) {
                if(arr[j].compareTo(arr[j+1])>0){
                    Student temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

}


// Comparable 接口强行对实现它的每个类的对象进行整体排序。这种排序被称为类的自然排序。
// 实现 Comparable 的类必须实现 compareTo(Object obj)方法，两个对象即通过compareTo(Object obj) 方法的返回值来比较大小。
// 如果当前对象 this 大于形参对象obj，则返回正整数，如果当前对象 this 小于形参对象 obj，则返回负整数，如果当前
// 对象 this 等于形参对象 obj，则返回零。

// 实现 Comparable 接口的对象列表（和数组）可以通过 Collections.sort 或 Arrays.sort 进行自动排序。
//     实现此接口的对象可以用作有序映射中的键或有序集合中的元素，无需指定比较器。
// 对于类 C 的每一个 e1 和 e2 来说，当且仅当 e1.compareTo(e2) == 0 与 e1.equals(e2) 具有相同的 boolean 值时，
//     类 C 的自然排序才叫做与 equals 一致。建议（虽然不是必需的）最好使自然排序与 equals 一致。
// Comparable 的典型实现：(默认都是从小到大排列的)
//     – String：按照字符串中字符的 Unicode 值进行比较
//     – Character：按照字符的 Unicode 值来进行比较
//     – 数值类型对应的包装类以及 BigInteger、BigDecimal：按照它们对应的数值大小进行比较
//     – Boolean：true 对应的包装类实例大于 false 对应的包装类实例
//     – Date、Time 等：后面的日期时间比前面的日期时间大

class Student implements Comparable {
    private int id;
    private String name;
    private int score;
    private int age;
    public Student(int id, String name, int score, int age) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.age = age;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", age=" + age +
                '}';
    }
    @Override
    public int compareTo(Object o) {
        //这些需要强制，将 o 对象向下转型为 Student 类型的变量，才能调用 Student 类中的属性
        //默认按照学号比较大小
        Student stu = (Student) o;
        return this.id - stu.id;
    }
}