package org.rc.apiCollect.basicApi.nio.charset.Charset;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class CharsetConstants {

    // 这个字段被声明为 final，意味着它必须在声明时或构造函数中初始化。
    // 如果它是一个类级别的变量（静态变量），则必须在声明时或者静态初始化块中进行初始化。
    public static final Charset CHARSET_ISO_8859_1;
    // 使用了 Charset.defaultCharset() 来初始化 CHARSET_ISO_8859_2，这实际上会根据JVM的默认字符集来设置这个变量的值
    // JVM的默认字符集是由运行JVM的操作系统的本地环境决定的，具体来说是根据操作系统的区域设置和语言环境来确定的。
    public static final Charset myDefaultCharset = Charset.defaultCharset();
    // 操作系统：不同操作系统可能有不同的默认字符集。例如，在Windows系统上，可能会使用类似于 windows-1252 或其他基于区域的编码；
    //      而在类Unix系统（如Linux和macOS），通常默认为 UTF-8。
    // 区域设置（Locale）：在同一个操作系统中，不同的用户可以有不同的区域设置，这也会对默认字符集产生影响。
    //      例如，在一个中文版的Windows上，默认字符集可能是 GBK 或 GB2312。
    // 环境变量：某些环境变量也可以影响默认字符集的选择，比如 LANG, LC_ALL 等等。这些环境变量可以在类Unix系统中设置用户的语言和字符编码偏好。
    // JVM 参数：启动JVM时可以通过 -Dfile.encoding=CHARSET 参数显式地指定默认字符集。如果指定了这个参数，那么无论操作系统或环境变量如何，
    //      JVM都会使用给定的字符集作为默认字符集。

    static {
        try {
            CHARSET_ISO_8859_1 = Charset.forName("ISO-8859-1");
        } catch (UnsupportedCharsetException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // 示例方法：将字符串编码为 ISO-8859-1 字节数组
    public static byte[] encodeToIso8859_1(String str) {
        return str.getBytes(CHARSET_ISO_8859_1);
    }

    // 示例方法：将 ISO-8859-1 字节数组解码为字符串
    public static String decodeFromIso8859_1(byte[] bytes) {
        return new String(bytes, CHARSET_ISO_8859_1);
    }

    public static void main(String[] args) {
        String originalString = "Hello, World!";

        // 编码
        byte[] encodedBytes = encodeToIso8859_1(originalString);
        System.out.println("Encoded Bytes: " + bytesToHex(encodedBytes));

        // 解码
        String decodedString = decodeFromIso8859_1(encodedBytes);
        System.out.println("Decoded String: " + decodedString);
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

    // Charset 是 Java 中用于表示字符集（Character Set）的一个类，它定义了字符（如 Unicode 字符）与字节序列之间的映射关系。
    //     字符集是计算机系统用来表示文本的一套规则和约定，而 Charset 类则提供了在这些字符集之间进行转换的机制。以下是关于 Charset 的详细解析：
    //
    // 什么是字符集？
    // 字符集是一组符号及其编码的集合，每个符号都被分配了一个唯一的数字代码点。例如，ASCII（American Standard Code for Information Interchange）
    // 是一个早期的字符集，它为英语字母、数字和其他一些常用符号分配了 7 位二进制数。
    //
    // 随着需求的增长，出现了更多支持不同语言和符号的字符集，如 ISO-8859 系列、GBK（用于简体中文）、Big5（用于繁体中文）等。
    // 为了统一全球范围内的文本表示，Unicode 标准应运而生，它几乎涵盖了世界上所有已知的语言和符号，并且每种符号都有一个唯一的代码点。
    //
    // Charset 的作用
    // Charset 类在 Java 中扮演着桥梁的角色，它使得开发者可以在不同的字符集之间轻松地转换数据。具体来说，Charset 的主要作用包括：
    //
    // 字符编码：
    //     将字符串（即一组 Unicode 字符）转换为特定字符集对应的字节序列。这通常发生在将文本写入文件或通过网络发送时。
    // 字符解码：
    //     将从外部源接收到的字节序列（可能是任何字符集编码的数据）转换回 Java 内部使用的 Unicode 字符串。这在读取文件或接收网络数据包时非常有用。
    // 提供标准化接口：
    //     Charset 提供了一致的 API 来处理各种字符集，无论它们是单字节编码（如 ISO-8859-1）还是多字节编码（如 UTF-8）。
    //     这样可以简化跨平台和跨国界的文本处理任务。
    // 确保正确性：
    //     使用 Charset 可以避免因字符集不匹配导致的乱码问题。比如，在不知道文件实际编码的情况下尝试读取它可能会产生不可预测的结果；
    //     但是，如果指定了正确的 Charset，就能保证文本被准确无误地解析出来。
    // 性能优化：
    //     当需要频繁地进行相同类型的编码/解码操作时，可以缓存 Charset 实例来减少创建新对象所带来的开销。
    //
    // 常用的 Charset 方法
    //     forName(String charsetName)：根据名称获取一个 Charset 实例。如果指定的字符集名称无效或不受支持，则抛出 UnsupportedCharsetException。
    //     availableCharsets()：返回一个包含所有可用字符集名称及其对应 Charset 实例的不可变映射。
    //     newEncoder() 和 newDecoder()：分别创建一个新的编码器和解码器，用于执行具体的编码和解码操作。
    //     contains(Charset cs)：判断当前字符集是否包含另一个字符集的所有字符。这对于确定一种编码是否能够完全表示另一种编码中的字符很有用。