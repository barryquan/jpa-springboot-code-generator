package com.github.barry.akali.generator.metadata;

import java.util.List;

/**
 * 实体解析器
 *
 */
public interface IEntityParser {

    /**
     * 将指定实体类解析为实体信息
     *
     * @param clazz 实体类
     * @return 实体信息
     */
    EntityInfo parse(Class<?> clazz);

    /**
     * 解析字段信息
     *
     * @param clazz 指定实体类
     * @return 字段信息
     */
    List<FieldInfo> parseField(Class<?> clazz);
}
