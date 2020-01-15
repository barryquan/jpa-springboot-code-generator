package com.github.barry.akali.generator.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.github.barry.akali.generator.metadata.EntityInfo;
import com.github.barry.akali.generator.metadata.FieldInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库实体字段隐射帮助类
 * 
 * @author quansr
 *
 */
@Slf4j
public class DataBaseEntityUtils {

    /**
     * 从数据库加载实体的信息
     * 
     * @param dbProperties
     * @return
     */
    public static List<EntityInfo> getEntityFromDb(DataBaseProperties dbProperties, List<String> exludeFiledList) {
        Connection connection = getConnection(dbProperties);
        List<EntityInfo> entityList = new ArrayList<>();
        getTableInfo(connection, entityList);
        getFiledInfo(connection, entityList, exludeFiledList);
        closeConnection(connection);
        return entityList;
    }

    /**
     * 获取数据库字段信息
     * 
     * @param connection
     * @param entityList
     * @param exludeFiledList 需要过滤的字段
     */
    private static void getFiledInfo(Connection connection, List<EntityInfo> entityList, List<String> exludeFiledList) {
        entityList.forEach(e -> {
            try {
                ResultSet resultSet = connection.getMetaData().getColumns(connection.getCatalog(), "%",
                        e.getTableName(), "%");
                List<FieldInfo> fieldList = new ArrayList<>();
                while (resultSet.next()) {
                    FieldInfo fieldInfo = new FieldInfo();
                    String columName = resultSet.getString("COLUMN_NAME");
                    fieldInfo.setColumnName(columName);
                    String filedName = replaceUnderlineAndfirstToUpper(columName.toLowerCase(), "_", "");
                    if (!exludeFiledList.contains(filedName)) {
                        fieldInfo.setName(filedName);
                        String typeName = resultSet.getString("TYPE_NAME").toUpperCase();
                        fieldInfo.setPackageName(matchPackName(typeName));
                        fieldInfo.setClassName(matchClassName(typeName));
                        fieldInfo.setComment(resultSet.getString("REMARKS"));
                        fieldList.add(fieldInfo);
                    }
                }
                e.setFields(fieldList);
            } catch (SQLException e1) {
                log.error("获取表的字段报错，原因={}", e1);
            }

        });
    }

    /**
     * 数据库对应的字段匹配成java的类名，如：String
     * 
     * @param typeName
     * @return
     */
    private static String matchClassName(String typeName) {
        String className = "";
        switch (typeName) {
        case "INT": // int
            className = Integer.class.getSimpleName();
            break;
        case "DATETIME": // 时间，使用java8的时间
            className = LocalDateTime.class.getSimpleName();
            break;
        case "BIT": // boolean
            className = Boolean.class.getSimpleName();
            break;
        case "VARCHAR": // 字符串
            className = String.class.getSimpleName();
            break;
        default:
            className = String.class.getSimpleName();
            break;
        }
        return className;
    }

    /**
     * 数据库对应的字段匹配成java的类库的包名<br>
     * 如：java.time.LocalDateTime
     * 
     * @param typeName
     * @return
     */
    private static String matchPackName(String typeName) {
        String packName = "";
        switch (typeName) {
        case "INT": // int lang包 不用管

            break;
        case "DATETIME": // 时间，使用java8的时间
            packName = LocalDateTime.class.getTypeName();// "java.time.LocalDateTime";
            break;
        case "BIT": // boolean lang包 不用管

            break;
        case "VARCHAR": // 字符串 lang包 不用管

            break;
        default:
            break;
        }
        return packName;
    }

    /**
     * 获取数据库表的信息
     * 
     * @param connection
     * @param entityList
     */
    private static void getTableInfo(Connection connection, List<EntityInfo> entityList) {
        DatabaseMetaData databaseMetaData;
        try {
            databaseMetaData = connection.getMetaData();
            ResultSet resultSet1 = databaseMetaData.getTables(connection.getCatalog(), "root", null,
                    new String[] { "TABLE" });
            while (resultSet1.next()) {
                EntityInfo entityInfo = new EntityInfo();
                entityInfo.setTableName(resultSet1.getString("TABLE_NAME"));
                entityInfo.setClassName(replaceUnderlineAndfirstToUpper(
                        firstCharacterToUpper(resultSet1.getString("TABLE_NAME").toLowerCase()), "_", ""));
                entityList.add(entityInfo);
            }
        } catch (SQLException e1) {

            e1.printStackTrace();
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
            Class.forName(dbProperties.getDriver());
            connection = DriverManager.getConnection(dbProperties.getJdbcUrl(), dbProperties.getUser(),
                    dbProperties.getPassword());
        } catch (ClassNotFoundException e) {
            log.error("找不到对应的数据库驱动，驱动为={},原因={}", dbProperties.getDriver(), e);
        } catch (SQLException e) {
            log.error("连接数据库出错，原因={}", e);
        }

        return connection;
    }

    /**
     * 替换字符串并让它的下一个字母为大写
     * 
     * @param srcStr 源字符串
     * @param org    需要被替换的字符串
     * @param ob     替换的字符串
     * @return
     */
    public static String replaceUnderlineAndfirstToUpper(String srcStr, String org, String ob) {
        String newString = "";
        int first = 0;
        while (srcStr.indexOf(org) != -1) {
            first = srcStr.indexOf(org);
            if (first != srcStr.length()) {
                newString = newString + srcStr.substring(0, first) + ob;
                srcStr = srcStr.substring(first + org.length(), srcStr.length());
                srcStr = firstCharacterToUpper(srcStr);
            }
        }
        newString = newString + srcStr;
        return newString;
    }

    /**
     * 首字母大写 字母的ascii编码前移
     * 
     * @param srcStr
     * @return
     */
    public static String firstCharacterToUpper(String srcStr) {
        char[] cs = srcStr.toCharArray();
        cs[0] -= 32;

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