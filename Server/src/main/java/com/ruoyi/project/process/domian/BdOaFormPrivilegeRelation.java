package com.ruoyi.project.process.domian;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.project.template.commons.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author niminui
 * @date 2021/5/31 14:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("bd_oa_form_privilege_relation")
public class BdOaFormPrivilegeRelation extends AbstractEntity {
    @TableId(value = "ROW_ID", type = IdType.UUID)
    private String rowId;
    private String processTemplateId;
    private String activityId;
    private String businessKey;
    private String formElementName;
    private String elementPrivilege;
    private String createUserId;
    private String relationStatus;
    private String relationComment;
    private String formElementComment;

}
