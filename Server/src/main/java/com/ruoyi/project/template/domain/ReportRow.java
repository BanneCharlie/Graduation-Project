package com.ruoyi.project.template.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.ruoyi.project.template.commons.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author niminui
 * @date 2020/11/24 12:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("REPORT_ROW")
public class ReportRow extends AbstractEntity {
    @TableId(value = "REPORT_ROW_ID", type = IdType.UUID)
    private String reportRowId;
    private String reportTitle;
    private String levelRow;
    private String levelRowName;
    private String templateId;
    private String templateName;
    private String parentId;
    private String parentName;
    private String reportResult;
    private String reportConclusion;
    private Integer orderNum;
    private Integer childCount;
    private String reportHtmlContent;
}
