package com.zhph.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Layui-grid
 * @param <T>
 */
public class Grid<T> implements Serializable {
    private Integer code = 0;
    private Long count = 1L;
    private List<T> data = new ArrayList<T>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
