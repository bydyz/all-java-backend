package org.rc.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 演示 @Slf4j 注解 - 自动生成日志对象
 * 演示 @SneakyThrows 注解 - 自动处理受检异常
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    private String orderId;
    private String productName;
    private int quantity;
    private double totalPrice;

    /**
     * 演示 @SneakyThrows - 自动处理受检异常
     * 即使方法抛出受检异常，也不需要 try-catch 或 throws 声明
     */
    @SneakyThrows
    public String formatOrder() {
        // 模拟可能抛出异常的操作
        log.info("格式化订单: {}", orderId);

        Thread.sleep(10); // 演示受检异常

        return String.format("订单号: %s, 商品: %s, 数量: %d, 总价: %.2f",
                orderId, productName, quantity, totalPrice);
    }
}
