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
@TableName("ENGINE_PROC_ACT_DEF_RELATION")
public class EngineProcActDefRelation extends AbstractEntity {
    @TableId(value = "EN_ID", type = IdType.ID_WORKER_STR)
    private String enId;
    private String actDefRelId;
    private String actName;
    private String actDefId;
    private String enStatus;
    private String modifyUserId;
    private String createUserId;
    private String verCode;
    private String verNum;
    private String enType;
    private String procDefId;
    private Long enOrder;

    /** 连线内容 */
    private String nodeLineName;


}