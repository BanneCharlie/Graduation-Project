package com.ruoyi.project.business.service.impl;

import com.ruoyi.project.business.mapper.RequestStatisticsMapper;
import com.ruoyi.project.business.service.IRequestStatisticsService;
import com.ruoyi.project.business.vo.ReqStatisticsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niminui
 * @date 2021/6/30 10:15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IRequestStatisticsServiceImpl implements IRequestStatisticsService {

    @Resource
    private RequestStatisticsMapper requestStatisticsMapper;

    /**
     * 获取委托检验合同申请信息及对应的检验报告申请
     * @param reqStatisticsVo 查询参数
     * @return
     */
    @Override
    public List<ReqStatisticsVo> getList(ReqStatisticsVo reqStatisticsVo) {
        return requestStatisticsMapper.getList(reqStatisticsVo);
    }
}
