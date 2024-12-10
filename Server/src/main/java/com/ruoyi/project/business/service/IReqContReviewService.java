package com.ruoyi.project.business.service;

import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author niminui
 * @date 2021/5/28 11:13
 */
public interface IReqContReviewService extends IBaseService<ReqContReview> {

    public List<ReqContReview> getList(ReqContReview reqContReview,String beginTime , String endTime);

    boolean deleteBeanByIds(String[] rowIds);

    List<Object> selectRowIds();

    List<ReqContReview> getALlList();

    List<Integer> getBarChartData(List<ReqContReview> reqContReviewList);
}
