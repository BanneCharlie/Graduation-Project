package com.ruoyi.project.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.project.process.domian.ProcSxpz;
import com.ruoyi.project.process.mapper.ProcSxpzMapper;
import com.ruoyi.project.process.service.IProcSxpzService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 13:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IProcSxpzServiceImpl extends BaseServiceImpl<ProcSxpzMapper, ProcSxpz> implements IProcSxpzService {

    @Resource
    private ProcSxpzMapper procSxpzMapper;

    @Override
    public List<ProcSxpz> getList(String sxId, String sxZfType) {
        QueryWrapper<ProcSxpz> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProcSxpz::getSxId, sxId)
                .eq(ProcSxpz::getSxpzZfType, sxZfType)
                .orderByAsc(ProcSxpz::getDataOrder);
        return procSxpzMapper.selectList(queryWrapper);
    }
}
