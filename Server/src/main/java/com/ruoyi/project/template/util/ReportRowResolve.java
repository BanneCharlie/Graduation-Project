package com.ruoyi.project.template.util;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.project.template.domain.ReportRow;
import com.ruoyi.project.template.service.ReportRowService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niminui
 * @date 2020/11/25 10:02
 */
@Component
public class ReportRowResolve {

    @Resource
    private ReportRowService reportRowService;

    public String resolveToHtmlTemp() {
        int count = 1;
        QueryWrapper<ReportRow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("LEVEL_ROW","3");
        queryWrapper.orderByAsc("LEVEL_ROW");
        queryWrapper.orderByAsc("ORDER_NUM");
//        queryWrapper.lambda()
//                .eq(ReportRow::getLevelRow, "3")
//                .orderByAsc(ReportRow::getLevelRow)
//                .orderByAsc(ReportRow::getOrderNum);
        List<ReportRow> list = reportRowService.list(queryWrapper);
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder("<table width=\"616\" style=\"border: 3px #000 solid;\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "    <tbody>\n" +
                "        <tr class=\"firstRow\">\n" +
                "            <td width=\"42px\" style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid;font-weight: bold;font-size:16px;font-family:宋体;height: 30px;text-align: center;\">\n" +
                "                序号\n" +
                "            </td>\n" +
                "            <td width=\"389px\" style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid;font-weight: bold;font-size:16px;font-family:宋体;text-align: center;\">\n" +
                "                检验项目及其内容\n" +
                "            </td>\n" +
                "            <td width=\"87px\" style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid; font-size:16px;font-family:宋体;font-weight: bold;text-align: center;\">\n" +
                "                检验结果\n" +
                "            </td>\n" +
                "            <td width=\"94px\" style=\"border-bottom: 1px #000 solid;font-weight: bold; font-size:16px;font-family:宋体;;\">\n" +
                "                检验结论\n" +
                "            </td>\n" +
                "        </tr>");
        for (int i = 0; i < list.size(); i++) {
            ReportRow temp = list.get(i);
            sb.append("<tr>\n" +
                    "            <td style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid; font-size:16px;font-family:宋体; height: 30px;    text-align: center;\">")
                    .append(i + 1)
                    .append("</td>\n" +
                            "            <td style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid; font-size:16px;font-family:宋体;\">")
                    .append(temp.getReportTitle())
                    .append("</td>\n" +
                            "            <td style=\"border-right: 1px #000 solid;border-bottom: 1px #000 solid; font-size:16px;font-family:宋体;\">")
                    .append(temp.getReportResult())
                    .append("</td>\n" +
                            "            <td style=\"border-bottom: 1px #000 solid; font-size:16px;font-family:宋体;\">")
                    .append(temp.getReportConclusion())
                    .append("</td>\n" +
                            "        </tr>");
        }
        sb.append(" </tbody>\n" +
                "</table>");
        return sb.toString();

    }



    public String resolveToHtml() {
        StringBuilder sb = new StringBuilder("<table cellspacing=\"0\" cellpadding=\"0\" width=\"616\">");
        QueryWrapper<ReportRow> root = new QueryWrapper();
        root.lambda()
                .eq(ReportRow::getReportRowId, "ROOT");
        ReportRow rootRow = reportRowService.getOne(root);
        sb.append(rootRow.getReportHtmlContent());
        sb.append("<tbody>");
        String oldLevel1Id = "";
        String oldLevel2Id = "";
        int count = 1;
        QueryWrapper<ReportRow> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ReportRow::getChildCount, "0")
                .orderByDesc(ReportRow::getLevelRow)
                .orderByAsc(ReportRow::getOrderNum);
        List<ReportRow> noChild = reportRowService.list(queryWrapper);

        for (int i = 0; i < noChild.size(); i++) {
            ReportRow no = noChild.get(i);
            sb.append("<tr style=\";height:30px\">");

            if (!no.getParentId().equals(oldLevel2Id)) {
                    oldLevel2Id = no.getParentId();
                QueryWrapper<ReportRow> queryWrapper1 = new QueryWrapper();
                queryWrapper1.lambda()
                            .eq(ReportRow::getReportRowId, no.getParentId())
                            .orderByAsc(ReportRow::getOrderNum);
                    ReportRow temp2 = reportRowService.getOne(queryWrapper1);
                    if (temp2.getChildCount() > 0) {
                        sb.append("<td width=\"50\" rowspan=\"")
                                .append(temp2.getChildCount())
                                .append("\" style=\"border-top: none; border-left: 3px solid windowtext; border-bottom: 1px solid windowtext; border-right: 1px solid windowtext; padding: 0px 7px;\" height=\"30\">\n" +
                                        "                <p style=\"margin-left:24px;text-align:center\">\n" +
                                        "                    <span style=\"font-size:16px;font-family:宋体\"><span style=\"font:9px &#39;Times New Roman&#39;\">&nbsp;&nbsp;&nbsp; </span>");
                    } else {
                        sb.append("<td width=\"50\" style=\"border-top: none; border-left: 3px solid windowtext; border-bottom: 1px solid windowtext; border-right: 1px solid windowtext; padding: 0px 7px;\" height=\"30\">\n" +
                                "                <p style=\"margin-left:24px;text-align:center\">\n" +
                                "                    <span style=\"font-size:16px;font-family:宋体;color:black\"><span style=\"font:9px &#39;Times New Roman&#39;\">&nbsp;&nbsp;&nbsp; </span>");
                    }
                    sb.append(count++)
                            .append(" </span><span style=\"font-size:16px;font-family:宋体;color:black\">&nbsp;</span>\n" +
                                    "                </p>\n" +
                                    "            </td>");
                    ReportRow temp1;
                    if (!temp2.getLevelRow().equals("1")) {
                        QueryWrapper<ReportRow> queryWrapper2 = new QueryWrapper();
                        queryWrapper2.lambda()
                                .eq(ReportRow::getReportRowId, temp2.getParentId())
                                .orderByAsc(ReportRow::getOrderNum);
                        temp1 = reportRowService.getOne(queryWrapper2);
                    } else {
                        temp1 = temp2;
                    }

                    if (!temp1.getReportRowId().equals(oldLevel1Id)) {
                        oldLevel1Id = temp1.getReportRowId();
                        if (temp1.getChildCount() > 0) {
                            sb.append("<td width=\"51\" rowspan=\"").append(temp1.getChildCount());
                        } else {
                            sb.append("<td width=\"51\" ");
                        }
                        sb.append("\" style=\"border-top: none; border-left: none; border-bottom: 1px solid windowtext; border-right: 1px solid windowtext; padding: 0px 7px;\" height=\"30\">\n" +
                                "                <p style=\"text-align:center\">\n" +
                                "                    <span style=\"font-size:16px;font-family:&#39;微软雅黑&#39;,sans-serif\">")
                                .append(temp1.getReportTitle())
                                .append("</span>\n" +
                                        "                </p>\n" +
                                        "            </td>");
                    }
                }


            sb.append("<td width=\"260\" style=\"border-top: none; border-left: none; border-bottom: 1px solid windowtext; border-right: 1px solid windowtext; padding: 0px 7px;\" height=\"60\">\n" +
                    "                <p style=\"margin-left:24px;text-align:left\">\n" +
                    "                    <span style=\"font-size:16px;font-family:   宋体\">")
                    .append(no.getReportTitle())
                    .append("<td width=\"87\" style=\"border-top: none; border-left: none; border-bottom: 1px solid windowtext; border-right: 1px solid windowtext; padding: 0px 7px;\" height=\"30\">")
                    .append(no.getReportResult())
                    .append("</td>\n" +
                            "            <td width=\"84\" rowspan=\"3\" style=\"border-top: none; border-left: none; border-bottom: 1px solid windowtext; border-right: 3px solid windowtext; padding: 0px 7px;\" height=\"30\">")
                    .append(no.getReportConclusion())
                    .append("</td>");

            sb.append("</tr>");
        }
        sb.append("    </tbody>\n" +
                "</table>\n" +
                "<p>\n" +
                "    <br/>\n" +
                "</p>");
        return sb.toString();
    }

}
