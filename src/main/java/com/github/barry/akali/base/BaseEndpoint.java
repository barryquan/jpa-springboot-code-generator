package com.github.barry.akali.base;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebBindingInitializer;

import com.github.barry.akali.base.dto.BaseResponseDto;
import com.github.barry.akali.base.utils.IConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * 基础控制器 其他控制器继承此控制器获得日期字段类型转换和防止XSS攻击的功能
 * 
 * @author barry
 * @date 2019年02月01日
 */
@Slf4j
public abstract class BaseEndpoint implements IConstants, WebBindingInitializer {

    /**
     * web数据绑定，主要是过滤XSS攻击<br>
     * 该方法只对控制器方法参数为String、Date、Timestamp生效<br>
     * 其他诸如POST、PUT方法的请求体参数无效<br>
     */
    @InitBinder
    @Override
    public void initBinder(WebDataBinder binder) {
        log.debug("进入BaseEndpoint的initBinder方法");
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                log.debug("传进来的String类型的字符串为={}", text);
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });

    }

    /**
     * 统一构造spring data rest的链接
     * 
     * @param controllerClass 控制器class，带有@Controller或@RestController的控制器
     * @param id              主键字段
     * @return 构造的链接实体
     */
    public Link getSelfLink(Class<?> controllerClass, Integer id) {

        return WebMvcLinkBuilder.linkTo(((BaseEndpoint) WebMvcLinkBuilder.methodOn(controllerClass)).details(id))
                .withSelfRel();
    }

    /**
     * 统一构造spring data rest 的分页数据信息
     * 
     * @param pageNumber      当前请求搜索的页吗
     * @param pageSize        当前请求搜索的分页大小
     * @param sortType        当前请求搜索的分页排序
     * @param request         当前请求搜索的参数
     * @param controllerClass 当前请求搜索的控制器类
     * @param page            当前需要构造分页信息的分页数据
     * @return spring data rest 的分页数据信息
     */
    public HttpEntity<PagedModel<EntityModel<?>>> doPage(int pageNumber, int pageSize, String sortType,
            ServletRequest request, Class<?> controllerClass, Page<? extends BaseResponseDto> page) {

        List<EntityModel<?>> list = new ArrayList<>();
        page.getContent().forEach(item -> {
            list.add(new EntityModel<>(item, getSelfLink(controllerClass, item.getId())));
        });
        // 组装分页信息
        PageMetadata pageMetadata = new PageMetadata(pageSize, pageNumber, page.getTotalElements(),
                page.getTotalPages());
        // 第一页链接
        Link firstLink = WebMvcLinkBuilder.linkTo(((BaseEndpoint) WebMvcLinkBuilder.methodOn(controllerClass))
                .getPageData(1, pageSize, sortType, request)).withRel("first");
        int totalPages = page.getTotalPages();
        Link prevLink = null;
        // 大于1时添加上一页链接
        if (pageNumber > 1 && totalPages > pageNumber) {
            prevLink = WebMvcLinkBuilder.linkTo(((BaseEndpoint) WebMvcLinkBuilder.methodOn(controllerClass))
                    .getPageData(pageNumber - 1, pageSize, sortType, request)).withRel("prev");
        }
        // 本页链接
        Link selfLink = WebMvcLinkBuilder.linkTo(((BaseEndpoint) WebMvcLinkBuilder.methodOn(controllerClass))
                .getPageData(pageNumber, pageSize, sortType, request)).withSelfRel();

        // 当总页数大于当前页数时，才有下一页
        Link nextLink = null;
        if (totalPages > pageNumber) {
            // 下一页链接
            nextLink = WebMvcLinkBuilder.linkTo(((BaseEndpoint) WebMvcLinkBuilder.methodOn(controllerClass))
                    .getPageData(pageNumber + 1, pageSize, sortType, request)).withRel("next");
        }

        // 最后一页链接
        Link lastLink = WebMvcLinkBuilder.linkTo(((BaseEndpoint) WebMvcLinkBuilder.methodOn(controllerClass))
                .getPageData(page.getTotalPages(), pageSize, sortType, request)).withRel("last");
        PagedModel<EntityModel<?>> pagedResources;

        // 判断是否存在上一页和下一页，返回的参数不能为空，总共存在4种情况
        if (Objects.nonNull(prevLink)) {
            if (Objects.nonNull(nextLink)) {
                pagedResources = new PagedModel<EntityModel<?>>(list, pageMetadata, firstLink, prevLink, selfLink,
                        nextLink, lastLink);
            } else {
                pagedResources = new PagedModel<EntityModel<?>>(list, pageMetadata, firstLink, prevLink, selfLink,
                        lastLink);
            }

        } else {
            if (Objects.nonNull(nextLink)) {
                pagedResources = new PagedModel<EntityModel<?>>(list, pageMetadata, firstLink, selfLink, nextLink,
                        lastLink);
            } else {
                pagedResources = new PagedModel<EntityModel<?>>(list, pageMetadata, firstLink, selfLink, lastLink);
            }

        }
        return new HttpEntity<PagedModel<EntityModel<?>>>(pagedResources);
    }

    /**
     * 抽象方法，由子类实现分页的数据的获取
     * 
     * @param pageNumber 查询的页码
     * @param pageSize   分页大小
     * @param sortType   排序字段
     * @param request    请求参数
     * @return spring data rest 的分页数据信息
     */
    protected abstract HttpEntity<PagedModel<EntityModel<?>>> getPageData(int pageNumber, int pageSize, String sortType,
            ServletRequest request);

    /**
     * 实体详情接口，交给子类实现
     * 
     * @param id 数据库主键id
     * @return spring data rest 的单个实体信息
     */
    protected abstract ResponseEntity<?> details(Integer id);

    /**
     * 构造List列表的spring data rest形式
     * 
     * @param sourcesList     列表数据
     * @param controllerClass 端点控制器
     * @return spring data rest 的集合实体信息
     */
    public ResponseEntity<?> doListResources(List<? extends BaseResponseDto> sourcesList, Class<?> controllerClass) {
        List<EntityModel<?>> resList = new ArrayList<>();
        sourcesList.forEach(item -> {
            resList.add(new EntityModel<>(item, getSelfLink(controllerClass, item.getId())));
        });
        return ResponseEntity.ok(new CollectionModel<EntityModel<?>>(resList));
    }

}
