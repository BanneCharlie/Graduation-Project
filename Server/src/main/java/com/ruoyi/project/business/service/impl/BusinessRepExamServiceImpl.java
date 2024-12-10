package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.project.business.domain.BusinessReportExamine;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.business.mapper.BusinessRepExamMapper;
import com.ruoyi.project.business.service.BusinessRepExamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BusinessRepExamServiceImpl extends ServiceImpl<BusinessRepExamMapper, BusinessReportExamine> implements BusinessRepExamService {

    @Resource
    private BusinessRepExamMapper businessRepExamMapper;

    @Override
    public List<BusinessReportExamine> getALlList() {
        QueryWrapper<BusinessReportExamine> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().orderByDesc(BusinessReportExamine::getCreateTime);
        List<BusinessReportExamine> businessReportExamineList = businessRepExamMapper.selectList(queryWrapper);

        if (businessReportExamineList ==  null && businessReportExamineList.size() <= 0){
            throw new CustomException("未查询到相关数据");
        }

        return businessReportExamineList;
    }

    @Override
    public List<Integer> getBarChartData(List<BusinessReportExamine> businessReportExamineList) {
        List<Integer> barChartData = new ArrayList<>(Collections.nCopies(12, 0));

        for (BusinessReportExamine reqContReview : businessReportExamineList) {
            int month = reqContReview.getCreateTime().getMonth(); // 假设 month 从 0 开始
            barChartData.set(month, barChartData.get(month) + 1);
        }

        return barChartData;
    }
}
