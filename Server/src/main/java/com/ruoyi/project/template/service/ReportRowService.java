package com.ruoyi.project.template.service;


import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;
import com.ruoyi.project.template.domain.ReportRow;
import com.ruoyi.project.template.vo.RowNode;

import java.util.List;

/**
 * @author niminui
 * @date 2020/11/24 12:54
 */
public interface ReportRowService extends IBaseService<ReportRow> {
    /**
     * 获取报告的整个树形结构
     * @param row 查询条件
     * @return
     */
    public List<RowNode> getRowTree(ReportRow row);

    /**
     * 获取最顶层的row，并且根据查询条件row查询
     * @param row 查询条件
     * @return
     */
    public List<ReportRow> getTopRow(ReportRow row);

    /**
     * 根据参数 rows，查询以它为最顶层row的树形结构
     * @param rows 此处作为最顶层的树形结构
     * @return
     */
    public List<RowNode> getRowTree(List<ReportRow> rows , ReportRow row);

    /**
     * 根据 ID 数组删除数据
     * @param rowIds 要删除的ids
     * @return
     */
    public boolean deleteReportByIds(String[] rowIds);

    /**
     * 获取所有以 parentId 为父ID的数据信息
     * @param parentId
     * @return
     */
    int getLastOrderByParentId(String parentId);

    /**
     * 根据已有的报告标题行查询该标题行最底层的所有的数据
     * @param reportRow 报告标题
     * @return
     */
    public List<ReportRow> getLastRowList(ReportRow reportRow);
}
