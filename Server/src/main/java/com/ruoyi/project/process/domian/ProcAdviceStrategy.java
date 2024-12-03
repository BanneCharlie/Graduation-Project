package com.ruoyi.project.process.domian;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.project.template.commons.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("PROC_ADVICE_STRATEGY")
public class ProcAdviceStrategy extends AbstractEntity {
    @TableId(value = "ADVICE_STRATEGY_ID", type = IdType.UUID)
    private String adviceStrategyId;
    private String tableName;
    private String tableId;
    private String piid;
    private String aiid;
    private String adviceContent;
    private Date fillTime;
    private String fillUserName;
    private String ptid;
    private String currentActName;
    private String recordId;
    private String docType;
    private String officialDocType;
    private String creatorUsername;
    private String creatorUserId;

}
