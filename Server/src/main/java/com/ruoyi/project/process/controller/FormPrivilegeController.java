package com.ruoyi.project.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.process.domian.BdOaFormPrivilegeRelation;
import com.ruoyi.project.process.domian.VTask;
import com.ruoyi.project.process.service.IBdOaFormPrivilegeRelationService;
import com.ruoyi.project.process.service.IVTaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("form-privilege")
public class FormPrivilegeController {

    /**
     * 表单元素权限manager
     */
    @Resource
    private IBdOaFormPrivilegeRelationService iBdOaFormPrivilegeRelationService;

    @Resource
    private IVTaskService ivTaskService;

    /**
     * 获取表单 权限列表json
     *            (sw/fw)
     * @return
     */
    @RequestMapping(value = "list")
    public AjaxResult queryFormPrivilegeJson(
            @RequestParam(required = false, defaultValue = "") String taskId) {
        VTask one = ivTaskService.getOne(new QueryWrapper<VTask>()
                .lambda()
                .eq(VTask::getTaskId, taskId));
        List<BdOaFormPrivilegeRelation> list = iBdOaFormPrivilegeRelationService.list(new QueryWrapper<BdOaFormPrivilegeRelation>()
                .lambda()
                .eq(BdOaFormPrivilegeRelation::getActivityId, one.getTaskName())
                .eq(BdOaFormPrivilegeRelation::getBusinessKey, one.getBusinessKey()));
        Map<String, String> map = new HashMap<String, String>();
        for (BdOaFormPrivilegeRelation pr : list) {
            map.put(pr.getFormElementName(), pr.getElementPrivilege());
        }
        return AjaxResult.success(map);
    }

}
