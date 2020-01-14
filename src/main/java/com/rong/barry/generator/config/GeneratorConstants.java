package com.rong.barry.generator.config;

import com.rong.barry.base.BaseEntity;

/**
 * 定义自动生成相关的常量
 * 
 * @author quansr
 *
 */
public interface GeneratorConstants {

    /**
     * src的目录前缀
     */
    String SRC_PATH_PREFIX = "src/main/";

    /**
     * 实体的模板前缀
     */
    String ENTITY_MODULE = "entity";

    /**
     * dto的模板前缀
     */
    String DTO_MODULE = "form";

    /**
     * 搜索dto的模板前缀
     */
    String SEARCH_MODULE = "search";

    /**
     * repository的模板前缀
     */
    String REPOSITORY_MODULE = "repository";

    /**
     * service的模板前缀
     */
    String SERVICE_MODULE = "service";

    /**
     * controller的模板前缀
     */
    String CONTROLLER_MODULE = "controller";

    /**
     * 默认的实体类的父类包名
     */
    String DEFAULT_ENTITY_SUPER_CLASS = BaseEntity.class.getName();
}
