package org.rc.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动填充处理器
 * 实现 MetaObjectHandler 接口，处理实体类字段的自动填充
 * 在插入和更新操作时自动填充 createTime 和 updateTime 字段
 */
@Component
public class AutoFillHandler implements MetaObjectHandler {
    
    /**
     * 插入时自动填充
     * 当执行插入操作时，自动填充 createTime 和 updateTime 字段
     * 
     * @param metaObject 元对象，包含实体类的元信息
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 使用 strictInsertFill 方法进行严格填充
        // 第一个参数：元对象
        // 第二个参数：字段名
        // 第三个参数：字段类型
        // 第四个参数：填充的值
        
        // 填充 createTime 字段
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        
        // 填充 updateTime 字段
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
    
    /**
     * 更新时自动填充
     * 当执行更新操作时，自动填充 updateTime 字段
     * 
     * @param metaObject 元对象，包含实体类的元信息
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充 updateTime 字段
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}