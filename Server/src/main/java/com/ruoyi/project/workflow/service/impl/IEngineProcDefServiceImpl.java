package com.ruoyi.project.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.project.system.domain.SysRole;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.workflow.domain.EngineProcDef;
import com.ruoyi.project.workflow.mapper.EngineProcDefMapper;
import com.ruoyi.project.workflow.service.IEngineProcDefService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 10:41
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IEngineProcDefServiceImpl extends BaseServiceImpl<EngineProcDefMapper, EngineProcDef> implements IEngineProcDefService {

    @Resource
    private EngineProcDefMapper engineProcDefMapper;

    @Override
    public List<EngineProcDef> selectEngineList(String engineName) {
        QueryWrapper<EngineProcDef> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(engineName), EngineProcDef::getProcName, engineName)
                .orderByDesc(EngineProcDef::getCreateTime);;
        return engineProcDefMapper.selectList(queryWrapper);
    }

    @Override
    public EngineProcDef selectEngineById(String enId) {
        return engineProcDefMapper.selectById(enId);
    }

    @Override
    public List<EngineProcDef> selectEngineByIds(String[] enIds) {
        List<EngineProcDef> list = new ArrayList<>();
        for (String enId : enIds) {
            list.add(engineProcDefMapper.selectById(enId));
        }
        return list;
    }

    @Override
    public int deleteEngineProcByIds(String[] enIds) {
        int res = 1;
        try {
            for (String enId : enIds) {
                engineProcDefMapper.deleteById(enId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }
}
