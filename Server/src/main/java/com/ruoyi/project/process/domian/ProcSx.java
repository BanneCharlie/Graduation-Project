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
@TableName("PROC_SX")
public class ProcSx extends AbstractEntity {
    @TableId(value = "SX_ROW_ID", type = IdType.UUID)
    private String sxRowId;
    private String sxName;
    private String sxShowName;
    private String sxType;
    private String sxIntro;
    private String createUserId;
    private String dwId;
    private String deptId;
    private String dataStatus;
    private Long dataOrder;
    private String formId;

}
