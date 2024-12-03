package com.ruoyi.project.process.domian;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author niminui
 * @date 2021/5/30 10:37
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@TableName("v_task")
public class VTask {

    private String mainRowId;
    private String workItemId;
    private String title;
    private String piId;
    private String ptId;
    private String businessKey;
    private String atId;
    private String rowId;
    private String taskId;
    private String passTaskId;
    private String taskName;
    private String passUserName;
    private Date createTime;
    private String userId;
    private String assigneeUserId;
    private String assigneeUserName;
    private String workItemType;
    private String taskStatus;
    private String bianhao;
    private String bdDate;

}
