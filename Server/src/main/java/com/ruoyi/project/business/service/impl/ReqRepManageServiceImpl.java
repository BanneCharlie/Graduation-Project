package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.business.domain.ReqRepManage;
import com.ruoyi.project.business.mapper.ReqRepManageMapper;
import com.ruoyi.project.business.service.IReqRepManageService;
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
 * @date: 2021_05_28 _ 11:52__星期五
 * @package: com.ruoyi.project.business.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReqRepManageServiceImpl extends BaseServiceImpl<ReqRepManageMapper, ReqRepManage> implements IReqRepManageService {

    @Resource
    private ReqRepManageMapper reqRepManageMapper;

    @Resource
    private ISysUserService iSysUserService;

    @Override
    public List<ReqRepManage> getList(ReqRepManage reqRepManage,String beginTime,String endTime) {
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        QueryWrapper<ReqRepManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotEmpty(reqRepManage.getCreateDeptName()), ReqRepManage::getCreateDeptName, reqRepManage.getCreateDeptName())
                .ge(StringUtils.isNotEmpty(beginTime), ReqRepManage::getCreateTime, beginTime)
                .le(StringUtils.isNotEmpty(endTime), ReqRepManage::getCreateTime, endTime)
                .eq(!SecurityUtils.isAdmin(user.getUserId()), ReqRepManage::getCreateUserId, user.getUserName())
                .orderByDesc(ReqRepManage::getCreateTime);
        return reqRepManageMapper.selectList(queryWrapper);
    }

    @Override
    public boolean deleteBeanByIds(String[] rowIds) {
        boolean flag = true;
        try {
            for (String rowId : rowIds) {
                reqRepManageMapper.deleteById(rowId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Object> selectReqContractViewRowIds() {
        return reqRepManageMapper.selectObjs(
            new QueryWrapper<ReqRepManage>()
                .select("contract_id"));
    }
}
