package com.ruoyi.project.process.service.impl;

import com.ruoyi.project.process.domian.ApplicationTypes;
import com.ruoyi.project.process.mapper.ApplicationTypesMapper;
import com.ruoyi.project.process.service.IApplicationTypesService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/24 13:37
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IApplicationTypesServiceImpl extends BaseServiceImpl<ApplicationTypesMapper, ApplicationTypes> implements IApplicationTypesService {
}
