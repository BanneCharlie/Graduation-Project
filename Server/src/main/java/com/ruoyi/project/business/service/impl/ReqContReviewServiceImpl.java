package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.business.mapper.ReqContReviewMapper;
import com.ruoyi.project.business.service.IReqContReviewService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReqContReviewServiceImpl extends BaseServiceImpl<ReqContReviewMapper, ReqContReview> implements IReqContReviewService {

    @Resource
    private ReqContReviewMapper reqContReviewMapper;

    @Resource
    private ISysUserService iSysUserService;

    @Override
    public List<ReqContReview> getList(ReqContReview reqContReview,String beginTime , String endTime) {

        SysUser user = SecurityUtils.getLoginUser().getUser();
        QueryWrapper<ReqContReview> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
                .like(StringUtils.isNotEmpty(reqContReview.getCreateDeptName()), ReqContReview::getCreateDeptName, reqContReview.getCreateDeptName())
                .ge(StringUtils.isNotEmpty(beginTime), ReqContReview::getCreateTime, beginTime)
                .le(StringUtils.isNotEmpty(endTime), ReqContReview::getCreateTime, endTime)
                .eq(!SecurityUtils.isAdmin(user.getUserId()) && user.getDeptId() != 10113L, ReqContReview::getCreateUserId, user.getUserName())
                .orderByDesc(ReqContReview::getCreateTime);
        return reqContReviewMapper.selectList(queryWrapper);
    }

    @Override
    public boolean deleteBeanByIds(String[] rowIds) {
        boolean flag = true;
        try {
            for (String rowId : rowIds) {
                reqContReviewMapper.deleteById(rowId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Object> selectRowIds() {
        QueryWrapper<ReqContReview> wrapper = new QueryWrapper<>();
        wrapper.select("row_id");
        return reqContReviewMapper.selectObjs(wrapper);
    }

    /**
     * 查询所有合同评审列表
     * @return reqContReviewList
     */
    @Override
    public List<ReqContReview> getALlList() {
        QueryWrapper<ReqContReview> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
                .orderByDesc(ReqContReview::getCreateTime);
        List<ReqContReview> reqContReviewList = reqContReviewMapper.selectList(queryWrapper);

        if(reqContReviewList ==  null && reqContReviewList.size() <= 0){
           throw new CustomException("查询合同评审列表为空");
        }

        return reqContReviewList;
    }

    @Override
    public List<Integer> getBarChartData(List<ReqContReview> reqContReviewList) {
        List<Integer> barChartData = new ArrayList<>(Collections.nCopies(12, 0));

        for (ReqContReview reqContReview : reqContReviewList) {
            int month = reqContReview.getCreateTime().getMonth(); // 假设 month 从 0 开始
            barChartData.set(month, barChartData.get(month) + 1);
        }

        return barChartData;
    }

}
