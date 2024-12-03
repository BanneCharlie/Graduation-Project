package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.project.template.commons.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 合同申请设备
 * @author niminui
 * @date 2021/6/10 14:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@TableName("business_device")
public class BusinessDevice extends AbstractEntity {

    @TableId(value = "row_id",type = IdType.UUID)
    private String rowId;
    private String contractId; //合同ID
    private String reqCheckId; //检验报告ID
    private String deviceCategory;
    private String deviceName;
    private String deviceNumber;
    private String useUnit;
    private Integer orderNum;
    private String fromUserId; //记录来自
    private String fromUserName;
    private String receivingUserId; //领用人id
    private String receivingUserName; //领用人名称
    private Integer isReceiving; //是否已经领用，0为是，1为否
    private Integer isRead; //是否已申请，0已申请，1未申请
    private Integer isHandle; //是否已处理（未处理报告），0已处理，1未处理
    private Integer isGenericReport;    // 是否已生成报告 0已生成 1未生成

}
