package com.ruoyi.project.template.service;

import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;
import com.ruoyi.project.template.domain.DataSourceConfig;

import java.util.List;
import java.util.Map;

/**
 * @author niminui
 * @date 2020/11/20 15:53
 */
public interface DataSourceConfigService extends IBaseService<DataSourceConfig> {

    public List<DataSourceConfig> getList(DataSourceConfig dataSourceConfig);

    public int deleteConfigByIds(String[] rowIds);
    /**
     * --->     获取数据源中的表结构字段
     * @author xqh, 987072248@qq.com
     * @date 2021/7/5 11:30
     * @param
     */
    List<Map<String, String>> getReqRepFields() throws Exception;

}
