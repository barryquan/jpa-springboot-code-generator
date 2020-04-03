package com.github.barry.akali.base.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 基础Dto信息<br>
 * 所有的Dto都继承该对象（每个Dto都有相同的字段）<br>
 * 
 * @author barry
 *
 */
@Setter
@Getter
@ToString
public abstract class BaseDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 实体ID, 主键 */
    protected Integer id;

    /** 实体创建时间 */
    protected LocalDateTime createdDate;

    /** 实体更新时间 */
    protected LocalDateTime modifiedDate;

    /** 实体删除标记，为false表示删除 */
    protected Boolean isActive;
}
