package com.ruoyi.project.template.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author niminui
 * @date 2020/11/8 13:50
 */
@Data
@NoArgsConstructor
@TableName("REPORT_TEMPLATE")
public class ReportTemplate {

    @TableId(value = "TEMPLATE_ID", type = IdType.UUID)
    private String templateId;

    private String templateName;

    private String templateType;

    private String status;

    @TableField(exist = false)
    private String templateResolveByRows;

    private String templateGenericContent;

    private String templateContent;

    private String dataSourceId;

    private String dataSourceName;

    private Integer orderNum;

    private String createUserId;

    private String createUserName;

    private String updateUserId;

    private String updateUserName;

    private String isResolveQrcodePath;           // 解析过后的二维码地址

    private String notResolveQrcodePath;          // 未经过解析的二维码地址

    private String templateCategory;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date updateTime;
}
