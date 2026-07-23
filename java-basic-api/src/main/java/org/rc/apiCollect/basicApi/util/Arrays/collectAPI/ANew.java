package org.rc.apiCollect.basicApi.util.Arrays.collectAPI;

public class ANew {
    public static void main(String[] args) {

        // int[] test1;
        // 命名数组后，不能直接使用，需要初始化
        // System.out.println(test1);


        // 静态初始化
        System.out.print("静态初始化：" + "\n");
        int[] test2 = new int[]{1, 2, 3};
        System.out.println(test2);
        char[] test3 = {'a', 'b', 'c', 'd', 'e'};
        System.out.println(test3);
        String[] test5 = {"毛泽东", "周恩来", "朱德"};
        System.out.println(test5);
        System.out.print("\n\n");


        // 动态初始化
        System.out.print("动态初始化：\n");
        int[] test6 = new int[7];
        System.out.println(test6);
        System.out.print("\n\n");


        int[] test7 = new int[6];
        System.out.print("存储数据到test7之前：");
        for (int i = 0; i < test7.length; i++) {
            if(i==0) {
                System.out.print(test7[0]);
            }else {
                System.out.print("，" + test7[i]);
            }
        }
        System.out.print("\n");
        // 赋值
        for (int i = 0; i < test7.length; i++) {
            test7[i] = i * 2;
        }
        System.out.print("存储数据到test7之后：");
        for (int i = 0; i < test7.length; i++) {
            if(i==0) {
                System.out.print(test7[0]);
            }else {
                System.out.print("，" + test7[i]);
            }
        }
        System.out.print("\n\n");


        // 二维数组
        System.out.print("二维数组：\n");
        String[][] grade = new String[][]{ {"段誉","令狐冲","任我行"},{"张三丰","周芷若"},{"赵敏","张无忌","韦小宝","杨过"} };
        String[][] grade1 = { {"段誉","令狐冲","任我行"},{"张三丰","周芷若"},{"赵敏","张无忌","韦小宝","杨过"} };
        /*
         * 连续对两行代码进行注释，或提示 Commented out code (2 lines)
         *
         * 未初始化的不能用，打印或者赋值均不可;
         * System.out.print(grade[1][2] + "\n\n");
         * grade[1][2] = "谢逊";
         *
         * */
        System.out.print(grade[0][2] + "\n");
        System.out.print(grade[2][2] + "\n");
        System.out.println("此二维数组中第三个以为数组的长度为" + grade[2].length);
        System.out.println(grade1[2][1]);
        System.out.print("\n\n");


        // 同时命名一维数组和二维数组
        System.out.print("同时命名一维数组和二维数组：\n");
        int[] x;
        int[][] y;
        // 错误写法
        // x = {1, 2, 3};
        x = new int[]{1, 2, 3};
        y = new int[][]{ {1, 2, 3}, {5, 6}, {7, 8, 9, 10} };
        System.out.println(x[1]);
        System.out.println(y[2][2]);
        System.out.print("\n\n");


        // 每一行的一维数组长度不同
        System.out.print("每一行的一维数组长度不同：\n");
        //（1）先确定总行数
        // 元素的数据类型[][] 二维数组名 = new 元素的数据类型[总行数][];
        // 此时只是确定了总行数，每一行里面现在是 null
        //（2）再确定每一行的列数，创建每一行的一维数组
        // 二维数组名[行下标] = new 元素的数据类型[该行的总列数];
        // 此时已经 new 完的行的元素就有默认值了，没有 new 的行还是 null
        // (3)再为元素赋值
        // 二维数组名[行下标][列下标] = 值;
        int[][] test11 = new int[6][];
        System.out.println(test11[1]);  // null
        test11[0] = new int[]{1, 2, 3};
        test11[1] = new int[]{5, 6, 7, 8};
        test11[2] = new int[2];
        test11[3] = new int[]{10, 11, 12, 13, 15};
        System.out.println(test11[1][1]);
        System.out.println(test11[2]);  // [I@378bf509   I 代表元素类型是 int
        System.out.println(test11[2][0]);
        System.out.println(test11[5]);
        System.out.print("\n");
    }
}
