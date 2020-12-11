package com.github.barry.akali.generator.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;

/**
 * 表字段类型对应的实体字段
 * 
 * @author quansr
 *
 */
public enum DbColumn implements ColumnType {

    // 基本类型
    BASE_BYTE("byte", ""), 
    BASE_SHORT("short", ""), 
    BASE_CHAR("char", ""), 
    BASE_INT("int", ""), 
    BASE_LONG("long", ""),
    BASE_FLOAT("float", ""), 
    BASE_DOUBLE("double", ""), 
    BASE_BOOLEAN("boolean", ""),

    // 包装类型
    BYTE("Byte", ""), 
    SHORT("Short", ""), 
    CHARACTER("Character", ""), 
    INTEGER("Integer", ""), 
    LONG("Long", ""),
    FLOAT("Float", ""), 
    DOUBLE("Double", ""), 
    BOOLEAN("Boolean", ""), 
    STRING("String", ""),
    
    
 // sql 包下数据类型
    DATE_SQL("Date", java.sql.Date.class.getTypeName()),
    TIME("Time", java.sql.Time.class.getTypeName()),
    TIMESTAMP("Timestamp", java.sql.Timestamp.class.getTypeName()),
    BLOB("Blob", java.sql.Blob.class.getTypeName()),
    CLOB("Clob", java.sql.Clob.class.getTypeName()),

    // java8 新时间类型
    LOCAL_DATE("LocalDate", LocalDate.class.getTypeName()), 
    LOCAL_TIME("LocalTime", LocalTime.class.getTypeName()),
    YEAR("Year", Year.class.getTypeName()), 
    YEAR_MONTH("YearMonth", YearMonth.class.getTypeName()),
    LOCAL_DATE_TIME("LocalDateTime", LocalDateTime.class.getTypeName()),
    INSTANT("Instant", Instant.class.getTypeName()),

    // 其他杂类
    BYTE_ARRAY("byte[]", ""), 
    OBJECT("Object", ""), 
    DATE("Date", java.util.Date.class.getTypeName()),
    BIG_INTEGER("BigInteger", BigInteger.class.getTypeName()),
    BIG_DECIMAL("BigDecimal", BigDecimal.class.getTypeName());

    /**
     * 类型
     */
    private final String className;

    /**
     * 包名
     */
    private final String packageName;

    private DbColumn(final String className, final String packageName) {
        this.className = className;
        this.packageName = packageName;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getClassName() {
        return className;
    }

}
