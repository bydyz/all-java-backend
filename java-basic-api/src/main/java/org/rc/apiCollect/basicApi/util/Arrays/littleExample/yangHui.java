package org.rc.apiCollect.basicApi.util.Arrays.littleExample;

public class yangHui {
    public static void main(String[] args) {
        int[][] yangHui = new int[10][];
        for (int i = 0; i < yangHui.length; i++) {
            yangHui[i] = new int[i + 1];

            yangHui[i][0] = yangHui[i][i] = 1;
        }

        for (int i = 2; i < yangHui.length; i++) {
            for (int j = 1; j < yangHui[i].length - 1; j++) {
                yangHui[i][j] = yangHui[i-1][j-1] + yangHui[i-1][j];
            }
        }

        int totalColumnsNum = 2 * (10 - 1) - 1;
        int mid = totalColumnsNum/2;

        for (int i = 0; i < yangHui.length; i++) {
            for (int j = 0; j < yangHui[i].length; j++) {
                System.out.print(yangHui[i][j] + "\t");
            }
            System.out.println();
        }



        System.out.print("\n\n\n");



        for (int i = 0; i < yangHui.length; i++) {
            for (int m = mid - i; m > 0; m--) {
                System.out.print("\t");
            }
            for (int j = 0; j < yangHui[i].length; j++) {
                System.out.print(yangHui[i][j] + "\t");
            }
            System.out.println();
        }



        System.out.print("\n\n\n");



        // this is my thoughts
        for (int i = 0; i < yangHui.length; i++) {
            for (int j = 0; j < yangHui[i].length; j++) {
                if (j != yangHui[i].length - 1) {
                    System.out.print(yangHui[i][j] + "\t");
                } else {
                    System.out.print(yangHui[i][j] + "\n");
                }
            }
        }
    }
}
