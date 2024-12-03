package com.ruoyi.project.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.project.template.commons.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("ENGINE_PROC_INST")
public class EngineProcInst extends AbstractEntity {
    @TableId(value = "EN_ID", type = IdType.UUID)
    private String enId;
    private String procInstId;
    private String procName;
    private String procDesc;
    private String currActName;
    private String currActId;
    private String procOrg;
    private String enStatus;
    private String modifyUserId;
    private String createUserId;
    private String verCode;
    private String verNum;
    private String enType;
    private String ext1;
    private String ext2;
    private String fromTitle;
    private String fromType;

}
