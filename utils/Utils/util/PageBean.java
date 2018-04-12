package com.zhph.util;

import java.io.Serializable;

/**
 * Created by zhouliang on 2017/11/23.
 */
public class PageBean implements Serializable{

    private Integer limit = 10;// 每页显示条数
    private Integer page = 1;// 当前页数
    private String sort="id";
    private String order="asc";// asc/desc
    private Integer rows = 10;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
