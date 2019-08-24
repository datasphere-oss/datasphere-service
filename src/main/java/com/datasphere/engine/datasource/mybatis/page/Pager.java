package com.datasphere.engine.datasource.mybatis.page;

import java.util.*;

public class Pager<T>
{
    Integer totalRecords;
    Integer pageNumber;
    Integer pageSize;
    List<T> list;
    
    public Pager() {
        this.totalRecords = 0;
        this.pageNumber = 1;
        this.pageSize = 10;
    }
    
    public Integer getTotalRecords() {
        return this.totalRecords;
    }
    
    public void setTotalRecords(final Integer count) {
        this.totalRecords = count;
    }
    
    public Integer getPageNumber() {
        return this.pageNumber;
    }
    
    public void setPageNumber(Integer pageNumber) {
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        this.pageNumber = pageNumber;
    }
    
    public Integer getPageSize() {
        return this.pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        if (pageSize < 1) {
            pageSize = 1;
        }
        this.pageSize = pageSize;
    }
    
    public List<T> getList() {
        return this.list;
    }
    
    public void setList(final List<T> list) {
        this.list = list;
    }
}
