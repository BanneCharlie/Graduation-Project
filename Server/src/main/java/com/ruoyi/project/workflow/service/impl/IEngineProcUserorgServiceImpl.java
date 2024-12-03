package com.ruoyi.project.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.workflow.domain.EngineProcUserorg;
import com.ruoyi.project.workflow.mapper.EngineProcUserorgMapper;
import com.ruoyi.project.workflow.service.IEngineProcUserorgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 10:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IEngineProcUserorgServiceImpl extends BaseServiceImpl<EngineProcUserorgMapper, EngineProcUserorg> implements IEngineProcUserorgService {

    @Resource
    private EngineProcUserorgMapper engineProcUserorgMapper;

    @Override
    public List<EngineProcUserorg> getEngineProcUserOrgList(String procDefId, String actDefId) {
        QueryWrapper<EngineProcUserorg> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcUserorg::getProcDefId, procDefId)
                .eq(EngineProcUserorg::getActDefId, actDefId);
        return engineProcUserorgMapper.selectList(queryWrapper);
    }

    @Override
    public List<EngineProcUserorg> getEngineProcUserOrgListQuery(EngineProcUserorg proc) {
        QueryWrapper<EngineProcUserorg> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcUserorg::getProcDefId, proc.getProcDefId())
                .eq(EngineProcUserorg::getActDefId, proc.getActDefId())
                .like(StringUtils.isNotEmpty(proc.getUserName()), EngineProcUserorg::getUserName, proc.getUserName());
        return engineProcUserorgMapper.selectList(queryWrapper);
    }

    @Override
    public int deleteActUserByIds(String[] enIds) {
        int res = 1;
        try {
            for (String enId : enIds) {
                engineProcUserorgMapper.deleteById(enId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }
}
