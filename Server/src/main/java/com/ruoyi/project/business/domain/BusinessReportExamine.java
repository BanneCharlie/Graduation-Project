package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @date: 2021-08-13 14:27 - 星期五
 * @package: com.ruoyi.project.business.domain
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("business_report_examine")
public class BusinessReportExamine {
    // 业务相关
    @TableId(value = "row_id" , type = IdType.UUID)
    private String rowId;
    private String reportId;
    /** 报告类别 ， DT - 或者取他。。。 */
    private String reportCategory;
    // 流程相关
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String createUserId;
    private String createUserName;
    private String createDeptId;
    private String createDeptName;
    private String ptid;
    private String piid;
    private String atid;
    private String actName;
    private String businessKey;
    private String deletes;

}
