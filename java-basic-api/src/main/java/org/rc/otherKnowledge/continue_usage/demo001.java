package org.rc.otherKnowledge.continue_usage;

// continue 不能直接作用于 switch，但当 switch 位于循环内时，continue 会跳过外层循环的本次迭代
public class demo001 {
    public static void main(String[] args) {
        // 打印 1~10 中的奇数，跳过偶数
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue;              // 偶数跳过，不执行下面的打印
            }
            System.out.print(i + " "); // 1 3 5 7 9
        }

        // 跳过一层 while 循环
        int i = 0;
        while (i < 5) {
            i++;
            if (i == 3) {
                continue;              // 跳过 3 的打印
            }
            System.out.print(i + " "); // 1 2 4 5
        }

        // 跳过一层 do...while 循环
        int j = 0;
        do {
            j++;
            if (j == 2) {
                continue;          // 跳到 while(j < 4) 判断
            }
            System.out.print(j + " ");  // 1 3 4
        } while (j < 4);
    }
}
