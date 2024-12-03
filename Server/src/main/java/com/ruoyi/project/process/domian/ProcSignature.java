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
@TableName("PROC_SIGNATURE")
public class ProcSignature extends AbstractEntity {
    @TableId(value = "SIGNATURE_ID", type = IdType.UUID)
    private String signatureId;
    private String creatorId;
    private String creatorName;
    private String controlName;
    private String optionContext;
    private String tableName;
    private String tableId;
    private String deleted;
    private String piid;
    private String aiid;

}
