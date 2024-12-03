package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @date: 2021_05_28 _ 10:55__星期五
 * @package: com.ruoyi.project.business.domain
 * @JDK-Version : 1.8
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@TableName("business_request_contract_review")
// 委托合同评审
public class ReqContReview{
    @TableId(value = "row_id",type = IdType.UUID)
    private String rowId;
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
    private Integer isPaymentSuccess;   // 0 未缴费  1 已缴费

    private String paymentType;   // 缴费类型
    private Integer confirmPrint; // 缴费了类型为年终缴费的 需要先由财务人员确认打印才可以打印

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
    private Integer isEnd;        // 流程结束标志位

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    private String isRead; //该委托合同是否开启检验申请，1为未申请，0位已申请

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
