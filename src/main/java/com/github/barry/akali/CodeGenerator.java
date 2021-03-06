package com.github.barry.akali;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.github.barry.akali.generator.config.CodeGeneratorConfig;
import com.github.barry.akali.generator.config.GeneratorConstants;
import com.github.barry.akali.generator.config.ModuleConfig;
import com.github.barry.akali.generator.db.DataBaseEntityUtils;
import com.github.barry.akali.generator.ex.CodegenException;
import com.github.barry.akali.generator.metadata.DefaultEntityInfoParser;
import com.github.barry.akali.generator.metadata.EntityInfo;
import com.github.barry.akali.generator.metadata.FieldInfo;
import com.github.barry.akali.generator.metadata.IEntityParser;
import com.github.barry.akali.generator.metadata.IdInfo;
import com.github.barry.akali.generator.render.DefaultRender;
import com.github.barry.akali.generator.render.IRender;
import com.github.barry.akali.generator.type.OracleSqlTypeConvert;
import com.github.barry.akali.generator.type.SqlTypeConvert;
import com.github.barry.akali.generator.utils.ReflectUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于jpa的实体自动生成对应的controller、service、repository的代码生成器
 * 
 * @author quansr
 * @date 创建时间：2019年12月13日 下午1:49:54
 * @version 1.0
 */
@Slf4j
public class CodeGenerator {

    /**
     * 代码生成器配置类
     */
    private CodeGeneratorConfig config;

    /**
     * 配置文件读取类
     */
    private Properties properties;

    /**
     * 模板集合
     */
    private List<String> moduleList = new LinkedList<>();

    /**
     * 实体解析类
     */
    private IEntityParser entityParser;

    /**
     * 模板渲染类
     */
    private IRender render;

    /**
     * 数据库类型转成Java实体的类型转换器
     */
    private SqlTypeConvert sqlTypeConvert;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String codeGeneratorConfigPath = "src/main/resources/application.yml";
        String entityPackage = "com.github.barry.akali.entity";
        new CodeGenerator(codeGeneratorConfigPath) //
                .useDb(true)// 是否使用数据表进行创建，设为true后，packInclude方法不生效
                .setSqlTypeConvert(new OracleSqlTypeConvert())//
                .packInclude(entityPackage) // 批量加入生成的实体类包名
                // .clazzInclude(me.itlearner.jpacodegen.sample.entity.SampleUser.class) //
                // 加入生成的实体类名
                // .clazzExlude(me.itlearner.jpacodegen.sample.entity.SampleUser.class) //
                // 排除生成的实体类名，通常与packInclude混用，以排除包下的特殊实体类不参与生成代码
                .packSuperClazz(GeneratorConstants.DEFAULT_ENTITY_SUPER_CLASS) // 实体需要继承的父类，用来排除不需要加入到实体的字段
                .registerRender(GeneratorConstants.ENTITY_MODULE) // 注册实体模板
                .registerRender(GeneratorConstants.DTO_MODULE) // 注册dto的模板
                .registerRender(GeneratorConstants.RESPONSE_DTO_MODULE) // 注册response的模板
                .registerRender(GeneratorConstants.SEARCH_MODULE) // 注册搜索的模板
                .registerRender(GeneratorConstants.REPOSITORY_MODULE) // 注册repository的模板
                .registerRender(GeneratorConstants.SERVICE_MODULE) // 注册service的模板
                .registerRender(GeneratorConstants.CONTROLLER_MODULE) // 注册控制器的模板
                .generate(); // 开始自动生成
        log.info("thanks you use,code generator sucess");
        log.info("generator code use {} ms", System.currentTimeMillis() - start);
    }

    /**
     * 类型转换器
     * 
     * @param sqlTypeConvert
     * @return
     */
    private CodeGenerator setSqlTypeConvert(SqlTypeConvert sqlTypeConvert) {
        this.sqlTypeConvert = sqlTypeConvert;
        return this;
    }

    /**
     * 是否使用数据库进行创建
     * 
     * @param isUseDb
     * @return
     */
    private CodeGenerator useDb(boolean isUseDb) {
        config.setUseDb(isUseDb);
        return this;
    }

    /**
     * 反射获取实体的父类
     * 
     * @param superEntityClass
     * @return
     */
    private CodeGenerator packSuperClazz(String superEntityClass) {
        try {
            config.setSuperEntityClass(Class.forName(superEntityClass));
        } catch (ClassNotFoundException e) {
            log.error("cant not find super class,class={},{}", superEntityClass, e);
        }
        return this;
    }

    /**
     * 
     * @param configLocation 配置文件地址
     */
    public CodeGenerator(String configLocation) {
        try {
            if (configLocation.endsWith(GeneratorConstants.PROPERTIES_CONFIG_SUFFIX)) {
                properties = new Properties();
                properties.load(new FileInputStream(new File(configLocation)));
            } else if (configLocation.endsWith(GeneratorConstants.YML_CONFIG_SUFFIX)) {
                YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
                yaml.setResources(new FileSystemResource(configLocation));// File引入
                properties = yaml.getObject();
            }

            config = new CodeGeneratorConfig();

            config.setAuthor(properties.getProperty(GeneratorConstants.AUTHOR_CONFIG, System.getProperty("user.name")));
            config.setComments(properties.getProperty(GeneratorConstants.COMMENTS_CONFIG, ""));
            config.setDate(LocalDate.now().toString());

            config.setFtlPath(
                    properties.getProperty(GeneratorConstants.TEMPLATE_DIR, GeneratorConstants.DEFAULT_TEMPLATE_DIR));
            config.setCover(Boolean
                    .parseBoolean(properties.getProperty(GeneratorConstants.COVER, GeneratorConstants.DEFAULT_COVER)));

            config.setEntityFlag(properties.getProperty(GeneratorConstants.EntityConstants.PACKAGE, ""));
            config.setBaseProjectPath(properties.getProperty(GeneratorConstants.BASE_PROJECT_PATH,
                    GeneratorConstants.DEFAULT_PROJECT_PATH));
            // 实体的主键
            config.setEntityIdClass(properties.getProperty(GeneratorConstants.EntityConstants.ID_CLASS,
                    GeneratorConstants.EntityConstants.DEFAULT_ID_CLASS));
            // 实体的主键包名
            config.setEntityIdPackName(properties.getProperty(GeneratorConstants.EntityConstants.ID_PACKAGE, ""));

            // 数据库相关的配置
            config.getDbProperties()
                    .setDriver(properties.getProperty(GeneratorConstants.DBConstants.DRIVER_CLASS_NAME));
            config.getDbProperties().setJdbcUrl(properties.getProperty(GeneratorConstants.DBConstants.URL));
            config.getDbProperties().setPassword(properties.getProperty(GeneratorConstants.DBConstants.PASSWORD));
            config.getDbProperties().setUser(properties.getProperty(GeneratorConstants.DBConstants.USERNAME));
            // custom other params
            Map<String, String> otherParams = new HashMap<>();
            config.setOtherParams(otherParams);

            // 实体解析器
            entityParser = new DefaultEntityInfoParser();

            // 渲染器
            render = new DefaultRender(config);

            log.info("init code generator success.");

        } catch (IOException e) {
            throw new CodegenException("init code generator failed.", e);
        }
    }

    /**
     * 添加包下的所有实体
     *
     * @param packages 包名
     */
    public CodeGenerator packInclude(String... packages) {
        if (!config.isUseDb()) {
            for (String pack : packages) {
                config.getEntityClasses()
                        .addAll(ReflectUtils.getClassListByAnnotation(pack, javax.persistence.Entity.class));
            }
        }
        return this;
    }

    /**
     * 添加实体类，必须由{@link javax.persistence.Entity} 修饰
     *
     * @param classes 实体类
     */
    public CodeGenerator clazzInclude(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            config.getEntityClasses().add(clazz);
        }
        return this;
    }

    /**
     * 排除某些实体
     *
     * @param classes 要排除的实体，通常在{@link #packInclude(String...)}之后使用，排除一些特殊的类
     */
    public CodeGenerator clazzExlude(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            config.getEntityClasses().remove(clazz);
        }
        return this;
    }

    /**
     * 解析模块配置 1、 {module}.suffix 为自动生成的java类的后缀<br>
     * 如：dto.suffix=Dto，则自动生成的Java类如：UserDto 2、<br>
     * 
     * @param module 模块
     * @return 模块配置
     */
    private ModuleConfig parseModuleConfig(String module) {
        ModuleConfig moduleConfig = new ModuleConfig();
        moduleConfig.setClassNameSuffix(properties.getProperty(
                GeneratorConstants.APP_DEFAULT_CONFIG_PREFIX + module + GeneratorConstants.DEFAULT_CLASS_SUFFIX,
                module.substring(0, 1).toUpperCase().concat(module.substring(1))));
        moduleConfig.setFtlName(properties.getProperty(
                GeneratorConstants.APP_DEFAULT_CONFIG_PREFIX + module + GeneratorConstants.DEFAULT_CLASS_TEMPLATE,
                module + GeneratorConstants.DEFAULT_TEMPLATE_NAME_SUFFIX));
        moduleConfig.setPackageName(properties.getProperty(
                GeneratorConstants.APP_DEFAULT_CONFIG_PREFIX + module + GeneratorConstants.DEFAULT_CLASS_PACKAGE,
                module));
        return moduleConfig;
    }

    public void generate() {
        List<EntityInfo> entityInfos = null;
        if (!config.isUseDb()) {
            log.info("use entity package generator");
            entityInfos = config.getEntityClasses().stream().map(entityParser::parse).filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            log.info("use db generator");
            entityInfos = getEntityFromDb();
            entityInfos.forEach(e -> {
                e.setPackageName(config.getEntityFlag());
                Optional<FieldInfo> fieldOptional = e.getFields().stream()
                        .filter(a -> Objects.equals(Boolean.TRUE, a.getIsPk())).findFirst();
                IdInfo idInfo = new IdInfo();
                if (fieldOptional.isPresent()) {
                    idInfo.setClassName(fieldOptional.get().getClassName());
                    idInfo.setPackageName(fieldOptional.get().getPackageName());
                    e.setHasPk(Boolean.TRUE);
                } else {
                    idInfo.setClassName(config.getEntityIdClass());
                    idInfo.setPackageName(config.getEntityIdPackName());
                }
                e.setId(idInfo);
            });
        }

        if (!CollectionUtils.isEmpty(entityInfos)) {
            log.info("find {} entity classes, now start generate code.", entityInfos.size());
            entityInfos.forEach(e -> {
                moduleList.forEach(m -> {
                    // 不使用数据库创建的，存在实体模板，直接跳过
                    if (Objects.equals(GeneratorConstants.ENTITY_MODULE, m) && !config.isUseDb()) {
                    } else {
                        render.render(e, m, config.getBaseProjectPath());
                    }

                });
            });
        } else {
            log.warn("find none entity class, please check your entity package or db is true.");
        }
    }

    /**
     * 从数据库中获取实体的信息
     * 
     * @return
     */
    private List<EntityInfo> getEntityFromDb() {
        List<String> superFieldList;
        if (config.getSuperEntityClass() != null) {
            List<FieldInfo> fieldInfos = entityParser.parseField(config.getSuperEntityClass());
            superFieldList = fieldInfos.stream().filter(a -> StringUtils.hasText(a.getName())).map(FieldInfo::getName)
                    .collect(Collectors.toList());
        } else {
            superFieldList = new ArrayList<>();
        }
        log.info("use db ,the jdbc url is {}", config.getDbProperties().getJdbcUrl());
        return DataBaseEntityUtils.getEntityFromDb(config.getDbProperties(), superFieldList, sqlTypeConvert);
    }

    /**
     * 注册渲染组件
     *
     * @param module 模块名
     * @return 代码生成器本身
     */
    public CodeGenerator registerRender(String module) {
        ModuleConfig moduleConfig = parseModuleConfig(module);
        config.getModuleConfigMap().put(module, moduleConfig);
        moduleList.add(module);
        return this;
    }
}
