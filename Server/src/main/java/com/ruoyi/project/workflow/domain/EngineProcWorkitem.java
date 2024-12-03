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
@TableName("ENGINE_PROC_WORKITEM")
public class EngineProcWorkitem extends AbstractEntity {
    @TableId(value = "EN_ID", type = IdType.UUID)
    private String enId;
    private String workItemId;
    private String workItemName;
    private String userId;
    private String workItemUserName;
    private String orgId;
    private String orgName;
    private String enStatus;
    private String modifyUserId;
    private String createUserId;
    private String verCode;
    private String verNum;
    private String enType;
    private String actInsName;
    private String actInsId;
    private String procInstId;
    private String procDefId;
    private String procDefName;
    private String fromTitle;
    private String fromType;
    private String giveUserId;
    private String giveUserName;

}
