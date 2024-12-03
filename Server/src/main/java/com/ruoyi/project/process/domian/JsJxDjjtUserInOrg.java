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
@TableName("JS_JX_DJJT_USER_IN_ORG")
public class JsJxDjjtUserInOrg extends AbstractEntity {
    @TableId(value = "DJJT_ROW_ID", type = IdType.UUID)
    private String djjtRowId;
    private String orgId;
    private String orgName;
    private String userId;
    private String userName;
    private String oType;
    private Long orderNum;
    private String mobilePhone;
    private String uZw;

}
