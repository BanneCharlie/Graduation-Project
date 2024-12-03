package com.ruoyi.project.workflow.ctl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.workflow.domain.EngineProcUserorg;
import com.ruoyi.project.workflow.service.IEngineProcActDefService;
import com.ruoyi.project.workflow.service.IEngineProcUserorgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 引擎后台管理控制器-用户模板
 *
 * @author optimus
 * @version 1.0
 */
@RestController
@RequestMapping("engine-act-user")
public class EngineActUserManagerController extends BaseController {
    // 日志
    private static Logger logger = LoggerFactory.getLogger(EngineActUserManagerController.class);
    @Resource
    private IEngineProcActDefService iEngineProcActDefService;
    @Resource
    private IEngineProcUserorgService iEngineProcUserorgService;
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private ISysDeptService iSysDeptService;


    /**
     * 前往节点关联用户界面
     * @return
     */
    @RequestMapping("act-user-list")
    public TableDataInfo getUserList(EngineProcUserorg proc) {
        startPage();
        List<EngineProcUserorg> list = iEngineProcUserorgService.getEngineProcUserOrgListQuery(proc);
        return getDataTable(list);
    }

    /**
     * 选择关联用户页面
     * @return
     */
    @RequestMapping("act-line-lookup")
    public TableDataInfo lineLookup() {
        List<SysUser> list = iSysUserService.getAllUsers();
        return getDataTable(list);
    }

    /**
     * 节点关联连线的保存
     *
     * @return
     */
    @RequestMapping("act-saveUserToActs")
    public AjaxResult saveLineToAct(@RequestBody Map<String, Object> map) {
        String actId = StringUtils.getString(map.get("actId"));
        String procDefId = StringUtils.getString(map.get("procDefId"));
        String userIds = StringUtils.getString(map.get("userIds"));
        String[] userIdGroup = userIds.split(",");
        List<String> uIdList = new ArrayList<String>();
        for (String id : userIdGroup) {// 去重复的值
            if (!uIdList.contains(id)) {
                uIdList.add(id);
            }
        }
        for (String userId : uIdList) {
            if (userId != null && actId != null) {
                EngineProcUserorg engineProcUserorg = new EngineProcUserorg();
                engineProcUserorg.setActDefId(actId);
                engineProcUserorg.setCreateTime(new Date());
                engineProcUserorg.setProcDefId(procDefId);
                engineProcUserorg.setUserId(userId);
                SysUser user = iSysUserService.selectUserById(Long.parseLong(userId));
                engineProcUserorg.setUserName(user.getNickName());
                engineProcUserorg.setOrgName(iSysDeptService.selectDeptById(user.getDeptId()).getDeptName());
                iEngineProcUserorgService.save(engineProcUserorg);
            }
        }
        return toAjax(true);
    }

    /**
     * 节点关联用户的删除
     */
    @Log(title = "流程模板管理", businessType = BusinessType.DELETE)
    @DeleteMapping("delete/{userIds}")
    public AjaxResult remove(@PathVariable String[] userIds)
    {
        return toAjax(iEngineProcUserorgService.deleteActUserByIds(userIds));
    }

}
