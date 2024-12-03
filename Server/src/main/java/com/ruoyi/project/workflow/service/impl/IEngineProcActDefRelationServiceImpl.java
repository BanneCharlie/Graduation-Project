package com.ruoyi.project.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.workflow.domain.EngineProcActDefRelation;
import com.ruoyi.project.workflow.mapper.EngineProcActDefRelationMapper;
import com.ruoyi.project.workflow.service.IEngineProcActDefRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 10:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IEngineProcActDefRelationServiceImpl extends BaseServiceImpl<EngineProcActDefRelationMapper, EngineProcActDefRelation> implements IEngineProcActDefRelationService {

    @Resource
    private EngineProcActDefRelationMapper engineProcActDefRelationMapper;

    @Override
    public List<EngineProcActDefRelation> getEngineProcActDefRelationList(String procDefId, String actDefId) {
        QueryWrapper<EngineProcActDefRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcActDefRelation::getProcDefId, procDefId)
                .eq(EngineProcActDefRelation::getActDefRelId, actDefId);
        return engineProcActDefRelationMapper.selectList(queryWrapper);
    }
}
