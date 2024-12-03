package com.ruoyi.project.workflow.service;

import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;
import com.ruoyi.project.workflow.domain.EngineProcUserorg;

import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 10:34
 */

public interface IEngineProcUserorgService extends IBaseService<EngineProcUserorg> {

    public List<EngineProcUserorg> getEngineProcUserOrgList(String procDefId, String actDefId);

    public List<EngineProcUserorg> getEngineProcUserOrgListQuery(EngineProcUserorg proc);

    public int deleteActUserByIds(String[] enIds);

}
