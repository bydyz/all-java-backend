package org.rc.apiCollect.basicApi.lang.StringBuffer.littleExample;

public class specialOne {
    public static void main(String[] args) {
        String str = null;
        StringBuffer sb = new StringBuffer();
        sb.append(str);
        System.out.println(sb.length());    // 4
        System.out.println(sb);             // null

        // StringBuffer sb1 = new StringBuffer(str);    // 报错
        // System.out.println(sb1);//
    }
}
