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
     * 主键类名
     */
    private IdInfo id;

    /**
     * 所有< 属性名, 类 >
     */
    private List<FieldInfo> fields;

    /**
     * 注解
     */
    private List<AnnotationInfo> annotations;

}
