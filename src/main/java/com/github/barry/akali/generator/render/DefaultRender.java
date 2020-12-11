package com.github.barry.akali.generator.render;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.logging.log4j.util.Strings;
import org.springframework.util.CollectionUtils;

import com.github.barry.akali.generator.config.CodeGeneratorConfig;
import com.github.barry.akali.generator.config.GeneratorConstants;
import com.github.barry.akali.generator.config.ModuleConfig;
import com.github.barry.akali.generator.metadata.EntityInfo;
import com.github.barry.akali.generator.metadata.FieldInfo;
import com.github.barry.akali.generator.utils.FreeMarkerUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的模板引擎渲染实现类
 * 
 * 
 */
@Slf4j
public class DefaultRender implements IRender {

    /**
     * 代码生成的配置
     */
    private final CodeGeneratorConfig config;

    private Map<String, RenderingResponse> lastRenderingResponseMap = new HashMap<>();

    public DefaultRender(CodeGeneratorConfig config) {
        this.config = config;
    }

    /**
     * 渲染对应的模板
     */
    @Override
    public final RenderingResponse render(EntityInfo entityInfo, String module, String savePath) {
        RenderingRequest renderingRequest = new RenderingRequest();

        renderingRequest.setLastRenderResponse(lastRenderingResponseMap);

        ModuleConfig moduleConfig = config.getModuleConfigMap().get(module);
        // 设置类名,为实体名称+模板配置的对应后缀
        renderingRequest.setClassName(entityInfo.getClassName() + moduleConfig.getClassNameSuffix());
        if (Objects.equals(GeneratorConstants.ENTITY_MODULE, module)) {
            entityInfo.setClassName(renderingRequest.getClassName());
        }
        String packageName = moduleConfig.getPackageName();
        renderingRequest.setPackageName(packageName);
        savePath = savePath.endsWith(IRender.SLASH) ? savePath : savePath + IRender.SLASH;
        renderingRequest.setSavePath(savePath + packageName.replace(".", IRender.SLASH) + IRender.SLASH);
        renderingRequest.setFtlName(moduleConfig.getFtlName());

        renderingRequest.setFtlPath(config.getFtlPath());
        renderingRequest.setCover(config.isCover());

        renderingRequest.setEntity(entityInfo);

        renderingRequest.setAuthor(config.getAuthor());
        renderingRequest.setComments(config.getComments());
        renderingRequest.setDate(config.getDate());

        // fields ，只支持基本类型映射
        renderingRequest.setOtherParams(config.getOtherParams());

        // check for other imports
        Set<String> importSet = checkImports(entityInfo);
        renderingRequest.setImports(importSet);

        // 是否包含时间，包含的话，需要转换格式
        renderingRequest.setHasDateParam(hasDateParam(importSet));

        // use freemarker to render code.
        RenderingResponse lastRenderingResponse = FreeMarkerUtils.process(renderingRequest);

        lastRenderingResponseMap.put(module, lastRenderingResponse);
        log.info("render module is {}, response is {}", module, lastRenderingResponse);
        log.info("lastRenderingResponseMap is {}", lastRenderingResponseMap);
        return lastRenderingResponse;
    }

    /**
     * 检查是否包含时间的包需要导入
     * 
     * @param importSet
     * @return
     */
    private Boolean hasDateParam(Set<String> importSet) {
        for (String p : importSet) {
            if (Objects.equals(LocalDateTime.class.getTypeName(), p)) {
                return Boolean.TRUE;
            }
            if (Objects.equals(LocalDate.class.getTypeName(), p)) {
                return Boolean.TRUE;
            }
            if (Objects.equals(Date.class.getTypeName(), p)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 过滤不需要模板导入的包
     * 
     * @param entityInfo
     * @return
     */
    private Set<String> checkImports(EntityInfo entityInfo) {
        Set<String> imports = new HashSet<>();
        List<FieldInfo> fielList = entityInfo.getFields();

        if (!CollectionUtils.isEmpty(fielList)) {
            fielList.forEach(f -> {
                String packageName = f.getPackageName();
                if (Strings.isNotBlank(packageName) && !packageName.contains("java.lang")) {
                    imports.add(packageName);
                }
            });
        }
        return imports;
    }

}
