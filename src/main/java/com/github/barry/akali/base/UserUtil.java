package com.github.barry.akali.base;

import lombok.extern.slf4j.Slf4j;


/**
 * 一些操作信息的帮助工具类
 * @author barry
 *
 */
@Slf4j
public class UserUtil {

    /**
     * 获取当前操作用户
     * 
     * @return
     */
    public static String getCurrentUser() {
        String principal = "";
        try {
//			 principal=SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            log.error("获取spring security的用户失败，原因={}", e.getMessage());
        }
        return principal;
    }

    /**
     * 记录戳（印记）. 用于写数据操作记录的时间和操作人. stamp 记录戳（印记） BeanUtils.get
     * 初次创建，创建人、创建时间默认值逻辑放在BaseEntity
     * 
     * @param bean
     */
    public static void stamp(Object bean) {
        try {
//            BeanUtils.setProperty(bean, "modified", new Date());
        } catch (Exception e) {
            log.error("记录更新时间戳失败，原因={}", e);
        }
    }

}