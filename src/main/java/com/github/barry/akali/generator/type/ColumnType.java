package com.github.barry.akali.generator.type;

/**
 * 获取实体类字段属性类信息接口
 * 
 * @author quansr
 *
 */
public interface ColumnType {

    /**
     * 获取字段属性所属类的包名，如：java.time.LocalDateTime
     * 
     * @return
     */
    String getPackageName();

    /**
     * 获取字段的属性的类名，如：LocalDateTime
     * 
     * @return
     */
    String getClassName();
}
