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
@TableName("BD_PAGESIZE_RECORD")
public class BdPagesizeRecord extends AbstractEntity {
    @TableId(value = "RECORD_ROW_ID", type = IdType.UUID)
    private String recordRowId;
    private String userId;
    private Integer pageSizeRecord;

}
