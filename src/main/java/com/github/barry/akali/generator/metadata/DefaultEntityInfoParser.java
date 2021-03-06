package com.github.barry.akali.generator.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.IdClass;
import javax.persistence.Table;

import com.github.barry.akali.generator.utils.ReflectUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的实体解析器
 *
 */
@Slf4j
public class DefaultEntityInfoParser extends BaseEntityParser {

    /**
     * 主键注解
     */
    private static final List<Class<? extends Annotation>> ID_CLASS_LIST = Arrays.asList(javax.persistence.Id.class,
            org.springframework.data.annotation.Id.class);

    @Override
    public EntityInfo parseEntity(Class<?> clazz) {
        EntityInfo entityInfo = new EntityInfo();
        entityInfo.setClassName(clazz.getSimpleName());

        String packageName = clazz.getPackage().getName();

        entityInfo.setPackageName(packageName);

        Table tableAnnotation = clazz.getAnnotation(javax.persistence.Table.class);
        if (tableAnnotation != null) {
            entityInfo.setTableName(tableAnnotation.name());
            entityInfo.setComment(tableAnnotation.name());
        }else {
            entityInfo.setComment(clazz.getSimpleName());
        }
        return entityInfo;
    }

    @Override
    public List<FieldInfo> parseField(Class<?> clazz) {
        List<FieldInfo> fields = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field f : declaredFields) {
            // 静态变量不加入生成的传输类中
            if (Modifier.isStatic(f.getModifiers())) {
                log.info("字段={} 为静态变量字段，忽略作为传输类的字段", f.getName());
                continue;
            }
            fields.add(getFieldInfo(f));
        }
        return fields;
    }

    /**
     * 解析字段信息
     * @param f
     * @return
     */
    private FieldInfo getFieldInfo(Field f) {
        FieldInfo fieldInfo = new FieldInfo();

        fieldInfo.setClassName(f.getType().getSimpleName());
        fieldInfo.setPackageName(f.getType().getTypeName());

        fieldInfo.setName(f.getName());
        fieldInfo.setComment(f.getName());

        // 字段注解
        Annotation[] fieldAnnotations = f.getAnnotations();
        if (fieldAnnotations.length > 0) {
            List<AnnotationInfo> annotationInfos = new ArrayList<>();

            for (Annotation annotation : fieldAnnotations) {
                AnnotationInfo annotationInfo = new AnnotationInfo();
                annotationInfo.setClassName(annotation.annotationType().getSimpleName());
                annotationInfo.setPackageName(annotation.annotationType().getPackage().getName());
            }
            fieldInfo.setAnnotations(annotationInfos);
        }
        return fieldInfo;
    }

    @Override
    public IdInfo parseId(Class<?> clazz) {
        // 尝试从类注解获取主键信息
        IdClass idClassAnnotation = clazz.getAnnotation(javax.persistence.IdClass.class);
        if (idClassAnnotation != null) {
            IdInfo idInfo = new IdInfo();
            idInfo.setClassName(idClassAnnotation.value().getSimpleName());
            idInfo.setPackageName(idClassAnnotation.value().getTypeName());
            return idInfo;
        }
        // 尝试获取第一个带主键注解的字段
        for (Class<? extends Annotation> idClass : ID_CLASS_LIST) {
            Optional<Field> fieldByAnnotation = ReflectUtils.getFieldByAnnotation(clazz, idClass);
            if (fieldByAnnotation.isPresent()) {
                Field field = fieldByAnnotation.get();
                IdInfo idInfo = new IdInfo();
                idInfo.setClassName(field.getType().getSimpleName());
                idInfo.setPackageName(field.getType().getTypeName());
                return idInfo;
            }
        }
        // try from super class
        Class<?> maybeExists = clazz.getSuperclass();
        if (!Object.class.equals(maybeExists)) {
            return parseId(maybeExists);
        }
        return null;
    }
}
