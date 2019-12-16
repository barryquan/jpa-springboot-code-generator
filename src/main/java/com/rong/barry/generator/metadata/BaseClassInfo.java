package com.rong.barry.generator.metadata;

import lombok.Data;

/**
 * 类描述信息
 *
 * @author gaochen
 * Created on 2019/6/21.
 */
@Data
public abstract class BaseClassInfo {

    /**
     * 类名
     */
    protected String className;

    /**
     * 包名,如：java.lang.String
     */
    protected String packageName;

}