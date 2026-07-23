package org.rc.apiCollect.basicApi.util.Scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Scanner2 {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(new FileInputStream("1.txt"));
        while(input.hasNextLine()){
            String str = input.nextLine();
            System.out.println(str);
        }
        input.close();
    }
}
