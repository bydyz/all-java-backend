package org.rc.apiCollect.basicApi.util.Arrays.littleExample;

public class bubbleSort {
    public static void main(String[] args) {
        bubbleSort a = new bubbleSort();
        int[] b = {1, 9, 2, 8};
        a.sort(b);

        a.print(b);
    }

    //冒泡排序，实现数组从小到大排序
    //每一轮的操作就是将最大值移到最后。第一轮是比较所有的值，将其中最大的移到最后；第二轮是比较除去最后一位的其他所有，然后将最大的移到最后；依次循环直到比较完成第一个和第二个
    public void sort(int[] arr){
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if(arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }


    //打印数组的元素
    public void print(int[] arr){
        // for (int i = 0; i < arr.length; i++) {
        //     System.out.print(arr[i]+" ");
        // }
        for (int j : arr) {
            System.out.print(j + " ");
        }
        System.out.println();
    }
}
