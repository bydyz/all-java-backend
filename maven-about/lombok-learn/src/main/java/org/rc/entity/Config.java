package org.rc.entity;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 演示 @Value 和 @RequiredArgsConstructor 注解
 *
 * @Value 创建不可变类（所有字段 private final）
 * @RequiredArgsConstructor 为 final 和 @NonNull 字段生成构造函数
 */
@Value
@RequiredArgsConstructor
public class Config {

    // @Value 使类为 final，字段为 private final
    String host;
    int port;

    // @NonNull 字段会被加入构造函数
    @NonNull
    String environment;
}
