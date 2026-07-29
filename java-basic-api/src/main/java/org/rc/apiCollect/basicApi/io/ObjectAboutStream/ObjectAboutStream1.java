package org.rc.apiCollect.basicApi.io.ObjectAboutStream;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

public class ObjectAboutStream1 {
    @Test
    public void save() throws IOException {
        String name = "巫师";
        int age = 300;
        char gender = '男';
        int energy = 5000;
        double price = 75.5;
        boolean relive = true;
        
        // 只能是 .dat  文件吗
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("game.dat"));
        System.out.println("000   " + oos);
        oos.writeUTF(name);
        oos.writeInt(age);
        oos.writeChar(gender);
        oos.writeInt(energy);
        oos.writeDouble(price);
        oos.writeBoolean(relive);
        System.out.println("111   " + oos);
        oos.close();
    }
    
    
    
    @Test
    public void reload()throws IOException{
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("game.dat"));
        System.out.println("000   " + ois);
        String name = ois.readUTF();
        int age = ois.readInt();
        char gender = ois.readChar();
        int energy = ois.readInt();
        double price = ois.readDouble();
        boolean relive = ois.readBoolean();
        System.out.println(name+"," + age + "," + gender + "," + energy + "," + price + "," + relive);
        ois.close();
    }

}
