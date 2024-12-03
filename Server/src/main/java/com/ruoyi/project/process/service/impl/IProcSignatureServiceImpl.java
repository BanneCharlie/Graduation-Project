package com.ruoyi.project.process.service.impl;

import com.ruoyi.project.process.domian.ProcSignature;
import com.ruoyi.project.process.mapper.ProcSignatureMapper;
import com.ruoyi.project.process.service.IProcSignatureService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/24 13:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IProcSignatureServiceImpl extends BaseServiceImpl<ProcSignatureMapper, ProcSignature> implements IProcSignatureService {
}
