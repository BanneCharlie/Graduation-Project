package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-05 15:43 - 星期四
 * @package: com.ruoyi.project.business.domain
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("business_relation_device_user")
public class BusinessRelationDeviceUser {
    @TableId(value = "row_id" , type = IdType.UUID)
    private String rowId;
    private String deviceId;
    private String contractId;
    private String userId;
    private String userPhone;
    private String userName;
    private String userDeptName;
}
