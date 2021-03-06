package com.github.barry.akali.base.utils;

/**
 * 分页参数类型
 * 
 * @author barry
 *
 */
public class PageReqDto {

    /** 当前分页页码 */
    private int number;

    /** 每页的数据大小 */
    private int size;

    /** 排序类型,默认按照主键id排序 */
    private String sortType = IConstants.DEFAULT_SORT_TYPE_VAL;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public PageReqDto(int number, int size, String sortType) {
        super();
        this.number = number;
        this.size = size;
        this.sortType = sortType;
    }

}
