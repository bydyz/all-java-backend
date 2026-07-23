package org.rc.apiCollect.basicApi.util.Map;

public class User implements Comparable{
    String name;
    int age;
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public User() {
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
    /*
    举例：按照 age 从小到大的顺序排列，如果 age 相同，则按照 name 从大到小的
   顺序排列
    * */
    @Override
    public int compareTo(Object o) {
        if(this == o){
            return 0;
        }
        if(o instanceof User user){
            int value = this.age - user.age;
            if(value != 0){
                return value;
            }
            return -this.name.compareTo(user.name);
        }
        throw new RuntimeException("输入的类型不匹配");
    }
}
