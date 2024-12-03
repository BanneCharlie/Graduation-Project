package com.ruoyi.project.template.service;


import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;
import com.ruoyi.project.template.domain.ReportTemplate;

import java.util.List;

/**
 * @author niminui
 * @date 2020/11/20 15:54
 */
public interface ReportTemplateService extends IBaseService<ReportTemplate> {
    /**
     * 查询报告模板列表
     * @param report 参数
     * @return
     */
    public List<ReportTemplate> getList(ReportTemplate report);

    /**
     * 根据ID数组删除报告模板
     * @param rowIds id数组
     * @return
     */
    int deleteReportByIds(String[] rowIds);

    public ReportTemplate templateResolveByRows(String templateId);
}
