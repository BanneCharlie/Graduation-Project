package com.ruoyi.project.process.service.impl;

import com.ruoyi.project.process.domian.WorkflowInstances;
import com.ruoyi.project.process.mapper.WorkflowInstancesMapper;
import com.ruoyi.project.process.service.IWorkflowInstancesService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/24 13:45
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IWorkflowInstancesServiceImpl extends BaseServiceImpl<WorkflowInstancesMapper, WorkflowInstances> implements IWorkflowInstancesService {
}
