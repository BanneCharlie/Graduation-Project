package com.ruoyi.project.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.business.domain.BusinessReportExamine;

import java.util.List;


public interface BusinessRepExamService extends IService<BusinessReportExamine> {

    List<BusinessReportExamine> getALlList();

    List<Integer> getBarChartData(List<BusinessReportExamine> businessReportExamineList);
}
