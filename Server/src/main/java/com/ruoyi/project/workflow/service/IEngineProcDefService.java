package com.ruoyi.project.workflow.service;

import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;
import com.ruoyi.project.workflow.domain.EngineProcDef;

import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 10:33
 */
public interface IEngineProcDefService extends IBaseService<EngineProcDef> {

    /**
     * 根据条件分页查询流程引擎列表
     *
     * @param engineName 流程引擎定义名称
     * @return 流程定义
     */
    public List<EngineProcDef> selectEngineList(String engineName);

    /**
     * 根据流程定义编号获取详细信息
     *  @param enId 流程定义编号
     *  @return 流程定义
     */
    public EngineProcDef selectEngineById(String enId);

    /**
     * 根据流程定义编号获取详细信息列表
     *  @param enIds 流程定义编号
     *  @return 流程定义
     */
    public List<EngineProcDef> selectEngineByIds(String[] enIds);

    /**
     * 批量删除流程模板
     *
     * @param enIds 需要删除的流程模板ID
     * @return 结果
     */
    int deleteEngineProcByIds(String[] enIds);
}
