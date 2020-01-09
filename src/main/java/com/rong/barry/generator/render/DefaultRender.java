package com.rong.barry.generator.render;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.util.Strings;
import org.springframework.util.CollectionUtils;

import com.rong.barry.generator.config.CodeGeneratorConfig;
import com.rong.barry.generator.config.ModuleConfig;
import com.rong.barry.generator.metadata.EntityInfo;
import com.rong.barry.generator.metadata.FieldInfo;
import com.rong.barry.generator.utils.FreeMarkerUtils;

/**
 * 默认的模板引擎渲染实现类
 * 
 * 
 */
public class DefaultRender implements IRender {

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

        String packageName = moduleConfig.getFlag();

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
        renderingRequest.setImports(checkImports(entityInfo));

        // use freemarker to render code.
        RenderingResponse lastRenderingResponse = FreeMarkerUtils.process(renderingRequest);

        lastRenderingResponseMap.put(module, lastRenderingResponse);
        return lastRenderingResponse;
    }

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
