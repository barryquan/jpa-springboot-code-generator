package com.rong.barry.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/***
 * 数据库实体的公共属性
 * @author quansr
 *
 */
@Setter
@Getter
@MappedSuperclass
@ToString
public abstract class BaseEntity implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -1L;
    
    /** 实体ID, 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    /** 实体创建时间 */
    protected Date created = new Date();

    /** 实体更新时间 */
    protected Date modified;

    /** 实体删除标记，为false表示删除 */
    protected Boolean isActive = Boolean.TRUE;

}
