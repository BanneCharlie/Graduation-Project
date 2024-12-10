package com.ruoyi.project.process.domian;

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
@TableName("PROC_FORM_PRIVILLEGE_RELATION")
public class ProcFormPrivillegeRelation extends AbstractEntity {
    @TableId(value = "RELATION_ROW_ID", type = IdType.UUID)
    private String relationRowId;
    private String processTemplateId;
    private String activityId;
    private String businessKey;
    private String formElementName;
    private String elementPrivillege;
    private String createUserId;
    private String relationStatus;
    private String relationComment;
    private String formElementComment;
}
