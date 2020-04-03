package com.github.barry.akali.base.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * 页面搜索及JPA的搜索参数构造工具类
 * 
 * @author barry
 *
 */
public class RequestSearchUtils {

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     * 
     * 返回的结果的Parameter名已去除前缀.
     */
    public static Map<String, Object> getParamStartWith(ServletRequest request, String prefix) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        prefix = Optional.ofNullable(prefix).orElseGet(() -> "");
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, StringEscapeUtils.escapeHtml4(values[0]));
                }
            }
        }
        return params;
    }

    /**
     * 组合Parameters生成Query String的Parameter部分, 并在paramter name上加上prefix.
     * 
     * @see #getParametersStartingWith
     */
    public static String enParamStrWithPrefix(Map<String, Object> params, String prefix) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        prefix = Optional.ofNullable(prefix).orElseGet(() -> "");
        StringBuilder queryStringBuilder = new StringBuilder();
        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            queryStringBuilder.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
            if (it.hasNext()) {
                queryStringBuilder.append('&');
            }
        }
        return queryStringBuilder.toString();
    }

    /**
     * 构造一般搜索标准查询
     * 
     * @param searchParams
     * @return
     */
    public static <T> Specification<T> buildSpec(Map<String, Object> searchParams) {
        return bySearchFilter(SearchFilter.parse(searchParams));
    }

    /**
     * 实现复杂对象查询，实现toPredicate方法，用JPA去构造Specification对象查询；
     * 
     * @author qsr
     *
     */
    public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters) {
        return new Specification<T>() {
            /**
             * 
             */
            private static final long serialVersionUID = 7054380594285260439L;

            // Root 查询中的条件表达式
            // CriteriaQuery 条件查询设计器
            // CriteriaBuilder 条件查询构造器
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!CollectionUtils.isEmpty(filters)) {
                    // 保存查询条件集
                    filters.forEach(f -> {
                        // 使用原生的java API进行分割，防止过度依赖第三方包
                        String[] names = f.fieldName.split("\\.");
                        Path expression = root.get(names[0]);
                        if (names.length > 1) {
                            expression = getSubExpression(names, expression, root);
                        }
                        switch (f.operator) {
                        case EQ:
                            // 等于查询构造
                            predicates.add(builder.equal(expression, f.value));
                            break;
                        case NE:
                            // 不等于查询构造
                            predicates.add(builder.notEqual(expression, f.value));
                            break;
                        case LIKE:
                            // 模糊查询构造
                            predicates.add(builder.like(expression, "%" + f.value + "%"));
                            break;
                        case GT:
                            // 大于查询构造
                            predicates.add(builder.greaterThan(expression, (Comparable) f.value));
                            break;
                        case LT:
                            // 小于查询构造
                            predicates.add(builder.lessThan(expression, (Comparable) f.value));
                            break;
                        case GTE:
                            // 大于等于查询
                            predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) f.value));
                            break;
                        case LTE:
                            // 小于等于查询构造
                            predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) f.value));
                            break;
                        case IN:
                            // 使用IN的查询构造，需要以数组为Value;
                            Object[] queryObj = null;
                            if (f.value instanceof Collection) {
                                queryObj = (Object[]) ((Collection) f.value).toArray();
                            } else {
                                queryObj = (Object[]) f.value;
                            }
                            predicates.add(builder.isTrue(expression.in(queryObj)));
                            break;
                        case BETWEEN:
                            if (f.value instanceof List) {
                                List c = (List) f.value;
                                Object param1 = c.get(0);
                                Object param2 = c.get(1);
                                if (param1 instanceof Number) {
                                    predicates.add(builder.between(expression, ((Number) param1).longValue(),
                                            ((Number) param2).longValue()));
                                } else if (param1 instanceof Date) {
                                    predicates.add(builder.between(expression, (Date) param1, (Date) param2));
                                } else if (param1 instanceof LocalDateTime) {
                                    predicates.add(builder.between(expression, (LocalDateTime) param1,
                                            (LocalDateTime) param2));
                                } else {
                                    predicates.add(builder.between(expression, param1.toString(), param2.toString()));
                                }
                            }
                            break;
                        case ISNULL:
                            predicates.add(builder.isNull(expression));
                            break;
                        case ISNOTNULL:
                            predicates.add(builder.isNotNull(expression));
                            break;
                        default:
                            break;
                        }
                    });
                }
                // 如果predicates集合大于0，将所有条件用 and 联合起来
                return predicates.isEmpty() ? builder.conjunction()
                        : builder.and(predicates.toArray(new Predicate[predicates.size()]));
            }

            /**
             * 无限连接查询构造<br>
             * 支持属性为List、Set、Map的集合数据
             * 
             * @param names      对应实体里面的字段
             * @param expression
             * @param root
             * @return
             */
            @SuppressWarnings("rawtypes")
            private Path getSubExpression(String[] names, Path expression, Root<T> root) {
                Join join = null;
                for (int i = 1; i < names.length; i++) {
                    // 属性为List集合
                    if (Objects.equals(List.class, expression.getJavaType())) {
                        if (join == null) {
                            join = root.joinList(names[i - 1]);
                        } else {
                            join = join.joinList(names[i - 1]);
                        }
                        expression = join.get(names[i]);
                    } else if (Objects.equals(Set.class, expression.getJavaType())) {
                        // 属性为Set集合
                        if (join == null) {
                            join = root.joinSet(names[i - 1]);
                        } else {
                            join = join.joinSet(names[i - 1]);
                        }
                        expression = join.get(names[i]);
                    } else if (Objects.equals(Map.class, expression.getJavaType())) {
                        // 属性为Map集合
                        if (join == null) {
                            join = root.joinMap(names[i - 1]);
                        } else {
                            join = join.joinMap(names[i - 1]);
                        }
                        expression = join.get(names[i]);
                    } else {
                        // 非属性集合
                        expression = expression.get(names[i]);
                    }
                }
                return expression;
            }
        };
    }

}