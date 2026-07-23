package org.rc.algorithmicProblem;

// 递归方法调用：方法自己调用自己的现象就称为递归。
// 递归的分类：直接递归、间接递归。
//     直接递归：方法自身调用自己。
//         public void methodA(){
//             methodA();
//         }
//
//     间接递归：可以理解为 A()方法调用 B()方法，B()方法调用 C()方法，C()方法调用 A()方法。
//         public static void A(){
//             B();
//         }
//         public static void B(){
//             C();
//         }
//         public static void C(){
//             A();
//         }
//
//     说明：
//         递归方法包含了一种隐式的循环。
//         递归方法会重复执行某段代码，但这种重复执行无须循环控制。
//         递归一定要向已知方向递归，否则这种递归就变成了无穷递归，停不下来，类似于死循环。最终发生栈内存溢出。


// 递归调用会占用大量的系统堆栈，内存耗用多，在递归调用层次多时速度要比循环慢的多，所以在使用递归时要慎重。
// 在要求高性能的情况下尽量避免使用递归，递归调用既花时间又耗内存。考虑使用循环迭代

public class LearnRecursiveFunc {
    public static void main(String[] args) {
        LearnRecursiveFunc demo = new LearnRecursiveFunc();

        //计算 1~num 的和，使用递归完成
        int numOne = 5;
        int resultOne = demo.getSum(numOne);
        System.out.println("递归求和\t" + resultOne);

        
        //计算 n! 的值，使用递归完成
        int numTwo = 5;
        int resultTwo = demo.getN(numTwo);
        System.out.println("递归求阶乘\t" + resultTwo);


        //f(0) = 1，f(1) = 4，f(n+2) = 2 * f(n+1) + f(n)，求f(10)
        int numThree = 3;
        int resultThree = demo.getFnOne(numThree);
        System.out.println("求数列的f(10)\t" + resultThree);


        int numFour = 10;
        recursionOne(numFour);

    }

    /*
         通过递归算法实现.
         参数列表:int 
         返回值类型：int 
     */
    public int getSum(int num) {
     /* 
         num 为 1 时方法返回 1,
         相当于是方法的出口，num 总有为 1 的情况
     */
        if(num == 1){
            return 1;
        }
     /*
         num 不为 1 时方法返回 num +(num-1)的累加
         递归调用 getSum 方法
     */
        return num + getSum(num-1);
    }


    public int getN(int num) {
        if(num == 1) {
            return 1;
        }

        return num * getN(num -1);
    }


    public int getFnOne(int num) {
        if (num == 0) {
            return 1;
        }
        if (num == 1) {
            return 4;
        }
        return getFnOne(num - 1) + getFnOne(num - 2);
    }


    // 中 -> 左 -> 右
    private static int count = 0;
    public static int recursionOne(int k) {
        count++;
        System.out.println("countOne:\t" + count + "\t\tk:\t" + k);
        if(k <= 0) {
            return 0;
        }
        return recursionOne(k - 1) + recursionOne(k - 2);
    }
}
