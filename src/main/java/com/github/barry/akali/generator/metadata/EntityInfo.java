package com.github.barry.akali.generator.metadata;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实体信息
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EntityInfo extends BaseClassInfo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表在数据库的注释
     */
    private String comment;

    /**
     * 主键类名
     */
    private IdInfo id;

    /**
     * 该实体是否存在自定义的主键字段，true表示存在
     */
    private Boolean hasPk = Boolean.FALSE;

    /**
     * 所有< 属性名, 类 >
     */
    private List<FieldInfo> fields;

    /**
     * 注解
     */
    private List<AnnotationInfo> annotations;

}
