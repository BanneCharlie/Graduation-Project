package com.ruoyi.project.workflow.service;

import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;
import com.ruoyi.project.workflow.domain.EngineProcActDefRelation;

import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 10:31
 */
public interface IEngineProcActDefRelationService extends IBaseService<EngineProcActDefRelation> {

    public List<EngineProcActDefRelation> getEngineProcActDefRelationList(String procDefId, String actDefId);

}
