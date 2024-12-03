package com.ruoyi.project.workflow.service.impl;

import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.workflow.domain.EngineProcDefByte;
import com.ruoyi.project.workflow.mapper.EngineProcDefByteMapper;
import com.ruoyi.project.workflow.service.IEngineProcDefByteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/24 10:40
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IEngineProcDefByteServiceImpl extends BaseServiceImpl<EngineProcDefByteMapper, EngineProcDefByte> implements IEngineProcDefByteService {
}
