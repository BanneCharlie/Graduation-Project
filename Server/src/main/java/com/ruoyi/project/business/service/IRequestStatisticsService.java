package com.ruoyi.project.business.service;

import com.ruoyi.project.business.vo.ReqStatisticsVo;

import java.util.List;

/**
 * @author niminui
 * @date 2021/6/30 10:15
 */
public interface IRequestStatisticsService {
    /**
     * 获取委托检验合同申请信息及对应的检验报告申请
     * @param reqStatisticsVo 查询参数
     * @return
     */
    public List<ReqStatisticsVo> getList(ReqStatisticsVo reqStatisticsVo);
}
