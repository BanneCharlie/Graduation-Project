package com.ruoyi.project.template.service.impl;
;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.template.domain.ReportRow;
import com.ruoyi.project.template.domain.ReportTemplate;
import com.ruoyi.project.template.mapper.ReportTemplateMapper;
import com.ruoyi.project.template.service.ReportRowService;
import com.ruoyi.project.template.service.ReportTemplateService;
import com.ruoyi.project.template.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author niminui
 * @date 2020/11/20 15:54
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReportTemplateServiceImpl extends BaseServiceImpl<ReportTemplateMapper, ReportTemplate> implements ReportTemplateService {

    @Resource
    private ReportTemplateMapper reportTemplateMapper;

    @Resource
    private ReportRowService reportRowService;

    /**
     * 查询报告模板列表
     * @param report 参数
     * @return
     */
    @Override
    public List<ReportTemplate> getList(ReportTemplate report) {
        QueryWrapper<ReportTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotEmpty(report.getTemplateName()), ReportTemplate::getTemplateName, report.getTemplateName())
                .eq(StringUtils.isNotEmpty(report.getTemplateContent()), ReportTemplate::getTemplateContent, report.getTemplateContent())
                .orderByAsc(ReportTemplate::getOrderNum);
        return reportTemplateMapper.selectList(queryWrapper);
    }

    /**
     * 根据ID数组删除报告模板
     * @param rowIds id数组
     * @return
     */
    @Override
    public int deleteReportByIds(String[] rowIds) {
        int res = 1;
        try {
            for (String id : rowIds) {
                reportTemplateMapper.deleteById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }

    /**
     * 根据templateId查询ReportRow（表格标题行），并且将表格标题行数据接续为html文本
     * @param templateId 模板ID
     * @return
     */
    @Override
    public ReportTemplate templateResolveByRows(String templateId) {
        ReportTemplate template = reportTemplateMapper.selectById(templateId);
        QueryWrapper<ReportRow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ReportRow::getTemplateId, templateId)
                .eq(ReportRow::getLevelRow, "1");
        String html = getTableInHtml(reportRowService.list(queryWrapper));
        template.setTemplateResolveByRows(html);
        return template;
    }

    private String getTableInHtml(List<ReportRow> reportRows) {
        List<ReportRow> lastRowList = new ArrayList<>();
        StringBuilder html = new StringBuilder();
        html.append(rowsHtmlInitSimple());
        for (ReportRow row : reportRows) {
            lastRowList.addAll(reportRowService.getLastRowList(row));
        }
        html.append(rowHtmlMiddleSimple(lastRowList));
        html.append(rowsHtmlEndSimple());
        return html.toString();
    }

    /**
     * 填充html表格的头部信息
     * @return
     */
    @SuppressWarnings("all")
    private String rowsHtmlInit() {
        StringBuilder html = new StringBuilder();
        html.append("<table cellspacing=\"0\" cellpadding=\"0\" width=\"612\">")
                .append("<thead>")
                .append("<tr style=\";height:30px\" class=\"firstRow\">")
                .append("<td width=\"42\" style=\"border-width: 3px 1px 1px 3px; border-color: windowtext; border-style: solid; padding: 0px 7px;\" height=\"30\">")
                .append("<p style=\"text-align:center\">")
                .append("<strong>")
                .append("<span style=\"font-size:16px;font-family:宋体\">")
                .append("序号")
                .append("</span>")
                .append("</strong>")
                .append("</p>")
                .append("</td>")
                .append("<td width=\"389\" colspan=\"3\" style=\"border-top: 3px solid windowtext; border-left: none; border-bottom: 1px solid windowtext; border-right: 1px solid windowtext; padding: 0px 7px;\" height=\"30\">")
                .append("<p style=\"text-align:center\">")
                .append("<strong>")
                .append("<span style=\"font-size:16px;font-family:宋体\">")
                .append("检验项目及其内容")
                .append("</span>")
                .append("</strong>")
                .append("</p>")
                .append("</td>")
                .append("<td width=\"87\" style=\"border-top: 3px solid windowtext; border-left: none; border-bottom: 1px solid windowtext; border-right: 1px solid windowtext; padding: 0px 7px;\" height=\"30\">")
                .append("<p style=\"text-align:center\">")
                .append("<strong>")
                .append("<span style=\"font-size:16px;font-family:宋体\">")
                .append("检验结果")
                .append("</span>")
                .append("</strong>")
                .append("</p>")
                .append("</td>")
                .append("<td width=\"94\" style=\"border-top: 3px solid windowtext; border-left: none; border-bottom: 1px solid windowtext; border-right: 3px solid windowtext; padding: 0px 7px;\" height=\"30\">")
                .append("<p style=\"text-align:center\">")
                .append("<strong>")
                .append("<span style=\"font-size:16px;font-family:宋体\">")
                .append("检验结论")
                .append("</span>")
                .append("</strong>")
                .append("</p>")
                .append("</td>")
                .append("</tr>")
                .append("</thead>");
        return html.toString();
    }

    /**
     * 填充html表格的尾部信息
     * @return
     */
    private String rowsHtmlEnd() {
        return "</table>" + "<p>" + "<br/>" + "</p>";
    }

    /**
     * 填充html表格的尾部信息 （无单元格合并）
     * @return
     */
    @SuppressWarnings("all")
    private String rowsHtmlInitSimple() {
        StringBuilder html = new StringBuilder();
        html.append("<table width=\"616\" style=\"border: 3px #000 solid;\" cellpadding=\"0\" cellspacing=\"0\">")
                .append("<tbody>")
                .append("<tr class=\"firstRow\">")
                .append("<td width=\"42px\" style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid;font-weight: bold;font-size:16px;font-family:宋体;height: 30px;text-align: center;\">")
                .append("序号")
                .append("</td>")
                .append("<td width=\"389px\" style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid;font-weight: bold;font-size:16px;font-family:宋体;text-align: center;\">")
                .append("检验项目及其内容")
                .append("</td>")
                .append("<td width=\"87px\" style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid; font-size:16px;font-family:宋体;font-weight: bold;text-align: center;\">")
                .append("检验结果")
                .append("</td>")
                .append("<td width=\"94px\" style=\"border-bottom: 1px #000 solid;font-weight: bold; font-size:16px;font-family:宋体;;\">")
                .append("检验结论")
                .append("</td>")
                .append("</tr>");
        return html.toString();
    }

    /**
     * 填充html表格的中部信息 （无单元格合并）
     * @return
     */
    private String rowHtmlMiddleSimple(List<ReportRow> lastRowList) {
        StringBuilder middle = new StringBuilder();
        for (int i = 0; i < lastRowList.size(); i++) {
            ReportRow row = lastRowList.get(i);
            middle.append("<tr>")
                    .append("<td style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid; font-size:16px;font-family:宋体; height: 30px;    text-align: center;\">")
                    .append(i + 1)
                    .append("</td>")
                    .append("<td style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid; font-size:16px;font-family:宋体;\">")
                    .append(row.getReportTitle())
                    .append("</td>")
                    .append("<td style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid; font-size:16px;font-family:宋体;\">")
                    .append(row.getReportResult())
                    .append("</td>")
                    .append("<td style=\"border-bottom: 1px #000 solid; font-size:16px;font-family:宋体;\">")
                    .append(row.getReportConclusion())
                    .append("</td>")
                    .append("</tr>");
        }
        return middle.toString();
    }

    /**
     * 填充html表格的尾部信息 （无单元格合并）
     * @return
     */
    private String rowsHtmlEndSimple() {
        return "</tbody></table>";
    }
}
