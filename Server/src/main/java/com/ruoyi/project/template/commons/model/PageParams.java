package com.ruoyi.project.template.commons.model;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.ruoyi.project.template.commons.constants.ConstantsField;
import com.ruoyi.project.template.util.BeanConvertUtils;
import com.ruoyi.project.template.util.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * 分页参数
 *
 * @author liuyau
 * @date 2018/07/10
 */
public class PageParams extends Page implements Serializable {
    private static final long serialVersionUID = -1710273706052960025L;
    private int page = ConstantsField.DEFAULT_PAGE;
    private int limit = ConstantsField.DEFAULT_LIMIT;
    private String sort;
    private String order;
    private Map<String, Object> requestMap = Maps.newHashMap();
    /**
     * 排序
     */
    private String orderBy;

    public PageParams() {
        requestMap = Maps.newHashMap();
    }

    public PageParams(Map map) {
        if (map == null) {
            map = Maps.newHashMap();
        }
        this.page = Integer.parseInt(map.getOrDefault(ConstantsField.PAGE_KEY, ConstantsField.DEFAULT_PAGE).toString());
        this.limit = Integer.parseInt(map.getOrDefault(ConstantsField.PAGE_LIMIT_KEY, ConstantsField.DEFAULT_LIMIT).toString());
        this.sort = (String) map.getOrDefault(ConstantsField.PAGE_SORT_KEY, "");
        this.order = (String) map.getOrDefault(ConstantsField.PAGE_ORDER_KEY, "");
        super.setCurrent(page);
        super.setSize(limit);
        map.remove(ConstantsField.PAGE_KEY);
        map.remove(ConstantsField.PAGE_LIMIT_KEY);
        map.remove(ConstantsField.PAGE_SORT_KEY);
        map.remove(ConstantsField.PAGE_ORDER_KEY);
        requestMap.putAll(map);
    }

    public PageParams(int page, int limit) {
        this(page, limit, "", "");
    }

    public PageParams(int page, int limit, String sort, String order) {
        this.page = page;
        this.limit = limit;
        this.sort = sort;
        this.order = order;
    }

    public int getPage() {
        if (page <= ConstantsField.MIN_PAGE) {
            page = 1;
        }
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        if (limit > ConstantsField.MAX_LIMIT) {
            limit = ConstantsField.MAX_LIMIT;
        }
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
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

    public String getOrderBy() {
        if (StringUtils.isBlank(order)) {
            order = "asc";
        }
        if (StringUtils.isNotBlank(sort)) {
            this.setOrderBy(String.format("%s %s", StringUtils.camelToUnderline(sort), order));
        }
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public <T> T mapToObject(Class<T> t) {
        return BeanConvertUtils.mapToObject(this.requestMap, t);
    }

    public Map<String, Object> getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(Map<String, Object> requestMap) {
        this.requestMap = requestMap;
    }
}