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
@TableName("PROC_SXPZ")
public class ProcSxpz extends AbstractEntity {
    @TableId(value = "SXPZ_ROW_ID", type = IdType.UUID)
    private String sxpzRowId;
    private String sxpzName;
    private String sxpzShowName;
    private String sxId;
    private String ptId;
    private String sxpzIntro;
    private String processName;
    private String createUserId;
    private String modifyUserId;
    private String dataStatus;
    private Long dataOrder;
    private String dwId;
    private String dwName;
    private String deptId;
    private String deptName;
    private String sxpzType;
    private String sxpzZfType;

}
