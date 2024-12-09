package com.ruoyi.project.process.service.impl;

import com.ruoyi.project.process.domian.BdPagesizeRecord;
import com.ruoyi.project.process.mapper.BdPagesizeRecordMapper;
import com.ruoyi.project.process.service.IBdPagesizeRecordService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/24 13:38
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IBdPagesizeRecordServiceImpl extends BaseServiceImpl<BdPagesizeRecordMapper, BdPagesizeRecord> implements IBdPagesizeRecordService {
}
