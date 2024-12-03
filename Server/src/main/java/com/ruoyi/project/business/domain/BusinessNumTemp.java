package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-10 11:23 - 星期四
 * @package: com.ruoyi.project.business.domain
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("business_number_temp")
public class BusinessNumTemp {
    private String businessRowId;
    private String deviceType;
    private String type;
    private Integer number;
    private String isUse;
    @TableId(value = "current_operation_identifying",type = IdType.ID_WORKER)
    private String currentOperationIdentifying;
    private String processStatus;
}
