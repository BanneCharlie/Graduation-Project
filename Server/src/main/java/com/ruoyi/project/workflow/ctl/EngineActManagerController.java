package com.ruoyi.project.workflow.ctl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.workflow.domain.EngineProcActDef;
import com.ruoyi.project.workflow.domain.EngineProcActDefRelation;
import com.ruoyi.project.workflow.service.IEngineProcActDefRelationService;
import com.ruoyi.project.workflow.service.IEngineProcActDefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 引擎后台管理控制器-活动节点模板
 *
 * @author optimus
 * @version 1.0
 */
@RestController
@RequestMapping("engine-act")
public class EngineActManagerController extends BaseController {

    // 日志
    private static final Logger logger = LoggerFactory.getLogger(EngineActManagerController.class);
    @Resource
    private IEngineProcActDefService iEngineProcActDefService;
    @Resource
    private IEngineProcActDefRelationService iEngineProcActDefRelationService;

    /**
     * 01进入节点模板管理
     * @return
     */
    @RequestMapping("proc-def-act-list")
    public TableDataInfo goProcessMgt(EngineProcActDef actDef) {
        startPage();
        List<EngineProcActDef> list = iEngineProcActDefService.selectProcActList(actDef);
        return getDataTable(list);
    }

    /**
     * 02根据流程节点编号获取详细信息
     */
    @GetMapping(value = "get-proc-def-act/{enId}")
    public AjaxResult getInfo(@PathVariable String enId)
    {
        return AjaxResult.success(iEngineProcActDefService.selectProcActById(enId));
    }
    /**
     * ---> 删除流程节点
     * @author xqh, 987072248@qq.com
     * @date 2021/7/22 14:05
     * @param enIds 需删除流程节点 ids数组
     */
    @DeleteMapping("delete/{enIds}")
    public AjaxResult deleteNode(@PathVariable String[] enIds){
        return toAjax(iEngineProcActDefService.deleteProcActDefNodeByIds(enIds));
    }
    /**
     * 03.5 删除下一步流程节点
     */
    @Log(title = "流程模板管理", businessType = BusinessType.DELETE)
    @DeleteMapping("delete/next/{enIds}")
    public AjaxResult remove(@PathVariable String[] enIds)
    {
        return toAjax(iEngineProcActDefService.deleteProcActByIds(enIds));
    }



    /**
     * 04流程定义保存/更新
     *
     * @param actDef
     * @return
     */
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "act-def-save")
    public AjaxResult save(@RequestBody EngineProcActDef actDef) {
        EngineProcActDef dest = null;
        String id = actDef.getEnId();
        boolean isTrue = false;

        if (id != null && id.length() > 0) {
            dest = iEngineProcActDefService.getById(id);
            BeanUtils.copyBeanProp(dest, actDef);
            dest.setUpdateTime(new Date());
            dest.setModifyUserId(SecurityUtils.getUsername());
            isTrue = iEngineProcActDefService.updateById(dest);
        } else {
            dest = actDef;
            dest.setEnId(null);
            dest.setCreateTime(new Date());
            dest.setCreateUserId(SecurityUtils.getUsername());
            dest.setEnStatus("nomal");
            isTrue = iEngineProcActDefService.save(dest);
        }
        return toAjax(isTrue);
    }

    /**
     * 前往节点下一步连线界面
     * @return
     */
    @RequestMapping("act-def-next")
    public TableDataInfo goNextPage(EngineProcActDef actDef) {
        startPage();
        List<EngineProcActDefRelation> list = iEngineProcActDefRelationService
                .getEngineProcActDefRelationList(actDef.getProcDefId(), actDef.getActDefId());
        return getDataTable(list);
    }

    /**
     * 选择关联连线页面
     * @return
     */
    @RequestMapping("act-line-lookup")
    public TableDataInfo lineLookup(@RequestParam("procDefId") String ProcDefId) {
        startPage();
        List<EngineProcActDef> list = iEngineProcActDefService.selectListByProcDefId(ProcDefId);
        return getDataTable(list);
    }

    /**
     * 节点关联连线的保存
     *
     * @return
     */
    @RequestMapping("act-saveLineToActs")
    public AjaxResult saveLineToAct(@RequestBody Map<String, Object> map) {
        String actId = StringUtils.getString(map.get("actId"));
        String procDefId = StringUtils.getString(map.get("procDefId"));
        String actDefId = StringUtils.getString(map.get("actDefId"));
        logger.info("out-actId=" + actId);
        logger.info("out-procDefId=" + procDefId);
        String[] actDefIdGroup = actDefId.split(",");
        List<String> uIdList = new ArrayList<String>();
        for (String id : actDefIdGroup) {// 去重复的值
            if (!uIdList.contains(id)) {
                uIdList.add(id);
            }
        }
        for (String enId : uIdList) {
            if (enId != null && actId != null) {
                EngineProcActDef engine = iEngineProcActDefService.getById(enId);
                EngineProcActDefRelation engineProcActDefRelation = new EngineProcActDefRelation();
                engineProcActDefRelation.setActDefRelId(actId);
                engineProcActDefRelation.setCreateTime(new Date());
                engineProcActDefRelation.setProcDefId(procDefId);
                engineProcActDefRelation.setActDefId(engine.getActDefId());
                engineProcActDefRelation.setActName(engine.getActName());
                // 设置默认连线名称为其节点名称
                engineProcActDefRelation.setNodeLineName(engine.getActName());
                iEngineProcActDefRelationService.save(engineProcActDefRelation);
            }
        }
        return toAjax(true);

    }

    /**
     * 改变节点间的连线名称
     * @param engineProcActDefRelation
     * @return
     */
    @PutMapping("update-node-line-name")
    public AjaxResult updateNodeLineName(@RequestBody EngineProcActDefRelation engineProcActDefRelation){
        return AjaxResult.success(iEngineProcActDefRelationService.updateById(engineProcActDefRelation));
    }


}
