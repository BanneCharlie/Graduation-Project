package com.ruoyi.project.business.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author niminui
 * @date 2021/6/30 10:19
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ReqStatisticsVo {

    private String reqContRowId; //合同rowId
    private String contractNumber; //合同编号
    private Integer deviceCount;
    private String contractMoney;
    private String checkDeptId;  // 检验部门
    private String checkDept;  // 检验部门
    private String checkUserName; // 检验员name，以逗号分隔
    @TableField(exist = false)
    private String checkUserNameMore; // 当检验员多于三个时，则checkUserName显示多于的为"..."，该字段存错所有的名称
    private String checkUserId;   // 检验员id，以逗号分隔
    private String contTitle;
    private String contPtid;        // 合同流程模板id
    private String contPiid;        // 合同流程实例 id
    private String contPaid;        // 合同流程环节id
    private String contProceedingId; // 合同事项配置id
    private String contBusinessKey; // 合同业务主键
    private String contActName;
    private String contActId;
    private String contDeletes;
    private String contIsRead; //该委托合同是否开启检验申请，1为未申请，0位已申请

    private String reqRepRowId; //检验报告rowId
    private String reportNumber; //检验报告报告编号
    private String deviceRowId; //设备编号
    private String repTitle;
    private String repPtid;        // 检验报告流程模板id
    private String repPiid;        // 检验报告流程实例 id
    private String repPaid;        // 检验报告流程环节id
    private String repProceedingId; // 检验报告事项配置id
    private String repBusinessKey; // 检验报告业务主键
    private String repActName;
    private String repActId;
    private String repDeletes;
    private String repIsRead; //该委托合同是否开启检验申请，1为未申请，0位已申请
    private Date createTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

}
