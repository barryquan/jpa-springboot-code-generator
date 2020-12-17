package com.github.barry.akali.base.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

/**
 * 属性复制工具，都从这里取
 * 
 * @author barry
 *
 */
public abstract class BeanMapperUtils {

    /**
     * 私有实例化
     */
    private static final Mapper DEFAULT_MAPPER = DozerBeanMapperBuilder.buildDefault();

    /**
     * 获取属性复制工具
     * 
     * @return
     */
    public static Mapper getDefaultBeanMapper() {
        return DEFAULT_MAPPER;
    }
}
