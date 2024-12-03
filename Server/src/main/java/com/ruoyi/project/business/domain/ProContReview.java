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
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@TableName("business_product_contract_review")
// 产品 合同评审
public class ProContReview {
    @TableId(value = "row_id",type = IdType.UUID)
    private String rowId;
    private String entrustUnit;
    private String typeUnit;
    private String useUnit;
    private String addressUnit;
    private String productCategory;
    private String productName;
    private String productType;
    private String productCount;
    private String contractMoney;
    private String realMoney;
    private String paymentType;
    private String liaisons;
    private String phone;
    private String zipCode;
    private String checkDept;  // 检验部门
    private Date formationTime; // 编制日期
    private Date orderTime;     // 约俭日期
    private String contractNumber;

    private String fileResultIds;
    private String attachmentResultIds;

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
}
