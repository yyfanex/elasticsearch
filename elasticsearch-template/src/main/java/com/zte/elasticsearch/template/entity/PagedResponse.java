package com.zte.elasticsearch.template.entity;

import java.io.Serializable;
import java.util.List;

public class PagedResponse<T> implements Serializable {
    private List<T> bo = null;

    private Integer count;

    private String scrollId;

    public List<T> getBo() {
        return bo;
    }

    public void setBo(List<T> bo) {
        this.bo = bo;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public static <T> PagedResponse success(List<T> list, Integer count, String scrollId) {
        PagedResponse response = new PagedResponse();
        response.setCount(count);
        response.setBo(list);
        response.setScrollId(scrollId);
        return response;
    }
}
