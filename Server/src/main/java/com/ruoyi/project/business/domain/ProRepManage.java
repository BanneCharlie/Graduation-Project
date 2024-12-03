package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

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
@TableName("business_product_report_manage")
// 产品 检验报告
public class ProRepManage {

    @TableId(value = "row_id",type = IdType.UUID)
    private String rowId;
    private String entrustUnit;
    private String useUnit;
    private String makeUnit;
    private String productModel;
    private String productName;
    private String productNumber;
    private Date dateOfManufacture;
    private String productPlace;

    private String dutyCheckUserId;         // 责任检验员 id
    private String dutyCheckUserName;       // 责任检验员 name
    private String checkUserName; // 责任员 name
    private String checkUserId;   //  责任员 1 2 3 id
    private String liaisons;
    private String phone;

    private String fileResultIds;
    private String attachmentResultIds;

    private String reportNumber;
    private String reportTemplateName;
    private String reportTemplateId;
    private String contractNumber;

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
    private String businessKey; //


    private String actName;
    private String actId;
    private String deletes;
    private String isRead;
}
