package com.rong.barry.generator.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * TODO
 *
 * @author gaochen
 * Created on 2019/6/20.
 */
@Data
public class CodeGeneratorConfig {
    private String entityFlag;
    private List<Class<?>> entityClasses = new ArrayList<>(256);
    private String ftlPath;
    private String author;
    private String date;
    private String comments;
    private boolean cover;

    private Map<String, ModuleConfig> moduleConfigMap = new HashMap<>();

    private Map<String, String> otherParams;
}
