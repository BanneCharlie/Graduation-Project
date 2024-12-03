package com.ruoyi.project.process.service.impl;

import com.ruoyi.project.process.domian.ProcAdviceStrategy;
import com.ruoyi.project.process.mapper.ProcAdviceStrategyMapper;
import com.ruoyi.project.process.service.IProcAdviceStrategyService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/24 13:40
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IProcAdviceStrategyServiceImpl extends BaseServiceImpl<ProcAdviceStrategyMapper, ProcAdviceStrategy> implements IProcAdviceStrategyService {
}
