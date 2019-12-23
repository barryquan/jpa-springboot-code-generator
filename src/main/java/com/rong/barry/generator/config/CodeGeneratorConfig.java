package com.rong.barry.generator.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;


/**
 * 自动生成代码配置类
 *
 */
@Data
public class CodeGeneratorConfig {
    
    /**
     * 实体的所在包名
     */
    private String entityFlag;
    
    /**
     * 实体集合
     */
    private List<Class<?>> entityClasses = new ArrayList<>(256);
    
    /**
     * 模板文件所在目录
     */
    private String ftlPath;
    
    /**
     * 文件的作者
     */
    private String author;
    
    /**
     * 创建的时间
     */
    private String date;
    
    /**
     * 注释
     */
    private String comments;
    
    /**
     * 转换是否完成
     */
    private boolean cover;

    /**
     * 各个类型模板的配置文件
     */
    private Map<String, ModuleConfig> moduleConfigMap = new HashMap<>();

    /**
     * 其他个性配置
     */
    private Map<String, String> otherParams;
}
