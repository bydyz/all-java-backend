package org.rc.otherKnowledge.switch_usage;

/**
 * Java switch 语句 5 大注意点 完整演示
 *
 * 注意点：
 *  1. switch 表达式的值只能是 byte / short / char / int / 枚举(JDK5) / String(JDK7)
 *  2. case 的值必须是编译期常量（不能是变量、不能是范围）
 *  3. 同一个 switch 内所有 case 的值必须互不相同
 *  4. break 用于跳出；没有 break 会"穿透"顺序执行到 switch 结尾
 *  5. default 可选，位置灵活；无匹配 case 时执行
 */
public class demo001 {

    // 供注意点 1 使用：JDK 5.0 起 switch 支持枚举
    enum Season { SPRING, SUMMER, AUTUMN, WINTER }

    public static void main(String[] args) {

        /* =========================================================
         * 注意点 1：switch(表达式) 的值只能是这 6 种类型：
         *   byte、short、char、int、枚举(JDK5.0)、String(JDK7.0)
         *   long / float / double / boolean 一律不支持
         * ========================================================= */

        // --- 1.1 int（最常用）---
        int num = 2;
        switch (num) {
            case 1:
                System.out.println("[int] 一");
                break;
            case 2:
                System.out.println("[int] 二");   // ← 输出
                break;
            default:
                System.out.println("[int] 其他");
        }

        // --- 1.2 byte ---
        byte b = 3;
        switch (b) {
            case 1: System.out.println("[byte] 一"); break;
            case 3: System.out.println("[byte] 三"); break;   // ← 输出
        }

        // --- 1.3 short ---
        short sh = 4;
        switch (sh) {
            case 4: System.out.println("[short] 四"); break;  // ← 输出
        }

        // --- 1.4 char ---
        char c = 'B';
        switch (c) {
            case 'A': System.out.println("[char] A"); break;
            case 'B': System.out.println("[char] B"); break;  // ← 输出
        }

        // --- 1.5 枚举（JDK 5.0 起）---
        Season season = Season.SUMMER;
        switch (season) {
            // 注意：case 后只写枚举常量名，不能带类名 Season.SPRING
            case SPRING: System.out.println("[enum] 春"); break;
            case SUMMER: System.out.println("[enum] 夏"); break;  // ← 输出
            case AUTUMN: System.out.println("[enum] 秋"); break;
            case WINTER: System.out.println("[enum] 冬"); break;
        }
        // ❌ 错误写法：case Season.SPRING:    编译报错（case 标签中不允许带枚举类名前缀）

        // --- 1.6 String（JDK 7.0 起）---
        String str = "hello";
        switch (str) {
            case "hello": System.out.println("[String] hello"); break;  // ← 输出
            case "world": System.out.println("[String] world"); break;
            default:      System.out.println("[String] 其他");
        }
        // ⚠️ 陷阱：String 为 null 时，switch 会抛 NullPointerException（不是走 default！）

        // --- 1.7 不支持的类型（仅注释说明，取消注释会编译报错）---
        // long l = 1L;
        // switch (l) { }                     // ❌ long 不支持
        //
        // double d = 1.0;
        // switch (d) { }                     // ❌ double 不支持
        //
        // boolean flag = true;
        // switch (flag) { }                  // ❌ boolean 不支持

        /* =========================================================
         * 注意点 2：case 的值必须是"编译期常量"
         *   ✔ 字面量、final 常量、常量表达式
         *   ✘ 变量名、运行时才确定的表达式、范围
         * ========================================================= */

        int x = 1;
        final int CONST = 10;               // final + 字面量初始化 = 编译期常量，可用于 case

        switch (x) {
            case 1:                          // ✔ 字面量
                System.out.println("[case 常量] 字面量 1");
                break;
            case 2 + 3:                      // ✔ 常量表达式（编译期能算出 5）
                System.out.println("[case 常量] 2+3=5");
                break;
            case CONST:                      // ✔ final 常量
                System.out.println("[case 常量] CONST");
                break;
        }

        // ❌ 错误示例（取消注释会编译报错）：
        // int y = 3;
        // int var = 5;
        // switch (y) {
        //     case var:        // ❌ 变量名，不是常量
        //     case y:          // ❌ 变量名
        //     case 1, 2:       // ❌ 逗号不能表示多个值（JDK14 之前的写法错误）
        //     case 1..5:       // ❌ 不支持范围
        //     case Math.abs(1) // ❌ 方法调用，不是编译期常量
        // }
        //
        // 补充陷阱：final 变量不一定可用于 case！
        // final int RUNTIME = new java.util.Random().nextInt(10);
        // switch (y) { case RUNTIME: }      // ❌ final 但值在运行期才确定，不是编译期常量

        /* =========================================================
         * 注意点 3：同一个 switch 中，case 常量值必须互不相同
         * ========================================================= */

        switch (x) {
            case 1:
                System.out.println("[case 唯一] case 1");
                break;
            case 2:
                System.out.println("[case 唯一] case 2");
                break;
            // case 1:   // ❌ 重复！编译错误：duplicate case label
        }

        /* =========================================================
         * 注意点 4：break 用来跳出 switch
         *   有 break   → 执行完本分支后跳出
         *   无 break   → "穿透"，顺序执行后续 case，直到 switch 结尾或遇到 break
         * ========================================================= */

        // --- 4.1 每个分支都有 break（正确做法）---
        int month = 2;
        System.out.println("---- 有 break ----");
        switch (month) {
            case 1: System.out.println("1月"); break;
            case 2: System.out.println("2月"); break;   // ← 输出后立即跳出
            case 3: System.out.println("3月"); break;
        }
        // 输出：2月

        // --- 4.2 没有 break（穿透）---
        System.out.println("---- 无 break，发生穿透 ----");
        switch (month) {
            case 1: System.out.println("1月");
            case 2: System.out.println("2月");   // ← 匹配 2，从这里开始
            case 3: System.out.println("3月");   // ← 继续穿透，也输出
                // 一直到 switch 结尾
        }
        // 输出：
        // 2月
        // 3月

        // --- 4.3 利用穿透：多个 case 共享同一段逻辑（合法且常用）---
        System.out.println("---- 利用穿透：判断工作日/休息日 ----");
        int day = 6;   // 1~7 表示周一到周日
        switch (day) {
            case 1: case 2: case 3: case 4: case 5:
                System.out.println("工作日");
                break;                    // 这一组逻辑结束必须 break
            case 6: case 7:
                System.out.println("休息日");  // ← 输出
                break;
            default:
                System.out.println("非法日期");
        }

        /* =========================================================
         * 注意点 5：default 可选、位置灵活；没有匹配 case 时执行
         * ========================================================= */

        // --- 5.1 没有 default：匹配不到时什么都不做 ---
        int v1 = 99;
        System.out.println("---- 没有 default ----");
        switch (v1) {
            case 1: System.out.println("1"); break;
            case 2: System.out.println("2"); break;
            // v1=99 匹配不到任何 case，也没有 default，直接跳出
        }
        System.out.println("v1=99，switch 什么都没输出");

        // --- 5.2 default 放中间（位置灵活，但不推荐）---
        int v2 = 100;
        System.out.println("---- default 放中间 ----");
        switch (v2) {
            case 1:
                System.out.println("case 1");
                break;
            default:
                System.out.println("default 命中");   // ← 输出
                // ⚠️ 这里没写 break，会继续往下穿透到 case 2！
            case 2:
                System.out.println("case 2（被穿透到）");  // ← 也被输出
                break;
        }
        // 输出：
        // default 命中
        // case 2（被穿透到）
        // ⚠️ 把 default 放中间且不写 break，是极易踩的坑，建议 default 一律写末尾

        // --- 5.3 default 放末尾（最常见、最安全）---
        int v3 = 100;
        System.out.println("---- default 放末尾 ----");
        switch (v3) {
            case 1: System.out.println("1"); break;
            default:
                System.out.println("default 兜底");   // ← 输出
        }
    }
}
