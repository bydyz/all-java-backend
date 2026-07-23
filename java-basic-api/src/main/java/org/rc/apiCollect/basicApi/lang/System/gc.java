package org.rc.apiCollect.basicApi.lang.System;

public class gc {
    public static void main(String[] args) throws InterruptedException {
        for (int i=1; i <=10; i++){
            MyDemo my = new MyDemo(i);
            //每一次循环 my 就会指向新的对象，那么上次的对象就没有变量引用它了，就成垃圾对象
        }

        //为了看到垃圾回收器工作，我要加下面的代码，让 main 方法不那么快结束，因为 main 结束就会导致 JVM 退出，GC 也会跟着结束。
        System.gc();    //如果不调用这句代码，GC 可能不工作，因为当前内存很充足，GC 就觉得不着急回收垃圾对象。

        //调用这句代码，会让 GC 尽快来工作。
        Thread.sleep(5000);
    }
}


class MyDemo{
    private final int value;
    public MyDemo(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MyDemo{" + "value=" + value + '}';
    }

    //重写 finalize 方法，让大家看一下它的调用效果
    @Override
    protected void finalize() throws Throwable {
    // 正常重写，这里是编写清理系统内存的代码
    // 这里写输出语句是为了看到 finalize()方法被调用的效果
        System.out.println(this+ "轻轻的我走了，不带走一段代码....");
    }
}
