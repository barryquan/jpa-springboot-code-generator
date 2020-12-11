package com.github.barry.akali.generator.render;

import java.util.Map;
import java.util.Set;

import com.github.barry.akali.generator.metadata.EntityInfo;

import lombok.Data;

/**
 * 渲染请求
 *
 * @author barry
 */
@Data
public class RenderingRequest {

    /**
     * 模块名称
     */
    private String ftlName;

    /**
     * 模板路径
     */
    private String ftlPath;

    /**
     * 保存的路径
     */
    private String savePath;

    /**
     * className类对应的包名
     */
    private String packageName;

    /**
     * 是否成功生成
     */
    private boolean cover;

    /**
     * 类名
     */
    private String className;

    /**
     * 作者
     */
    private String author;

    /**
     * 字符串类型的创建时间
     */
    private String date;

    /**
     * 全局注释
     */
    private String comments;

    /**
     * 实体信息
     */
    private EntityInfo entity;
    /**
     * 需要导入的包
     */
    private Set<String> imports;
    /**
     * 上一步生成的信息
     */
    private Map<String, RenderingResponse> lastRenderResponse;
    /**
     * 其他的参数
     */
    private Map<String, String> otherParams;

    /**
     * 实体中是否包含时间的字段
     */
    private Boolean hasDateParam = Boolean.FALSE;
}
