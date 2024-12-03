package com.ruoyi.project.workflow.service.impl;

import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.workflow.domain.EngineProcWorkitem;
import com.ruoyi.project.workflow.mapper.EngineProcWorkitemMapper;
import com.ruoyi.project.workflow.service.IEngineProcWorkitemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/24 10:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IEngineProcWorkitemServiceImpl extends BaseServiceImpl<EngineProcWorkitemMapper, EngineProcWorkitem> implements IEngineProcWorkitemService {
}
