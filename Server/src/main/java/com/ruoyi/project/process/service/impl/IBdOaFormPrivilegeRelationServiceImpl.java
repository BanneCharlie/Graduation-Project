package com.ruoyi.project.process.service.impl;

import com.ruoyi.project.process.domian.BdOaFormPrivilegeRelation;
import com.ruoyi.project.process.mapper.BdOaFormPrivilegeRelationMapper;
import com.ruoyi.project.process.service.IBdOaFormPrivilegeRelationService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/31 14:10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IBdOaFormPrivilegeRelationServiceImpl extends BaseServiceImpl<BdOaFormPrivilegeRelationMapper, BdOaFormPrivilegeRelation> implements IBdOaFormPrivilegeRelationService {
}
