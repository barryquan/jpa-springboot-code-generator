package com.github.barry.akali.generator.render;

import java.util.Map;
import java.util.Set;

import com.github.barry.akali.generator.metadata.EntityInfo;

import lombok.Data;

/**
 * 渲染请求
 *
 * @author gaochen
 * Created on 2019/6/20.
 */
@Data
public class RenderingRequest {
    private String ftlName;
    private String ftlPath;
    private String savePath;
    private String packageName;
    private boolean cover;
    private String className;
    private String author;
    private String date;
    private String comments;
    private EntityInfo entity;
    private Set<String> imports;
    private Map<String, RenderingResponse> lastRenderResponse;
    private Map<String, String> otherParams;
}
