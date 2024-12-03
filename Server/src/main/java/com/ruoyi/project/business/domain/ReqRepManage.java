package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_28 _ 11:26__星期五
 * @package: com.ruoyi.project.business.domain
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("business_request_report_manage")
// 委托检验报告管理
public class ReqRepManage {

    @TableId(value = "row_id",type = IdType.UUID)
    private String rowId;
    private String entrustUnit; //委托单位
    private String useUnit; //单位类型
    private String makeUnit; //制造单位
    private String constructUnit; //施工单位
    private String repairUnit; //维保单位
    private String deviceRowId; //设备编号
    private String deviceModel; //设备型号
    private String deviceCategory; //设备种类
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
    private String isRead;

    /** 请求参数 */
    @TableField(exist = false)
    private Map<String, Object> params;


    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }
}
