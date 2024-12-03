package com.ruoyi.project.workflow.service;

import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;
import com.ruoyi.project.workflow.domain.EngineProcActDef;

import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 10:30
 */
public interface IEngineProcActDefService extends IBaseService<EngineProcActDef> {

    /**
     * 根据名称查询流程节点模板
     * @return
     */
    public List<EngineProcActDef> selectProcActList(EngineProcActDef actDef);

    public List<EngineProcActDef> selectListByProcDefId(String procDefId);

    public EngineProcActDef selectProcActById(String enId);

    /*
        删除的是 流程节点里边的下一步参与的流程节点
     */
    public int deleteProcActByIds(String[] enIds);
    /*
        删除当前流程定义里边的流程
     */
    int deleteProcActDefNodeByIds(String[] ids);

}
