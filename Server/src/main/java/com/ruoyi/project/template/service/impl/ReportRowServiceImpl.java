package com.ruoyi.project.template.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.template.domain.ReportRow;
import com.ruoyi.project.template.mapper.ReportRowMapper;
import com.ruoyi.project.template.service.ReportRowService;
import com.ruoyi.project.template.vo.RowNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author niminui
 * @date 2020/11/24 12:55
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReportRowServiceImpl extends BaseServiceImpl<ReportRowMapper, ReportRow> implements ReportRowService {

    @Resource
    private ReportRowMapper reportRowMapper;

    /**
     * 获取报告的整个树形结构
     * @param row 查询条件
     * @return
     */
    @Override
    public List<RowNode> getRowTree(ReportRow row) {
        return getRowTree(getTopRow(row) , row);
    }

    /**
     * 获取最顶层的row，并且根据查询条件row查询
     * @param row 查询条件
     * @return
     */
    @Override
    public List<ReportRow> getTopRow(ReportRow row) {
        QueryWrapper<ReportRow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ReportRow::getLevelRow, "1")
                .orderByAsc(ReportRow::getOrderNum)
                .orderByAsc(ReportRow::getCreateTime);
        return reportRowMapper.selectList(queryWrapper);
    }

    /**
     * 根据参数 rows，查询以它为最顶层row的树形结构
     * @param rows 此处作为最顶层的树形结构
     * @return
     */
    @Override
    public List<RowNode> getRowTree(List<ReportRow> rows , ReportRow row) {
        List<RowNode> nodes = setNodeByRows(rows);
        List<RowNode> retNode = new ArrayList<>();
        String rowTitle = row.getReportTitle();
        for (RowNode node : nodes) {
            QueryWrapper<ReportRow> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(ReportRow::getParentId, node.getReportRowId())
                    .orderByAsc(ReportRow::getOrderNum)
                    .orderByAsc(ReportRow::getCreateTime);
            List<ReportRow> reportRows = reportRowMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(reportRows)){
                node.setChildren(getRowTree(reportRows , row));
            }
            // 如果有需要进行筛选标题
            if (StringUtils.isNotEmpty(rowTitle)){
                // 过滤掉当前层级中查询条件
                if (!CollectionUtils.isEmpty(node.getChildren()) || node.getReportTitle().contains(rowTitle)){
                    retNode.add(node);
                }
            }else {
                retNode.add(node);
            }

        }
        return retNode;
    }

    /**
     * 将 ReportRow 列表转换为树形结构需要的 RowNode
     * @param rows 需要转换的 ReportRowList
     * @return
     */
    private List<RowNode> setNodeByRows(List<ReportRow> rows) {
        List<RowNode> nodes = new ArrayList<>();
        for (ReportRow row : rows) {
            RowNode node = new RowNode();
            BeanUtils.copyBeanProp(node, row);
            nodes.add(node);
        }
        return nodes;
    }

    /**
     * 根据 ID 数组删除数据
     * @param rowIds 要删除的ids
     * @return
     */
    @Override
    public boolean deleteReportByIds(String[] rowIds) {
        boolean flag = true;
        for (String id : rowIds) {
            QueryWrapper<ReportRow> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(ReportRow::getParentId, id);
            List<ReportRow> rows = reportRowMapper.selectList(queryWrapper);
            ReportRow one = reportRowMapper.selectById(id);
            if (rows != null && rows.size() != 0) {
                flag = false;
                break;
            }
            reportRowMapper.deleteById(id);
        }
        return flag;
    }

    @Override
    public int getLastOrderByParentId(String parentId) {
        int order = 0;
        QueryWrapper<ReportRow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ReportRow::getParentId, parentId)
                .orderByDesc(ReportRow::getOrderNum);
        List<ReportRow> rows = reportRowMapper.selectList(queryWrapper);
        if (rows != null && rows.size() != 0) {
            order = rows.get(0).getOrderNum();
        }
        return order;
    }

    /**
     * 根据已有的报告标题行查询该标题行最底层的所有的数据
     * @param reportRow 报告标题
     * @return
     */
    @Override
    public List<ReportRow> getLastRowList(ReportRow reportRow) {
        List<ReportRow> list = new ArrayList<>();
        List<ReportRow> rows = getListByParentId(reportRow.getReportRowId());
        for (ReportRow row : rows) {
            List<ReportRow> inRows = getListByParentId(row.getReportRowId());
            if (inRows != null && inRows.size() != 0) {
                list.addAll(getLastRowList(row));
            } else {
                list.add(row);
            }
        }
        return list;
    }

    private List<ReportRow> getListByParentId(String parentId) {
        QueryWrapper<ReportRow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ReportRow::getParentId, parentId);
        return reportRowMapper.selectList(queryWrapper);
    }
}
