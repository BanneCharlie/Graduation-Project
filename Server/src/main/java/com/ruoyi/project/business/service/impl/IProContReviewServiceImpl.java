package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.business.domain.ProContReview;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.business.mapper.ProContReviewMapper;
import com.ruoyi.project.business.service.IProContReviewService;
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
 * @date: 2021_05_28 _ 13:57__星期五
 * @package: com.ruoyi.project.business.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IProContReviewServiceImpl extends BaseServiceImpl<ProContReviewMapper, ProContReview> implements IProContReviewService {

    @Resource
    private ProContReviewMapper proContReviewMapper;

    @Resource
    private ISysUserService iSysUserService;

    @Override
    public boolean deleteBeanByIds(String[] rowIds) {
        boolean flag = true;
        try {
            for (String rowId : rowIds) {
                proContReviewMapper.deleteById(rowId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public List<ProContReview> getList(ProContReview proContReview) {
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        QueryWrapper<ProContReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotEmpty(proContReview.getCreateDeptName()), ProContReview::getCreateDeptName, proContReview.getCreateDeptName())
                .ge(proContReview.getCreateTime() != null, ProContReview::getCreateTime, proContReview.getCreateTime())
                .le(proContReview.getCreateTime() != null, ProContReview::getCreateTime, proContReview.getCreateTime())
                .eq(!SecurityUtils.isAdmin(user.getUserId()), ProContReview::getCreateUserId, user.getUserName());
        return proContReviewMapper.selectList(queryWrapper);
    }
}
