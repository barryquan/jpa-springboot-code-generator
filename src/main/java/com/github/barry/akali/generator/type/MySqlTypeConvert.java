package com.github.barry.akali.generator.type;

/**
 * mysql的字段对应实体字段
 * 
 * @author quansr
 *
 */
public class MySqlTypeConvert implements SqlTypeConvert {

    @Override
    public ColumnType getColumnType(String sqlType) {
        String t = sqlType.toLowerCase();
        if (t.contains("char")) {
            return DbColumn.STRING;
        } else if (t.contains("bigint")) {
            return DbColumn.LONG;
        } else if (t.contains("tinyint(1)")) {
            return DbColumn.BOOLEAN;
        } else if (t.contains("int")) {
            return DbColumn.INTEGER;
        } else if (t.contains("text")) {
            return DbColumn.STRING;
        } else if (t.contains("bit")) {
            return DbColumn.BOOLEAN;
        } else if (t.contains("decimal")) {
            return DbColumn.BIG_DECIMAL;
        } else if (t.contains("clob")) {
            return DbColumn.CLOB;
        } else if (t.contains("blob")) {
            return DbColumn.BLOB;
        } else if (t.contains("binary")) {
            return DbColumn.BYTE_ARRAY;
        } else if (t.contains("float")) {
            return DbColumn.FLOAT;
        } else if (t.contains("double")) {
            return DbColumn.DOUBLE;
        } else if (t.contains("json") || t.contains("enum")) {
            return DbColumn.STRING;
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            switch (t) {
            case "date":
                return DbColumn.LOCAL_DATE;
            case "time":
                return DbColumn.LOCAL_TIME;
            case "year":
                return DbColumn.YEAR;
            default:
                return DbColumn.LOCAL_DATE_TIME;
            }
        }
        return DbColumn.STRING;
    }

}
