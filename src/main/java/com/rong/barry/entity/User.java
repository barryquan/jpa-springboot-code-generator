package com.rong.barry.entity;

/**
 * 请输入类的介绍信息
 * @author quansr
 * @date 创建时间：2019年12月13日 下午2:03:45
 * @version 1.0
 */

import javax.persistence.Entity;

import com.rong.barry.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
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
