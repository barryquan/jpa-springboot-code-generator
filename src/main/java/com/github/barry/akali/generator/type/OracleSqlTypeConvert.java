package com.github.barry.akali.generator.type;

/**
 * oracle的字段对应实体字段
 * 
 * @author quansr
 *
 */
public class OracleSqlTypeConvert implements SqlTypeConvert {

    @Override
    public ColumnType getColumnType(String sqlType) {
        String t = sqlType.toLowerCase();
        if (t.contains("char")) {
            return DbColumn.STRING;
        } else if (t.contains("date") || t.contains("timestamp")) {
            return DbColumn.LOCAL_DATE_TIME;
        } else if (t.contains("number")) {
            if (t.matches("number\\(+\\d\\)")) {
                return DbColumn.INTEGER;
            } else if (t.matches("number\\(+\\d{2}+\\)")) {
                return DbColumn.LONG;
            }
            return DbColumn.BIG_DECIMAL;
        } else if (t.contains("float")) {
            return DbColumn.FLOAT;
        } else if (t.contains("clob")) {
            return DbColumn.STRING;
        } else if (t.contains("blob")) {
            return DbColumn.BLOB;
        } else if (t.contains("binary")) {
            return DbColumn.BYTE_ARRAY;
        } else if (t.contains("raw")) {
            return DbColumn.BYTE_ARRAY;
        }
        return DbColumn.STRING;
    }

}
