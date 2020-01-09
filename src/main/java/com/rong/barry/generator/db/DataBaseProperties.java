package com.rong.barry.generator.db;

import lombok.Data;

/**
 * 数据库的连接配置类
 * 
 * @author quansr
 *
 */
@Data
public class DataBaseProperties {

    /**
     * 数据库连接
     */
    private String jdbcUrl;

    /**
     * 数据库的用户名
     */
    private String user;

    /**
     * 数据库的密码
     */
    private String password;

    /**
     * 数据库的驱动
     */
    private String driver;
}
