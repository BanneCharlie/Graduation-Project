package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.business.domain.ProRepManage;
import com.ruoyi.project.business.domain.ProRepManage;
import com.ruoyi.project.business.mapper.ProRepManageMapper;
import com.ruoyi.project.business.mapper.ReqRepManageMapper;
import com.ruoyi.project.business.service.IProRepManageService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_28 _ 13:59__星期五
 * @package: com.ruoyi.project.business.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProRepManageServiceImpl extends BaseServiceImpl<ProRepManageMapper, ProRepManage> implements IProRepManageService {
    @Resource
    private ProRepManageMapper proRepManageMapper;

    @Resource
    private ISysUserService iSysUserService;

    @Override
    public List<ProRepManage> getList(ProRepManage proRepManage) {
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        QueryWrapper<ProRepManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotEmpty(proRepManage.getCreateDeptName()), ProRepManage::getCreateDeptName, proRepManage.getCreateDeptName())
                .ge(proRepManage.getCreateTime() != null, ProRepManage::getCreateTime, proRepManage.getCreateTime())
                .le(proRepManage.getCreateTime() != null, ProRepManage::getCreateTime, proRepManage.getCreateTime())
                .eq(!SecurityUtils.isAdmin(user.getUserId()), ProRepManage::getCreateUserId, user.getUserName());
        return proRepManageMapper.selectList(queryWrapper);
    }

    @Override
    public boolean deleteBeanByIds(String[] rowIds) {
        boolean flag = true;
        try {
            for (String rowId : rowIds) {
                proRepManageMapper.deleteById(rowId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
