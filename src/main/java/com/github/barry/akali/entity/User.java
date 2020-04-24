package com.github.barry.akali.entity;

import javax.persistence.Entity;

import com.github.barry.akali.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户数据库实体
 * 
 * @author quansr
 * @date 创建时间：2019年12月13日 下午2:03:45
 * @version 1.0
 */
@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -8502915311006879921L;

    /**
     * 用户名
     */
    private String username;

    private String email;

    private String password;

    private Integer age;

}
