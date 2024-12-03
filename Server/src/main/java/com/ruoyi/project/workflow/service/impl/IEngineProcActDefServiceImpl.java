package com.ruoyi.project.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.workflow.domain.EngineProcActDef;
import com.ruoyi.project.workflow.mapper.EngineProcActDefMapper;
import com.ruoyi.project.workflow.mapper.EngineProcActDefRelationMapper;
import com.ruoyi.project.workflow.service.IEngineProcActDefService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 10:38
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IEngineProcActDefServiceImpl extends BaseServiceImpl<EngineProcActDefMapper, EngineProcActDef> implements IEngineProcActDefService {

    @Resource
    private EngineProcActDefMapper engineProcActDefMapper;

    @Resource
    private EngineProcActDefRelationMapper engineProcActDefRelationMapper;

    @Override
    public List<EngineProcActDef> selectProcActList(EngineProcActDef actDef) {
        QueryWrapper<EngineProcActDef> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcActDef::getProcDefId, actDef.getProcDefId())
                .like(StringUtils.isNotBlank(actDef.getActName()), EngineProcActDef::getActName, actDef.getActName())
                .orderByAsc(EngineProcActDef::getEnOrder);;
        return engineProcActDefMapper.selectList(queryWrapper);
    }

    @Override
    public List<EngineProcActDef> selectListByProcDefId(String procDefId) {
        QueryWrapper<EngineProcActDef> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcActDef::getProcDefId, procDefId)
                .orderByAsc(EngineProcActDef::getCreateTime);;
        return engineProcActDefMapper.selectList(queryWrapper);
    }

    @Override
    public EngineProcActDef selectProcActById(String enId) {
        return engineProcActDefMapper.selectById(enId);
    }

    @Override
    public int deleteProcActByIds(String[] enIds) {
        int res = 1;
        try {
            for (String enId : enIds) {
                engineProcActDefRelationMapper.deleteById(enId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }

    @Override
    public int deleteProcActDefNodeByIds(String[] enIds) {
        int res = 1;
        try {
            for (String enId : enIds) {
                engineProcActDefMapper.deleteById(enId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }
}
