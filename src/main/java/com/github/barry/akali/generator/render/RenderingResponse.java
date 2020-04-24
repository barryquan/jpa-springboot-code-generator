package com.github.barry.akali.generator.render;

import java.io.Serializable;

import lombok.Data;

/**
 * 渲染结果
 *
 * @author barry
 */
@Data
public class RenderingResponse implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 渲染是否成功：true为成功
     */
    private boolean success;

    /**
     * 渲染错误信息
     */
    private String errorMsg;

    /**
     * 模板名称
     */
    private String ftlName;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 类名
     */
    private String className;
}
