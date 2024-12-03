package com.ruoyi.project.process.service.impl;

import com.ruoyi.project.process.domian.JsJxDjjtUserInOrg;
import com.ruoyi.project.process.mapper.JsJxDjjtUserInOrgMapper;
import com.ruoyi.project.process.service.IJsJxDjjtUserInOrgService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niminui
 * @date 2021/5/24 13:39
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IJsJxDjjtUserInOrgServiceImpl extends BaseServiceImpl<JsJxDjjtUserInOrgMapper, JsJxDjjtUserInOrg> implements IJsJxDjjtUserInOrgService {
}
