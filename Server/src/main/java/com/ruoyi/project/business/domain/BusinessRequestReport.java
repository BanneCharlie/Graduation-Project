package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-05 14:53 - 星期四
 * @package: com.ruoyi.project.business.domain
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("business_request_report")
public class BusinessRequestReport implements Serializable {
    @TableId(value = "row_id",type = IdType.UUID)
    private String rowId;
    private String businessContractReviewId;
    private String businessDeviceId;
    private String businessDeviceNumber;
    private String templateId;
    private String templateName;
    private String reportGenericContext;
    private String reportTemplateContext;
    private String isResolveQrcodePath;
    private String notResolveQrcodePath;
    private String templateCategory;
    private String createUserId;
    private String createUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-NN-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-NN-dd HH:mm:ss")
    private Date updateTime;
    private String isGenericReport;
    private String isStartFlow;
    private String isPrint;
    /** 是否已经解析过了 */
    private String isResolve;
    /** 是否可以重置当前报告内容 */
    private String isReset;
    private String fileName;
    private String fileRelativeName;
    private String filePath;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-NN-dd HH:mm:ss")
    private Date fileUploadTime;
    private int filePages;
}
