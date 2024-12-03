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
@TableName("APPLICATION_TYPE")
public class ApplicationTypes extends AbstractEntity {
    @TableId(value = "TYPE_ROW_ID", type = IdType.UUID)
    private String typeRowId;
    private String appTypeId;
    private String appTypeName;
    private String appType;
    private String status;
    private String remark;
    private String teamCode;

}
