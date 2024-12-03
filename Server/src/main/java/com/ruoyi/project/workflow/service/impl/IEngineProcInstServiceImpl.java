package com.ruoyi.project.workflow.service.impl;

import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.workflow.domain.EngineProcInst;
import com.ruoyi.project.workflow.mapper.EngineProcInstMapper;
import com.ruoyi.project.workflow.service.IEngineProcInstService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/24 10:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IEngineProcInstServiceImpl extends BaseServiceImpl<EngineProcInstMapper, EngineProcInst> implements IEngineProcInstService {
}
