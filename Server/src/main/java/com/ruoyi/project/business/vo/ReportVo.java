package com.ruoyi.project.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-10 12:14 - 星期二
 * @package: com.ruoyi.project.business.vo
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportVo {

    private String deviceId;
    private String templateCategory;
    private String contractId;
    private String reportId;
    private String templateId;
    private String templateName;
    private String contractNumber;
    private String receivingUserName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String deviceNumber;
    // new Insert Field
    private String isPrint;
    private String isStartFlow;
    private String isReset;
    private String fileName;
    private String filePath;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fileUploadTime;
    private int filePages;
    private String fileRelativeName;
}
