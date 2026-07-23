package org.rc.apiCollect.basicApi.util.Arrays.littleExample;
// 输入一个整形数组，数组里有正数也有负数。数组中连续的一个或多个整数组成一个子数组，每个子数组都有一个和。求所有子数组的和的最大值。要求时间复杂度为 O(n)。 例如：输入的数组为 1, -2, 3, -10, -4, 7, 2, -5，和最大的子数组为 3, 10, -4, 7, 2，因此输出为该子数组的和 18。

public class getBiggestSonArray {
    public static void main(String[] args) {
        int[] array = { 1, -2, 3, 10, -4, 7, 2, -5 };
        int finalValue = getBiggestSonArrayAdd(array);
        System.out.print("子数组中和最大的值为：" + finalValue);
    }

    public static int getBiggestSonArrayAdd(int[] array) {
        int biggestArrayAdd = 0;
        if (array == null || array.length == 0) {
            return 0;
        }
        int temp = biggestArrayAdd;
        for(int arrayItem : array) {
            temp += arrayItem;

            // 如果前面的数的和为负数，那加其值还会减小和，肯定不行。故不能从其算起，将temp赋值为零，相当于摒弃前面的数而从新开始
            if(temp < 0) {
                temp = 0;
            }

            // 和比biggestArrayAdd大则将biggestArrayAdd的值设为temp的值，此时是加上了正数，倘若加上负数，则temp必定小于biggestArrayAdd，此时并不表示后面都不用再尝试了，因为可能负数后面的正数比负数的绝对值大。因此此处只有temp比biggestArrayAdd大的情况，当小或者相等时，继续下面求和即可
            if (temp > biggestArrayAdd) {
                biggestArrayAdd = temp;
            }
        }
        // 标准答案上有这样的一句，我感觉没必要
        if(biggestArrayAdd == 0){
            biggestArrayAdd = array[0];
            for(int i = 1;i < array.length;i++){
                if(biggestArrayAdd < array[i]){
                    biggestArrayAdd = array[i];
                }
            }
        }
        return biggestArrayAdd;
    }
}
