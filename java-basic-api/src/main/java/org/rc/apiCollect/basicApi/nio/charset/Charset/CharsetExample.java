package org.rc.apiCollect.basicApi.nio.charset.Charset;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CharsetExample {

    public static void main(String[] args) {
        // 定义要编码的字符串
        String originalString = "Hello, World!";

        // 获取 UTF-8 字符集实例       静态引用，作用上等同于下面行的
        Charset utf8Charset = StandardCharsets.UTF_8;
        Charset charset = Charset.forName("UTF-8");

        // 编码为字节数组
        byte[] encodedBytes = originalString.getBytes(utf8Charset);     // [72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33]
        System.out.println("encodedBytes = " + Arrays.toString(encodedBytes));
        System.out.println("Encoded Bytes (UTF-8): " + bytesToHex(encodedBytes));   // 48656c6c6f2c20576f726c6421   72 -> 48(十进制 到 16进制)

        // 解码 回 字符串
        String decodedString = new String(encodedBytes, utf8Charset);
        System.out.println("Decoded String: " + decodedString);

        // 尝试使用 ISO-8859-1 字符集
        Charset iso88591Charset = Charset.forName("ISO-8859-1");     // [72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33]
        byte[] encodedBytesIso = originalString.getBytes(iso88591Charset);
        System.out.println("Encoded Bytes (ISO-8859-1): " + bytesToHex(encodedBytesIso));   // 48656c6c6f2c20576f726c6421   72 -> 48(十进制 到 16进制)

        String decodedStringIso = new String(encodedBytesIso, iso88591Charset);
        System.out.println("Decoded String (ISO-8859-1): " + decodedStringIso);
    }

    // 辅助方法：将字节数组转换为十六进制字符串表示
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
