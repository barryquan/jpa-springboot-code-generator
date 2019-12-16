package com.rong.barry.generator.metadata;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO
 *
 * @author gaochen Created on 2019/6/21.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class FieldInfo extends BaseClassInfo {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段上的注解
     */
    private List<AnnotationInfo> annotations;

}