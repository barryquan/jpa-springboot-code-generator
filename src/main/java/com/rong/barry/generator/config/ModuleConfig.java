package com.rong.barry.generator.config;

import lombok.Data;

/**
 * 模块名称
 * 
 * @author quansr
 *
 */
@Data
public class ModuleConfig {
    /**
     * 包名的后缀，如：com.abc,<br>
     * 则包名实际为：packageName+flag
     */
    private String flag;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 类名后缀，如：Service，则包名后缀为：xxxService
     */
    private String classNameSuffix;

    /**
     * 对应的模板文件名称
     */
    private String ftlName;

    /**
     * 保存的文件路径
     */
    private String savePath;
}
