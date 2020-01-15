package com.github.barry.akali.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.barry.akali.base.SearchFilter.Operator;
import com.github.barry.akali.base.conver.EnumConverter;

import io.beanmapper.BeanMapper;
import io.beanmapper.config.BeanMapperBuilder;

/**
 * 基础业务模型，用于实现基础的业务功能<br>
 * 本身自带强大的查询参数构造
 * 
 * @author barry
 *
 * @param <T>  数据库实体
 * @param <ID> 数据库实体的主键实体
 */
@Service
@Transactional(readOnly = true)
public abstract class BaseService<T, ID extends Serializable> {

    @Autowired
    protected BaseRepository<T, ID> baseRepository;

    /**
     * 默认删除标志的字段
     */
    private String isActive = "isActive";

    /**
     * 如果是采用spring boot的话，会自动注入，其他情况需要手动创建
     */
    @Autowired
    protected ObjectMapper objectMapper;

    BeanMapper beanMapper = new BeanMapperBuilder().addConverter(new EnumConverter()).build();

    public void setbaseRepository(BaseRepository<T, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    /**
     * 按照主键查询
     * 
     * @param id 主键
     * @return 返回id对应的实体
     */
    @Transactional(readOnly = true)
    public Optional<T> findOne(ID id) {
        return baseRepository.findById(id);
    }

    /**
     * 保存单个实体
     * 
     * @param t 实体
     * @return 返回保存的实体
     */
    @Transactional(readOnly = false)
    public T save(T t) {
        UserUtil.stamp(t);
        return baseRepository.save(t);
    }
    
    /***
     * 删除实体
     * @param id 主键id
     */
    @Transactional(readOnly = false)
    public void delete(ID id) {
        findOne(id).ifPresent(t ->{
            UserUtil.stamp(t);
            baseRepository.save(t);
        }); 
    }

    /**
     * 保存多个实体
     * 
     * @param List<t> 实体
     * @return 返回保存的实体
     */
    @Transactional(readOnly = false)
    public List<T> save(List<T> tlist) {
        tlist.forEach(t -> {
            UserUtil.stamp(t);
        });
        return baseRepository.saveAll(tlist);
    }

    /**
     * 统计实体总数
     *
     * @return 实体总数
     */
    @Transactional(readOnly = true)
    public long count() {
        List<SearchFilter> sfList = new ArrayList<>();
        sfList.add(new SearchFilter(isActive, Operator.EQ, Boolean.TRUE));
        return baseRepository.count(PageUtils.bySearchFilter(sfList));
    }

    /**
     * 查询所有实体
     * 
     * @return 实体集合
     */
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    /**
     * 查询所有实体，根据排序方式和字段排序<br>
     * searchParams的参数key必须包含如：EQ_name=xxx<br>
     * 否则无法正确解析构造动态的jpa搜索条件
     * 
     * @param searchParams 搜索参数
     * @param direction    排序方式
     * @param sortType     排序字段
     * @return 实体集合
     */
    @Transactional(readOnly = true)
    public List<T> findAllByMapParams(Map<String, Object> searchParams, Direction direction, String... sortType) {
        return baseRepository.findAll(PageUtils.buildSpec(searchParams), Sort.by(direction, sortType));
    }

    /**
     * 获取分页
     * 
     * @param searchParams 搜索参数
     * @param pageNumber   页码
     * @param pageSize     分页大小
     * @param direction    排序方式
     * @param sortType     排序字段
     * @return 分页实体信息
     */
    @Transactional(readOnly = true)
    public Page<T> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, Direction direction,
            String... sortType) {
        return baseRepository.findAll(PageUtils.buildSpec(searchParams),
                PageRequest.of(pageNumber - 1, pageSize, Sort.by(direction, sortType)));
    }

    /**
     * 根据某个字段查询单个实体<br>
     * 该查询不会过滤实体字段isActive=false的数据<br>
     * 
     * @param param    实体的搜索字段，字段必须在实体中存在
     * @param operator 搜索查询的方式
     * @param object   搜索查询的值
     * @return 查询不到返回null
     */
    @Transactional(readOnly = true)
    public T findOneByParam(String param, Operator operator, Object object) {
        List<T> list = findALLByParam(param, operator, object);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 根据某个字段查询数量<br>
     * 该查询会过滤掉实体字段isActive=false的数据
     * 
     * @param param    实体的搜索字段，字段必须在实体中存在
     * @param operator 搜索查询的方式
     * @param object   搜索查询的值
     * @return 根据条件查询到数量
     */
    @Transactional(readOnly = true)
    public long countByParam(String param, Operator operator, Object object) {
        List<SearchFilter> sfList = new ArrayList<>();
        sfList.add(new SearchFilter(param, operator, object));
        sfList.add(new SearchFilter(isActive, Operator.EQ, Boolean.TRUE));
        return baseRepository.count(PageUtils.bySearchFilter(sfList));
    }

    /**
     * 根据查询条件获取所有
     * 
     * @param spec 构造的JPA搜索条件
     * @return 实体集合
     */
    @Transactional(readOnly = true)
    public List<T> findAllBySpec(Specification<T> spec) {
        return baseRepository.findAll(spec);
    }

    /**
     * 根据查询条件获取，返回Optional
     * 
     * @param spec 构造的JPA搜索条件
     * @return 返回实体的Optional信息
     */
    @Transactional(readOnly = true)
    public Optional<T> findOneBySpec(Specification<T> spec) {
        return baseRepository.findOne(spec);
    }

    /**
     * 根据查询条件获取数量
     * 
     * @param spec 构造的JPA搜索条件
     * @return 查询的数量
     */
    @Transactional(readOnly = true)
    public long count(Specification<T> spec) {
        return baseRepository.count(spec);
    }

    /**
     * 根据某个字段查询实体集合<br>
     * 该查询会过滤掉实体字段isActive=false的数据
     * 
     * @param param    实体的搜索字段，字段必须在实体中存在
     * @param operator 搜索的方式
     * @param object   搜索的值
     * @return 实体集合
     */
    @Transactional(readOnly = true)
    public List<T> findALLByParam(String param, Operator operator, Object object) {
        List<SearchFilter> sfList = new ArrayList<>();
        sfList.add(new SearchFilter(param, operator, object));
        sfList.add(new SearchFilter(isActive, Operator.EQ, Boolean.TRUE));
        return baseRepository.findAll(PageUtils.bySearchFilter(sfList));
    }

    /**
     * 克隆对象属性值<br>
     * 来源和接收实体都不能为空
     * 
     * @param source      属性来源实体
     * @param destination 属性接收实体
     */
    @Transactional(readOnly = true)
    public void mapper(Object source, Object destination) {
        beanMapper.map(source, destination);
    }

    /**
     * 将一个实体转换成另一个实体<br>
     * 通常情况下是将map转成Java Bean
     * 
     * @param <S>
     * @param source 来源实体
     * @param cls    目标转换的class
     * @return
     */
    public <S> S conver(Object source, Class<S> cls) {
        return objectMapper.convertValue(source, cls);
    }

    /**
     * 获取集合的子列表，第二个参数是获取的大小，从0开始
     * 
     * @param list  数据集合
     * @param limit 子列表大小
     * @return 实体集合
     */
    @Transactional(readOnly = true)
    public List<T> getLimit(List<T> list, Integer limit) {
        return list.size() > limit ? list.subList(0, limit) : list;
    }

}