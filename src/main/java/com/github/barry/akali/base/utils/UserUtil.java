package com.github.barry.akali.base.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 一些操作信息的帮助工具类
 * 
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

}