package com.rong.barry.generator.render;

import com.rong.barry.generator.metadata.EntityInfo;

/**
 * TODO
 *
 * @author gaochen
 * Created on 2019/6/20.
 */
public interface IRender {

    /**
     * 渲染为java文件
     *
     * @param entityInfo 实体信息
     * @param module     模块名称
     * @return 渲染结果
     */
    RenderingResponse render(EntityInfo entityInfo, String module);

}