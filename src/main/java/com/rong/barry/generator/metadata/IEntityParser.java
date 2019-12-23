package com.rong.barry.generator.metadata;

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
}
