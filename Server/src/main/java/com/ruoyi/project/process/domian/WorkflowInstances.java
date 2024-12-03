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
@TableName("WORKFLOW_INSTANCE")
public class WorkflowInstances extends AbstractEntity {
    @TableId(value = "INSTANCE_ROW_ID", type = IdType.UUID)
    private String instanceRowId;
    private String appCode;
    private String appName;
    private Long workflowId;
    private String workflowVersion;
    private String procId;
    private String procName;
    private Long importance;
    private Long secret;
    private String bussinessId;
    private Date beginTime;
    private Date endTime;
    private String status;

}
