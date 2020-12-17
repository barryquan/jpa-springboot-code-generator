package com.github.barry.akali.base.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.barry.akali.base.utils.IConstants;

import lombok.ToString;

/**
 * 基础Dto信息<br>
 * 所有的Dto都继承该对象（每个Dto都有相同的字段）<br>
 * 
 * @author barry
 *
 */
@ToString
public abstract class BaseDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 实体ID, 主键 */
    protected Long id;

    /**
     * 创建人
     */
    protected String createdBy;

    /** 实体创建时间 */
    @DateTimeFormat(pattern = IConstants.DATE_TIME_MS_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = IConstants.DATE_TIME_MS_FORMAT)
    protected LocalDateTime createdDate;

    /**
     * 更新人
     */
    protected String lastModifiedBy;

    /** 实体更新时间 */
    @DateTimeFormat(pattern = IConstants.DATE_TIME_MS_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = IConstants.DATE_TIME_MS_FORMAT)
    protected LocalDateTime lastModifiedDate;

    /** 实体删除标记，为false表示删除 */
    protected Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
