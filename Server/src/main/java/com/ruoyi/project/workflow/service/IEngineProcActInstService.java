package com.ruoyi.project.workflow.service;

import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;
import com.ruoyi.project.workflow.domain.EngineProcActInst;

/**
 * @author niminui
 * @date 2021/5/24 10:32
 */
public interface IEngineProcActInstService extends IBaseService<EngineProcActInst> {
    /**
     * 判断流程是否已经结束，1为已结束，0为未结束，-1为无该流程
     * @return
     */
    public Integer isEndNode(String piId);

}
