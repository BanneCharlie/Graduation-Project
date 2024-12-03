package com.ruoyi.project.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author niminui
 * @date 2021/6/29 9:53
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ReqCheckVo {

    private String rowId;
    private String entrustUnit; //委托单位
    private String useUnit; //单位类型
    private String makeUnit; //制造单位
    private String constructUnit; //施工单位
    private String repairUnit; //维保单位
    private String deviceModel; //设备型号
    private String deviceName; //设备名称
    private String productNumber; //产品编号
    private Date dateOfManufacture; //出厂日期
    private String devicePlace; //设备使用地点
    private String dutyCheckUserId; // 责任检验员 id
    private String dutyCheckUserName; // 责任检验员 name
    private String checkUserName; // 检验员name
    private String checkUserId;   // 检验员 1 2 3 id
    private String liaisons; //联系人
    private String phone; //手机号

    private String fileResultIds;
    private String attachmentResultIds;

    private String reportNumber; //报告编号
    private String reportTemplateName; //检验报告模板name
    private String reportTemplateId; //检验报告模板id
    private String contractId; //合同rowId
    private String contractNumber; //合同编号
    private String money; //经费
    private String reportIssue; //报告书中问题描述
    private String reportIssueNumber; //报告书中问题描述数目

    private Date createTime;    // 创建时间
    private String createUserId;// 申请人id
    private String createUserName;//申请人name
    private String createDeptId; // 申请人部门id
    private String createDeptName;// 申请人name
    private Integer orderNum;
    private String status;

    private String title;
    private String ptid;        // 流程模板id
    private String piid;        // 流程实例 id
    private String paid;        // 流程环节id
    private String proceedingId; // 事项配置id
    private String businessKey; // 业务主键

    private String actName;
    private String actId;
    private String deletes;

    private String deviceNumber; //设备编号
    private Integer isRead; //是否已申请，0已申请，1未申请
    private Integer isHandle; //是否已处理(未处理报告)，0已处理，1未处理
    private Integer isReceiving; //是否已经领用，0为是，1为否



}
