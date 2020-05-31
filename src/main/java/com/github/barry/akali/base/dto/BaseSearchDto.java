package com.github.barry.akali.base.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 搜索参数的基类
 * 
 * @author quansr
 *
 */
public abstract class BaseSearchDto {

    /**
     * 获取查询条件与值
     * 
     */
    public Map<String, Object> getSearchParams() {
        Map<String, Object> map = new HashMap<>();
        buildSearchParams(map);
        return map;
    }

    /**
     * 子类实现该方法，将查询的条件和值封装到map参数中<br>
     * key为查询条件，如：LIKE_name<br>
     * value为查询条件对应的值，如：123<br>
     * 则封装后的SQL语句为：where name like '%123%'
     * 
     * @param map 保持查询条件和值的集合
     */
    protected abstract void buildSearchParams(Map<String, Object> map);

    /**
     * 查询的值不为空才放入集合中<br>
     * 1.如果时字符串，字符串必须不为空<br>
     * 2.如果是List集合类型，集合必须不为空<br>
     * 3.其他情况，value != null<br>
     * 
     * @param key   查询的key，如：LIKE_name
     * @param value 对应的值，不为空，如：123
     * @param map
     */
    public void putNoNull(String key, Object value, Map<String, Object> map) {
        boolean isCanSearch = true;
        if (value instanceof String) {
            isCanSearch = StringUtils.hasText(value.toString());
        } else if (value instanceof Collection) {
            isCanSearch = !CollectionUtils.isEmpty((Collection<?>) value);
        } else {
            isCanSearch = (value != null);
        }
        if (isCanSearch) {
            map.put(key, value);
        }
    }

}
