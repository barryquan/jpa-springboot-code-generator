package com.github.barry.akali.generator.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import com.github.barry.akali.generator.metadata.EntityInfo;
import com.github.barry.akali.generator.metadata.FieldInfo;
import com.github.barry.akali.generator.type.ColumnType;
import com.github.barry.akali.generator.type.SqlTypeConvert;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库实体字段映射工具类
 * 
 * @author barry
 *
 */
@Slf4j
public class DataBaseEntityUtils {

    /**
     * 从数据库加载实体的信息
     * 
     * @param dbProperties
     * @param sqlTypeConvert
     * @return
     */
    public static List<EntityInfo> getEntityFromDb(DataBaseProperties dbProperties, List<String> exludeFiledList,
            SqlTypeConvert sqlTypeConvert) {
        Connection connection = getConnection(dbProperties);
        List<EntityInfo> entityList = new ArrayList<>();
        getTableInfo(connection, entityList, dbProperties.getUser());
        getFiledInfo(connection, entityList, exludeFiledList, sqlTypeConvert);
        closeConnection(connection);
        return entityList;
    }

    /**
     * 获取数据库字段信息
     * 
     * @param connection
     * @param entityList
     * @param exludeFiledList 需要过滤的字段
     * @param sqlTypeConvert
     */
    private static void getFiledInfo(Connection connection, List<EntityInfo> entityList, List<String> exludeFiledList,
            SqlTypeConvert sqlTypeConvert) {
        entityList.forEach(e -> {
            try {
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                String primaryKeyColumName = getPrimaryKeyColumName(databaseMetaData, connection.getCatalog(),
                        e.getTableName());
                ResultSet resultSet = databaseMetaData.getColumns(connection.getCatalog(), "%", e.getTableName(), "%");
                List<FieldInfo> fieldList = new ArrayList<>();
                while (resultSet.next()) {
                    FieldInfo fieldInfo = new FieldInfo();
                    String columName = resultSet.getString("COLUMN_NAME");
                    fieldInfo.setColumnName(columName);
                    String filedName = replaceUnderlineAndfirstToUpper(columName.toLowerCase(), "_", "");
                    if (!exludeFiledList.contains(filedName)) {
                        fieldInfo.setName(filedName);
                        String typeName = resultSet.getString("TYPE_NAME").toUpperCase();
                        int columnSize = resultSet.getInt("COLUMN_SIZE");
                        int decimalDigits = resultSet.getInt("DECIMAL_DIGITS");
                        String sqlType = "";
                        // 精度大于0才拼接
                        if (decimalDigits > 0) {
                            sqlType = typeName + "(" + columnSize + "," + decimalDigits + ")";
                        } else {
                            sqlType = typeName + "(" + columnSize + ")";
                        }

                        ColumnType columnType = sqlTypeConvert.getColumnType(sqlType.toLowerCase());
                        fieldInfo.setPackageName(columnType.getPackageName());
                        fieldInfo.setClassName(columnType.getClassName());
                        fieldInfo.setComment(Optional.ofNullable(resultSet.getString("REMARKS")).orElse(filedName));
                        fieldInfo.setIsPk(Objects.equals(primaryKeyColumName, columName));
                        fieldList.add(fieldInfo);
                    }
                }
                e.setFields(fieldList);
            } catch (SQLException e1) {
                log.error("获取表的字段报错，原因={}", e1);
                throw new RuntimeException(e1);
            }

        });
    }

    /**
     * 获取表的主键字段
     * 
     * @param databaseMetaData
     * @param catalog
     * @param tableName        数据表名称
     * @return
     */
    private static String getPrimaryKeyColumName(DatabaseMetaData databaseMetaData, String catalog, String tableName) {

        try {
            ResultSet pkInfo = databaseMetaData.getPrimaryKeys(catalog, "%", tableName);
            String pkColumName = "";
            while (pkInfo.next()) {
                pkColumName = pkInfo.getString("COLUMN_NAME");
                log.info("获取表={}的主键成功,获取到主键字段为={}", tableName, pkColumName);
            }
            return pkColumName;
        } catch (SQLException e) {
            log.error("获取表={}的主键失败，原因={}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据库表的信息
     * 
     * @param connection
     * @param entityList
     * @param dbUser
     */
    private static void getTableInfo(Connection connection, List<EntityInfo> entityList, String dbUser) {
        DatabaseMetaData databaseMetaData;
        try {
            databaseMetaData = connection.getMetaData();
            ResultSet resultSet1 = databaseMetaData.getTables(connection.getCatalog(), dbUser.toUpperCase(), null,
                    new String[] { "TABLE" });
            while (resultSet1.next()) {
                EntityInfo entityInfo = new EntityInfo();
                String tableName = resultSet1.getString("TABLE_NAME");
                entityInfo.setTableName(tableName);
                entityInfo.setClassName(
                        replaceUnderlineAndfirstToUpper(firstCharacterToUpper(tableName.toLowerCase()), "_", ""));
                String tableComment = Optional.ofNullable(resultSet1.getString("REMARKS")).orElse(tableName);
                log.info("tableName={},tableComment={}", tableName, tableComment);
                entityInfo.setComment(tableComment);
                entityList.add(entityInfo);
            }
        } catch (SQLException e1) {
            log.error("获取数据库表出错，原因={}", e1.getMessage(), e1);
            throw new RuntimeException(e1);
        }

    }

    /**
     * 获取数据库连接
     * 
     * @return
     */
    private static Connection getConnection(DataBaseProperties dbProperties) {
        Connection connection = null;
        try {
            Properties props = new Properties();
            props.put("remarksReporting", "true");
            props.put("user", dbProperties.getUser());
            props.put("password", dbProperties.getPassword());
            Class.forName(dbProperties.getDriver());
            connection = DriverManager.getConnection(dbProperties.getJdbcUrl(), props);
        } catch (ClassNotFoundException e) {
            log.error("找不到对应的数据库驱动，驱动为={},原因={}", dbProperties.getDriver(), e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            log.error("连接数据库出错，原因={}", e);
            throw new RuntimeException(e);
        }

        return connection;
    }

    /**
     * 替换字符串并让它的下一个字母为大写<br>
     * 如：srcStr=abc_dfg,oldStr=_,newStr="@"<br>
     * 则替换后的字符串为：abc@Dfg
     * 
     * @param srcStr 源字符串
     * @param oldStr 需要被替换的字符串
     * @param newStr 替换的字符串
     * @return
     */
    public static String replaceUnderlineAndfirstToUpper(String srcStr, String oldStr, String newStr) {
        StringBuilder builder = new StringBuilder();
        int first = 0;
        while (srcStr.indexOf(oldStr) != -1) {
            first = srcStr.indexOf(oldStr);
            if (first != srcStr.length()) {
                builder.append(srcStr.substring(0, first));
                builder.append(newStr);
                srcStr = srcStr.substring(first + oldStr.length(), srcStr.length());
                srcStr = firstCharacterToUpper(srcStr);
            }
        }
        builder.append(srcStr);
        return builder.toString();
    }

    /**
     * 首字母大写 字母的ascii编码前移<br>
     * 1.只有小写字母才前移，其他不管
     * 
     * @param srcStr
     * @return
     */
    public static String firstCharacterToUpper(String srcStr) {

        char[] cs = srcStr.toCharArray();
        // 是字母并且是小写字母
        if (Character.isLetter(cs[0]) && Character.isLowerCase(cs[0])) {
            cs[0] -= 32;
        }
        return String.valueOf(cs);
    }

    /**
     * 关闭数据库连接
     * 
     * @param connection
     */
    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("关闭数据连接失败，原因={}", e);
            }
        }
    }
}
