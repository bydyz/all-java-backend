package org.rc.apiCollect.basicApi.util.Base64;

import java.util.Base64;

public class AAABasicUse {
    public static void main(String[] args) {
        String originalInput = "Hello, World!";

        // 使用标准编码器
        String encodedStandard = Base64.getEncoder().encodeToString(originalInput.getBytes());
        System.out.println("Standard Encoded: " + encodedStandard);

        // 使用 MIME 编码器
        String encodedMime = Base64.getMimeEncoder().encodeToString(originalInput.getBytes());
        System.out.println("MIME Encoded: " + encodedMime);

        // 使用 URL 安全编码器
        String encodedUrlSafe = Base64.getUrlEncoder().encodeToString(originalInput.getBytes());
        System.out.println("URL Safe Encoded: " + encodedUrlSafe);

        // 解码示例
        byte[] decodedStandard = Base64.getDecoder().decode(encodedStandard);
        byte[] decodedMime = Base64.getMimeDecoder().decode(encodedMime);
        byte[] decodedUrlSafe = Base64.getUrlDecoder().decode(encodedUrlSafe);

        System.out.println("Decoded Standard: " + new String(decodedStandard));
        System.out.println("Decoded MIME: " + new String(decodedMime));
        System.out.println("Decoded URL Safe: " + new String(decodedUrlSafe));
    }
}
