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
@TableName("WORK_ITEM")
public class WorkItems extends AbstractEntity {
    @TableId(value = "ITEM_ROW_ID", type = IdType.UUID)
    private String itemRowId;
    private String procId;
    private Integer taskId;
    private String taskName;
    private String taskText;
    private Date receiveTime;
    private Date readTime;
    private Date finishTime;
    private Date expiredTime;
    private String status;
    private String autoFinish;
    private String userOrgId;
    private String userOrgName;
    private String userId;
    private String userName;
    private String userChoice;
    private String opinionContent;
    private String opinionArea;
    private Integer passTaskId;
    private String passOrgId;
    private String passOrgName;
    private String passUserId;
    private String passUserName;
    private String assigneeOrgId;
    private String assigneeOrgName;
    private String assigneeUserId;
    private String assigneeUserName;
    private String businessDataId;
    private String procName;
    private String createUserName;
    private String orgCode;
    private Integer workItemType;
    private String addTask;

}
