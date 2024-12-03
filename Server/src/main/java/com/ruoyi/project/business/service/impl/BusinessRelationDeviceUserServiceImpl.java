package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.project.business.domain.BusinessRelationDeviceUser;
import com.ruoyi.project.business.mapper.BusinessRelationDeviceUserMapper;
import com.ruoyi.project.business.service.BusinessRelationDeviceUserService;
import com.ruoyi.project.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-05 15:46 - 星期四
 * @package: com.ruoyi.project.business.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessRelationDeviceUserServiceImpl extends ServiceImpl<BusinessRelationDeviceUserMapper, BusinessRelationDeviceUser> implements BusinessRelationDeviceUserService
{
    @Resource
    private BusinessRelationDeviceUserMapper businessRelationDeviceUser;
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public boolean selectChangeUserIsGenericReport(String rowId) {
        return businessRelationDeviceUser.selectChangeUserIsGenericReportCount(rowId) != 0;
    }
}
