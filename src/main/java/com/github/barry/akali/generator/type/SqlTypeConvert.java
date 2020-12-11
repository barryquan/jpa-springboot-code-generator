package com.github.barry.akali.generator.type;

/**
 * sql数据库类型转换成实体类的转换器
 * 
 * @author quansr
 *
 */
public interface SqlTypeConvert {

    /**
     * 获取字段对应的包名和类名
     * 
     * @param sqlType 传入时，会转成小写，sql类型，如：varchar(12,0)
     * @return
     */
    ColumnType getColumnType(String sqlType);

}
