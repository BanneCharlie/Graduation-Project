package com.ruoyi.project.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.workflow.domain.EngineProcActInst;
import com.ruoyi.project.workflow.mapper.EngineProcActInstMapper;
import com.ruoyi.project.workflow.service.IEngineProcActInstService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 10:39
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IEngineProcActInstServiceImpl extends BaseServiceImpl<EngineProcActInstMapper, EngineProcActInst> implements IEngineProcActInstService {

    @Resource
    private EngineProcActInstMapper engineProcActInstMapper;

    /**
     * 判断流程是否已经结束，1为已结束，0为未结束，-1为无该流程
     * @return
     */
    @Override
    public Integer isEndNode(String piId) {
        int res = -1;
        QueryWrapper<EngineProcActInst> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcActInst::getProcInstId, piId)
                .orderByDesc(EngineProcActInst::getCreateTime);
        List<EngineProcActInst> list = engineProcActInstMapper.selectList(queryWrapper);
        if (list != null && list.size() != 0) {
            res = 0;
            for (EngineProcActInst inst : list) {
                if (StringUtils.equals(inst.getEnType(), "end")) {
                    res = 1;
                    break;
                }
            }
        }
        return res;
    }
}
