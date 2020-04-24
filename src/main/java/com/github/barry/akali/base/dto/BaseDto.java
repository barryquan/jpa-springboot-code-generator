package com.github.barry.akali.base.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础Dto信息<br>
 * 所有的Dto都继承该对象（每个Dto都有相同的字段）<br>
 * 
 * @author barry
 *
 */
public abstract class BaseDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 实体ID, 主键 */
    protected Integer id;

    /**
     * 创建人
     */
    protected String createdBy;

    /** 实体创建时间 */
    protected LocalDateTime createdDate;

    /**
     * 更新人
     */
    protected String lastModifiedBy;

    /** 实体更新时间 */
    protected LocalDateTime modifiedDate;

    /** 实体删除标记，为false表示删除 */
    protected Boolean isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
