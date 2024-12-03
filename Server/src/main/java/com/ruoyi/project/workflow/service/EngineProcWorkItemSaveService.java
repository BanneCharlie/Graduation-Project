package com.ruoyi.project.workflow.service;

import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.template.util.StringUtils;
import com.ruoyi.project.workflow.domain.EngineProcActInst;
import com.ruoyi.project.workflow.domain.EngineProcInst;
import com.ruoyi.project.workflow.domain.EngineProcWorkitem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_27 _ 16:03__星期四
 * @package: com.ruoyi.project.workflow.service
 * @JDK-Version : 1.8
 */
@Component
public class EngineProcWorkItemSaveService {

    @Autowired
    private IEngineProcInstService iEngineProcInstService;
    @Autowired
    private IEngineProcActInstService iEngineProcActInstService;
    @Autowired
    private TokenService tokenService;


    public EngineProcWorkitem engineProcWorkitem(
            String procInstId , String actInsId ,
            SysUser currentInsertUser,
            EngineProcActInst engineProcActInst,
            EngineProcInst engineProcInst) throws Exception , NullPointerException{

        final SysUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();

        if (engineProcActInst == null || engineProcInst == null) {
            throw new NullPointerException();
        }
        EngineProcWorkitem engineProcWorkitem = new EngineProcWorkitem();

        try {

            engineProcWorkitem.setFromTitle(
                    StringUtils.isEmpty(engineProcInst.getFromTitle()) ? "" : engineProcInst.getFromTitle());
            engineProcWorkitem.setFromType(
                    StringUtils.isEmpty(engineProcInst.getFromType()) ? "" : engineProcInst.getFromType());
            engineProcWorkitem.setGiveUserId(
                    StringUtils.isEmpty(loginUser.getUserName()) ? "" : loginUser.getUserName());
            engineProcWorkitem.setGiveUserName(
                    StringUtils.isEmpty(loginUser.getNickName()) ? "" : loginUser.getNickName());
            engineProcWorkitem.setUserId(
                    StringUtils.isEmpty(currentInsertUser.getUserName()) ? "" : currentInsertUser.getUserName());
            engineProcWorkitem.setWorkItemUserName(
                    StringUtils.isEmpty(currentInsertUser.getNickName()) ? "" : currentInsertUser.getNickName());
            engineProcWorkitem.setOrgId(
                    StringUtils.isEmpty(String.valueOf(currentInsertUser.getDeptId())) ? "" : String.valueOf(currentInsertUser.getDeptId()));
            engineProcWorkitem.setOrgName(
                    StringUtils.isEmpty(currentInsertUser.getDept().getDeptName()) ? "" : currentInsertUser.getDept().getDeptName());
            engineProcWorkitem.setCreateTime(engineProcActInst.getCreateTime());
            engineProcWorkitem.setUpdateTime(engineProcActInst.getUpdateTime());
            engineProcWorkitem.setEnStatus(
                    StringUtils.isEmpty(engineProcActInst.getEnStatus()) ? "" : engineProcActInst.getEnStatus());
            engineProcWorkitem.setModifyUserId(engineProcActInst.getModifyUserId());
            engineProcWorkitem.setCreateUserId(
                    StringUtils.isEmpty(engineProcActInst.getCreateUserId()) ? "" : engineProcActInst.getCreateUserId());
            engineProcWorkitem.setEnType(engineProcActInst.getEnType());
            engineProcWorkitem.setActInsName(engineProcActInst.getActName());
            engineProcWorkitem.setActInsId(actInsId);
            engineProcWorkitem.setProcInstId(procInstId);
            engineProcWorkitem.setProcDefId(engineProcActInst.getProcDefId());
            engineProcWorkitem.setProcDefName(engineProcActInst.getProcDefName());

        }catch (Exception e){
            throw new Exception();
        }


        return engineProcWorkitem;
    }
}
