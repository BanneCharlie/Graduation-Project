package com.ruoyi.project.process.service.impl;

import com.ruoyi.project.process.domian.ProcFormPrivillegeRelation;
import com.ruoyi.project.process.mapper.ProcFormPrivillegeRelationMapper;
import com.ruoyi.project.process.service.IProcFormPrivillegeRelationService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/24 13:41
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IProcFormPrivillegeRelationServiceImpl extends BaseServiceImpl<ProcFormPrivillegeRelationMapper, ProcFormPrivillegeRelation> implements IProcFormPrivillegeRelationService {
}
