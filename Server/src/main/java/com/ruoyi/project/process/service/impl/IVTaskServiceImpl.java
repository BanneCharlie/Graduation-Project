package com.ruoyi.project.process.service.impl;

import com.ruoyi.project.process.domian.VTask;
import com.ruoyi.project.process.mapper.VTaskMapper;
import com.ruoyi.project.process.service.IVTaskService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/30 10:39
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IVTaskServiceImpl extends BaseServiceImpl<VTaskMapper, VTask> implements IVTaskService {
}
