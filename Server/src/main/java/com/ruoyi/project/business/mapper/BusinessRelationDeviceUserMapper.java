package com.ruoyi.project.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.business.domain.BusinessRelationDeviceUser;
import org.apache.ibatis.annotations.Param;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-05 15:45 - 星期四
 * @package: com.ruoyi.project.business.mapper
 * @JDK-Version : 1.8
 */
public interface BusinessRelationDeviceUserMapper extends BaseMapper<BusinessRelationDeviceUser> {

    Integer selectChangeUserIsGenericReportCount(
            @Param("rowId") String rowId);
}
