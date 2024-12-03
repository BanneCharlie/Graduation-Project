package com.ruoyi.project.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author niminui
 * @date 2021/6/24 16:50
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class DeviceVo {

    private String rowId; //设备rowId
    private String contractRowId; //合同rowId
    private String entrustUnit;
    private String typeUnit;
    private String useUnit;
    private String addressUnit;
    private String deviceCategory;
    private String deviceName;
    private String checkCategory;
    private Integer deviceCount;
    private String contractMoney;
    private String realMoney;
    private String paymentType;
    private String isPaymentSuccess;
    private String liaisons;
    private String phone;
    private String zipCode;
    private String checkDept;  // 检验部门
    private Date formationTime; // 编制日期
    private Date orderTime;     // 约俭日期
    private String contractNumber;

    private String checkUserName; // 检验员name，以逗号分隔
    private String checkUserId;   // 检验员id，以逗号分隔

    private String fileResultIds;
    private String attachmentResultIds;


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

    private String deviceNumber;
    private String receivingUserId; //领用人id
    private String receivingUserName; //领用人名称
    private String isReceiving; //是否已经领用，0为是，1为否
    private String isRead;      //对于已领取的任务是否已处理，0为是，1为否
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;    // 任务生成时间
    private Date updateTime;    // 任务领取时间

}
