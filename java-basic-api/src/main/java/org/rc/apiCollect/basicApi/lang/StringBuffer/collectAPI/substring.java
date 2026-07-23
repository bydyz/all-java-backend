package org.rc.apiCollect.basicApi.lang.StringBuffer.collectAPI;

public class substring {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("I hate learning");
        System.out.println(buffer);

        String buffer2 = buffer.substring(2);
        System.out.println(buffer);
        System.out.println(buffer2);

        // 包前不包后
        String buffer3 = buffer.substring(2, 6);
        System.out.println(buffer);
        System.out.println(buffer3);
    }
}
