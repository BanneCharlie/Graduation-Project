package com.ruoyi.project.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.business.domain.BusinessReportExamine;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.business.service.BusinessRepExamService;
import com.ruoyi.project.business.service.BusinessRequestReportService;
import com.ruoyi.project.business.service.IReqContReviewService;
import com.ruoyi.project.business.vo.ReportVo;
import com.ruoyi.project.common.vo.ProcessResultVo;
import com.ruoyi.project.process.domian.*;
import com.ruoyi.project.process.service.*;
import com.ruoyi.project.process.vo.PieVo;
import com.ruoyi.project.process.vo.ProcessVo;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.workflow.domain.*;
import com.ruoyi.project.workflow.service.EngineFlowService;
import com.ruoyi.project.workflow.service.IEngineProcActInstService;
import com.ruoyi.project.workflow.vo.NextStepVo;
import com.ruoyi.project.workflow.vo.ProcRunVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@RestController
@RequestMapping("process")
public class ProcessController extends BaseController {

    // 日志
    private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);
    // STEP1：获取应用基本信息
    private static final String appId = "RssyWU0Oz26K2EGFukeu54";
    private static final String appKey = "5cW5v0QXNT9wD6RG7oXwz5";
    private static final String masterSecret = "JbfIcn7uBa9EKxSLJfrTlA";
    private static final String url = "http://api.getui.com/apiex.htm";
    @Resource
    private IProcSxpzService iProcSxpzService;
    @Resource
    private ISysDeptService iSysDeptService;
    @Resource
    private EngineFlowService engineFlowService;
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private IVTaskService ivTaskService;
    @Resource
    private IProcAdviceStrategyService iProcAdviceStrategyService;
    @Resource
    private IProcSignatureService iProcSignatureService;
    @Resource
    private IEngineProcActInstService iEngineProcActInstService;

    @Resource
    private BusinessRequestReportService businessRequestReportService;
    @Resource
    private BusinessRepExamService businessRepExamService;
    @Resource
    private SysUserReportAuthorService sysUserReportAuthorService;

    @Resource
    private IReqContReviewService iReqContReviewService;

    /**
     * ======================流程运行相关方法开始========================================
     * ==============================================
     */
    /**
     * 前往流程事项的方法(带条件)
     *
     * @return
     */
    @RequestMapping("go-process")
    public AjaxResult goProcess(@RequestParam(required = false, defaultValue = "") String sxId,
                                @RequestParam(required = false, defaultValue = "") String swId,
                                @RequestParam(required = false, defaultValue = "") String type,
                                @RequestParam(required = false, defaultValue = "") String swtofw,
                                @RequestParam(required = false, defaultValue = "") String deptNameString,
                                @RequestParam(required = false, defaultValue = "") String swtaskId,
                                @RequestParam(required = false, defaultValue = "") String fileRelationBeanId,
                                @RequestParam(required = false, defaultValue = "") String flag) {

        List<ProcSxpz> sxpzList = iProcSxpzService.getList(sxId, "0");
        ProcessVo processVo = new ProcessVo();
        processVo.setSxpzList(sxpzList);
        processVo.setType(type);
        processVo.setFileRelationBeanId(fileRelationBeanId);
        processVo.setFlag(flag);
        processVo.setSwtaskId(swtaskId);
        processVo.setSwId(swId);

        processVo.setSwtofw(swtofw);processVo.setSxId(sxId);

        logger.info("新增督办单：" + url);
        return AjaxResult.success(processVo);
    }

    /**
     * 01进入流程初始化
     *
     * @return
     */
    @RequestMapping("start-process")
    public AjaxResult startProcess(@RequestParam(required = false, defaultValue = "") String businessKey,
                                   @RequestParam(required = false, defaultValue = "") String ssot,
                                   @RequestParam(required = false, defaultValue = "") String processKey,
                                   @RequestParam(required = false, defaultValue = "") String reportType
    ) {
        Map<String, Object> retMap = new HashMap<>();
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        List<NextStepVo> list = engineFlowService.queryWorkflowNextStepsStartByProcDefId(processKey, user);
        logger.info("processKey=" + processKey);
/*        for (NextStepVo nextStepVo : list) {
            if (nextStepVo.getNextActName().contains("部门负责人") || "检验报告审核".equals(nextStepVo.getNextActName())) {
                List<EngineProcUserorg> organizeList = new ArrayList<>();
                List<EngineProcUserorg> uerList = nextStepVo.getStep();
                SysDept org = iSysDeptService.selectDeptById(user.getDeptId());
                List<EngineProcUserorg> checkReportExamineList = new ArrayList<>();
                for (EngineProcUserorg engineProcUserorg : uerList) {
                        // 过滤筛选出来的 参与人员  只显示本部门的审核人 --》 针对检验报告审核
                    if (org.getDeptName().contains(engineProcUserorg.getOrgName())){
                        if (StringUtils.isNotEmpty(reportType)) {
                            // 过滤掉本部门没有 审核当前报告类型权限的人员
                            SysUserReportAuthor currentUserAuthorInstance = sysUserReportAuthorService.getOne(
                                    new QueryWrapper<SysUserReportAuthor>()
                                            .lambda()
                                            .eq(SysUserReportAuthor::getUserId, engineProcUserorg.getUserId())
                            );
                            try {
                                if (currentUserAuthorInstance == null) {
                                    throw new Exception("请联系管理员 调整审核人员权限!");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                return null;
                            }
                            if (sysUserReportAuthorService.checkCurrentUserAuthor(currentUserAuthorInstance,reportType)) {
                                checkReportExamineList.add(engineProcUserorg);
                            }
                        }else{
                            organizeList.add(engineProcUserorg);
                        }
                    }
                }
                // 强制覆盖当前查询出来的人员
                if (StringUtils.isNotEmpty(reportType)) {
                    nextStepVo.setStep(checkReportExamineList);
                }
            }
        }*/
        EngineProcDef processDefinition = engineFlowService.getDefinitionByProcDefId(processKey, user);

        retMap.put("businessKey", businessKey);
        retMap.put("processKey", processKey);
        retMap.put("procTransList", list);
        retMap.put("ssot", ssot);
        retMap.put("processDefinitionEntity", processDefinition);
        retMap.put("processUrl", "/process-started");
        //return "process/process-started";
        return AjaxResult.success(retMap);
    }

    /**
     * 02启动并完成第一个流程节点并设置下一个节点的参与者
     *
     * @return
     */
    @RequestMapping(value = "first-run-process", produces = "application/json")
    public ProcessResultVo firstRunProcess(
            @RequestParam(required = false, defaultValue = "") String processKey,
            @RequestParam(required = false, defaultValue = "") String businessKey,
            @RequestParam(required = false, defaultValue = "") String chooseFlow,
            @RequestParam(required = false, defaultValue = "") String selectedUserIds,
            @RequestParam(required = false, defaultValue = "") String procTitleName,
            HttpServletRequest request) {
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        StringBuilder selectedUserNames = new StringBuilder();
        String[] idArr = selectedUserIds.split(",");
        for (String id : idArr) {
            SysUser byId = iSysUserService.selectUserById(Long.parseLong(id));
            selectedUserNames.append(byId.getNickName()).append(",");
        }
        procTitleName = procTitleName.replaceAll("(\r\n|\r|\n|\n\r)", "");//过滤标题空格
        ProcessResultVo resultVo = new ProcessResultVo();// 初始化回调VO
        try {

            //参与者选择
            List<SysUser> userList = new ArrayList<>();
            StringBuilder poneNumber = new StringBuilder();
            if (selectedUserIds != null) {
                String[] ids = selectedUserIds.split(",");
                for (String id : ids) {
                    SysUser u = iSysUserService.selectUserById(Long.parseLong(id));
                    poneNumber.append(u.getPhonenumber()).append(",");
                    userList.add(u);
                }
            }
            /**
             * 启动流程第一步并运行流程
             */
            ProcRunVo runVo = engineFlowService.startProcessByProcDefId(processKey, chooseFlow, userList, user, procTitleName, businessKey);
            //ResultEntity<WorkflowRequest> resultEntity = workFlowEngineService.saveOrUpdateWorkflow(workflowRequest,null,user);
            // 运行流程
            String procId = runVo.getProcInstId();
            EngineProcInst instance = engineFlowService.getProcInstByProcInstId(runVo.getProcInstId());
            if (runVo.getRe()) {
                if (!poneNumber.toString().equals("")) {
                    /*SMSDmeo smsDmeo1 = new SMSDmeo();
                    //发送短信接口
                    poneNumber = new StringBuilder(poneNumber.toString().endsWith(",") ? poneNumber.substring(0, poneNumber.length() - 1) : poneNumber.toString());
                    String msg = "【流程送达】提交的'"+instance.getFromTitle()+"'需要您的确认，请查阅。";
                    smsDmeo1.send(poneNumber.toString(),msg);*/
                }
            }
            String taskId = engineFlowService.getStartActByProcDefId(runVo.getEngineProcDef().getProcDefId()).getActDefId();

            System.out.println(procId + "第一次运行流程");


            // 设置回调VO参数
            resultVo.setProcessInstId(runVo.getProcInstId());
            resultVo.setBusinessKey(businessKey);
            resultVo.setCurrentActId(taskId);
            resultVo.setCurrentActName("开始步骤");
            resultVo.setProcessDefId(runVo.getEngineProcDef().getProcDefId());
            resultVo.setProcessKey(processKey);
            resultVo.setSelectedUserIds(selectedUserIds);
            resultVo.setSelectedUserNames(selectedUserNames.substring(0, selectedUserNames.length() - 1));
            resultVo.setUserActionType("0");
            resultVo.setBlfs("1");
            resultVo.setResult("success");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVo;
    }

    /**
     * 0当前用户全部待办流程
     *
     * @return
     * @author optimus
     */
    @RequestMapping("get-user-all-process")
    public TableDataInfo getUserAllProcess() {
        startPage();
        String userId = SecurityUtils.getUsername();
        QueryWrapper<VTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(VTask::getTaskStatus, "running")
                .eq(VTask::getUserId, userId)
                .orderByDesc(VTask::getCreateTime);
        List<VTask> list = ivTaskService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 进入运行的流程03
     *
     * @return
     * @author optimus
     */
    @RequestMapping("go-run-process")
    public AjaxResult goRunProcess(
            @RequestParam(required = false, defaultValue = "") String businessKey,
            @RequestParam(required = false, defaultValue = "") String taskId,
            @RequestParam(required = false, defaultValue = "") String processKey,
            @RequestParam(required = false, defaultValue = "") String processInstanceId,
            @RequestParam(required = false, defaultValue = "") String blfs,
            @RequestParam(required = false, defaultValue = "") String ssot) {
        Map<String, Object> retMap = new HashMap<>();
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        SysDept nowDept = iSysDeptService.selectDeptById(user.getDeptId());
        System.out.println("user:" + user.getUserName() + "|" + nowDept.getDeptName());

        //获取流程下一步queryWorkflowNextSteps
        List<NextStepVo> list = engineFlowService.queryWorkflowNextStepsByProcInstd(processInstanceId, processKey, user);
/*        for (NextStepVo nextStepVo : list) {
            if (nextStepVo.getNextActName().contains("部门负责人") || nextStepVo.getNextActName().contains("检验报告审核")) {
                List<EngineProcUserorg> organizeList = new ArrayList<>();
                List<EngineProcUserorg> uerList = nextStepVo.getStep();
                SysDept org = iSysDeptService.selectDeptById(user.getDeptId());
                List<EngineProcUserorg> checkReportExamineList = new ArrayList<>();
                for (EngineProcUserorg engineProcUserorg : uerList) {
                    if (org.getDeptName().contains(engineProcUserorg.getOrgName())){

                        // 过滤筛选出来的 参与人员  只显示本部门的审核人 --》 针对检验报告审核
                        if (nextStepVo.getNextActName().contains("检验报告审核")) {

                            BusinessReportExamine reportExamine = businessRepExamService.getOne(new QueryWrapper<BusinessReportExamine>()
                                    .lambda()
                                    .eq(BusinessReportExamine::getPiid, processInstanceId));

                            BusinessRequestReport businessRequestReport = businessRequestReportService.getById(reportExamine.getReportId());

                            String reportType = businessRequestReport.getTemplateCategory();

                            // 过滤掉本部门没有 审核当前报告类型权限的人员
                            SysUserReportAuthor currentUserAuthorInstance = sysUserReportAuthorService.getOne(
                                    new QueryWrapper<SysUserReportAuthor>()
                                            .lambda()
                                            .eq(SysUserReportAuthor::getUserId, engineProcUserorg.getUserId())
                            );
                            try {
                                if (currentUserAuthorInstance == null) {
                                    throw new Exception("请联系管理员 调整审核人员权限!");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                return null;
                            }
                            if (sysUserReportAuthorService.checkCurrentUserAuthor(currentUserAuthorInstance,reportType)) {
                                checkReportExamineList.add(engineProcUserorg);
                            }
                        }else{
                            organizeList.add(engineProcUserorg);
                        }
                    }
                }
                // 强制覆盖当前查询出来的人员
                if (nextStepVo.getNextActName().contains("检验报告审核")) {
                    nextStepVo.setStep(checkReportExamineList);
                }
            }
        }*/

        logger.info("processInstanceId=" + processInstanceId);
        EngineProcDef processDefinition = engineFlowService.getDefinitionByProcDefId(processKey, user);
        EngineProcInst instance = engineFlowService.getProcInstByProcInstId(processInstanceId);
        EngineProcWorkitem currentworkItem = engineFlowService.getWorkItemByProcInstId(processInstanceId, taskId);
        boolean isAddTask = false;//true为新增工作项
        String isadd = "0";

        List<EngineProcWorkitem> workItems = engineFlowService.getTaskUsersByProcInstId(processInstanceId);

        String status = currentworkItem.getEnStatus();
        int isOver = 0;//是否最后一步
        if (currentworkItem.getEnType().equals("end")) {
            isOver = 1;
        }
        String morePersonHandel = "0";
        if (workItems.size() > 1) {
            for (EngineProcWorkitem workItem : workItems) {
                for (EngineProcWorkitem engineProcWorkitem : workItems) {
                    if (workItem.getActInsName().equals(engineProcWorkitem.getActInsName())
                            && workItem.getEnStatus().equals(engineProcWorkitem.getEnStatus())) {
                        morePersonHandel = "1";
                        break;
                    }
                }
            }
        }


        retMap.put("processInstance", instance);
        retMap.put("morePersonHandel", morePersonHandel);
        retMap.put("procTransList", list);
        retMap.put("taskId", taskId);
        retMap.put("actid", currentworkItem.getActInsName());
        retMap.put("blfs", blfs);
        retMap.put("isadd", isadd);
        retMap.put("isOver", isOver);
        retMap.put("businessKey", businessKey);
        retMap.put("status", status);

        retMap.put("currentworkItemName", currentworkItem.getWorkItemUserName());
        retMap.put("processDefinitionEntity", processDefinition);
        retMap.put("ssot", ssot);
        retMap.put("userId", user.getUserName());
        return AjaxResult.success(retMap);
    }

    /**
     * 执行已经运行的流程
     *  执行已经开始的流程
     * @return
     * @author optimus
     */
    @RequestMapping(value = "run-process")
    public ProcessResultVo runProcess(
            @RequestParam(required = false, defaultValue = "") String processKey,
            @RequestParam(required = false, defaultValue = "") String businessKey,
            @RequestParam(required = false, defaultValue = "") String chooseFlow,
            @RequestParam(required = false, defaultValue = "") String taskId,
            @RequestParam(required = false, defaultValue = "1") String blfs,
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") String processInstanceId,
            @RequestParam(required = false, defaultValue = "") String addTaskNames,
            @RequestParam(required = false, defaultValue = "") String addTaskIds,
            @RequestParam(required = false, defaultValue = "") String selectedUserIds,
            @RequestParam(required = false, defaultValue = "") String selectedUserName,
            @RequestParam(required = false, defaultValue = "") String stepName) {
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());

        // 获取已选中的新增任务人员名
        String addTaskNamesTemp = "";
        String[] addTaskNamesAttr = addTaskNames.split(",");// 获取已选中的新增任务人员名
        // 获取已选择的人员名称
        // 对IE8下传flow2.name为中文的时候，乱码问题
        String[] selectedUserNameArr = selectedUserName.split(",");// 含有两个同名的参数，取被js编码的参数
        String selectedUserNames = "";
        if (StringUtils.isNotEmpty(addTaskNames)) {
            addTaskNamesTemp = addTaskNamesAttr[0].contains("%") ? addTaskNamesAttr[0]
                    : addTaskNamesAttr[1];// 获取已选择的人员名称
            try {
                addTaskNamesTemp = URLDecoder.decode(addTaskNamesTemp, "utf-8");
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        logger.info("chooseFlow:" + chooseFlow);
        if (chooseFlow.equals("newtask")) {// 如果选择了新增任务 则blfs=3
            blfs = "3";
        }

        EngineProcInst instance = engineFlowService.getProcInstByProcInstId(processInstanceId);

        System.out.println("stepName:" + stepName);

        Boolean re = true;
        //参与者选择
        List<SysUser> users = new ArrayList<>();
        StringBuilder poneNumber = new StringBuilder();
        if (StringUtils.isNotEmpty(selectedUserIds)) {
            String[] ids = selectedUserIds.split(",");
            for (String id : ids) {
                logger.info("ids:" + id);
                SysUser v = new SysUser();

                v = iSysUserService.selectUserById(Long.parseLong(id));
                poneNumber.append(v.getPhonenumber()).append(",");
                users.add(v);
            }

        }
        //运行流程下一步
        logger.info("进入流程下一步执行title");
        if (title != null && title != "") {
            title = title.replaceAll("(\r\n|\r|\n|\n\r)", "");//过滤
            logger.info("进入流程下一步执行");
        }
        /**
         * 运行流程
         */
        ProcRunVo runVo = engineFlowService.runProcessByProcInstId(processInstanceId, chooseFlow, users, user, title, businessKey);
        re = runVo.getRe();
        EngineProcInst processInstance = engineFlowService.getProcInstByProcInstId(processInstanceId);

        List<VTask> vTasklist = ivTaskService.list(new QueryWrapper<VTask>()
                .lambda()
                .eq(VTask::getTaskId, taskId)
                .eq(VTask::getUserId, user.getUserName()));
        if (vTasklist.size() > 0) {
            stepName = vTasklist.get(0).getTaskName();
        }
        if (stepName.equals("申请")) {
            stepName = "申请人修改";
        }
        ProcessResultVo resultVo = new ProcessResultVo();// 初始化回调VO
        if (chooseFlow.equals("完成任务")) {
            blfs = "4";
        }
        // 如果不是最后节点
        if (processInstance != null) {
            if (blfs.equals("3")) {
                resultVo.setProcessInstId(processInstanceId);
                resultVo.setBusinessKey(businessKey);
                resultVo.setCurrentActId(taskId);
                resultVo.setCurrentActName(stepName);
                resultVo.setProcessDefId(processInstance.getExt1());
                resultVo.setProcessKey(processKey);
                resultVo.setSelectedUserIds(addTaskIds);
                resultVo.setSelectedUserNames(addTaskNames);
                resultVo.setUserActionType("0");
                resultVo.setBlfs(blfs);
                if (instance.getCurrActId().equals("end")) {
                    resultVo.setCurrentActId("0");
                }
                if (re) {
                    resultVo.setResult("success");
                } else {
                    resultVo.setResult("false");
                }
            } else {
                resultVo.setProcessInstId(processInstanceId);
                resultVo.setBusinessKey(businessKey);
                resultVo.setCurrentActId(taskId);
                resultVo.setCurrentActName(stepName);
                resultVo.setProcessDefId(processInstance.getExt1());
                resultVo.setProcessKey(processKey);
                resultVo.setSelectedUserIds(selectedUserIds);
                resultVo.setSelectedUserNames(selectedUserName);
                resultVo.setUserActionType("0");
                resultVo.setBlfs(blfs);
                if (re) {
                    resultVo.setResult("success");
                } else {
                    resultVo.setResult("false");
                }
            }

        } else {// 如果是最后节点 processInstance不存在 需要重新获取数据
            resultVo.setProcessInstId(processInstanceId);
            resultVo.setBusinessKey(businessKey);

            resultVo.setCurrentActName("流程结束");
            resultVo.setProcessDefId("");
            resultVo.setProcessKey(processKey);
            resultVo.setSelectedUserIds(selectedUserIds);
            resultVo.setSelectedUserNames("办结归档环节");
            resultVo.setUserActionType("0");
            resultVo.setBlfs(blfs);
            if (re) {
                resultVo.setResult("success");
            } else {
                resultVo.setResult("false");
            }
        }
        return resultVo;
    }

    /**
     * 意见处理过程0009
     *
     * @param piid
     * @return
     */
    @GetMapping(value = "dispatch-advice-strategy-list")
    public AjaxResult showDispatchAdviceStrategyList(@RequestParam(value = "piid") String piid) {
        Map<String, Object> retMap = new HashMap<>();
        QueryWrapper<ProcAdviceStrategy> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProcAdviceStrategy::getPiid, piid)
                .orderByAsc(ProcAdviceStrategy::getCreateTime);
        List<ProcAdviceStrategy> adviceStragegyList = iProcAdviceStrategyService.list(queryWrapper);
        //parameterMaps.put("receiveTime","asc");
        //流程取出代办人员清单
        StringBuilder usernames = new StringBuilder();

        List<EngineProcWorkitem> engineProcWorkitemList = engineFlowService.getTaskUsersByProcInstId(piid);
        if (engineProcWorkitemList.size() > 0) {
            for (EngineProcWorkitem ew : engineProcWorkitemList) {
                usernames.append(ew.getWorkItemUserName()).append(",");
            }

        }
        retMap.put("adviceStrategyList", adviceStragegyList);
        if (StringUtils.isNotEmpty(usernames)) {
            retMap.put("usernames", usernames.substring(0, usernames.length() - 1));
        } else {
            retMap.put("usernames", "");
        }

        return AjaxResult.success(retMap);
    }

    /**
     * 当前用户全部历史流程
     * @return
     */
    @RequestMapping("get-user-all-history-process")
    public TableDataInfo getUserAllHistoryProcess() {
        startPage();
        String userId = SecurityUtils.getUsername();
        QueryWrapper<VTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(VTask::getTaskStatus, "finish")
                .eq(VTask::getUserId, userId)
                .orderByDesc(VTask::getCreateTime);
        List<VTask> list = ivTaskService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 获取表单的意见列表
     *
     * @param tableId
     * @param signControlName
     * @return
     */
    @GetMapping(value = "form-advice-list")
    public AjaxResult getSignMap(@RequestParam(required = false, defaultValue = "") String tableId,
                                 @RequestParam(required = false, defaultValue = "") String signControlName) {
        QueryWrapper<ProcSignature> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProcSignature::getTableId, tableId)
                .eq(ProcSignature::getControlName, signControlName)
                .orderByAsc(ProcSignature::getCreateTime);
        List<ProcSignature> signatureList = iProcSignatureService.list(queryWrapper);
        // 一个意见框对应多个意见
        List<ProcSignature> signList = new ArrayList<ProcSignature>();
        for (ProcSignature s : signatureList) {
            if (s.getControlName().equals(signControlName)) {
                signList.add(s);
            }
        }
        return AjaxResult.success(signList);
    }

    @RequestMapping(value = "push-back")
    public AjaxResult pushBack(@RequestParam(value = "enId") String enId) {
        String message = "撤回成功";
        try{
            message = engineFlowService.pushBack(enId);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("撤回流程失败原因："+e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success(message);
    }

    /**
     * 获取根据流程实例ID和流程活动节点定义ID获取创建人信息
     * @param piId 流程实例ID
     * @param aiId 流程活动节点定义ID
     * @return
     */
    @GetMapping("get-previous-user")
    public AjaxResult getPreviousUser(@RequestParam(value = "piId") String piId, @RequestParam("aiId") String aiId) {
        SysUser user = null;
        QueryWrapper<EngineProcActInst> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcActInst::getProcInstId, piId)
                .eq(EngineProcActInst::getActInsId, aiId)
                .eq(EngineProcActInst::getEnStatus, "running");
        List<EngineProcActInst> list = iEngineProcActInstService.list(queryWrapper);
        if (list != null && list.size() == 1) {
            user = iSysUserService.selectUserByUserName(list.get(0).getCreateUserId());
        }
        return AjaxResult.success(user);
    }

    /**
     * 获取柱状图数据
     * @return
     */
    @GetMapping("getBarChartData")
    public AjaxResult getBarChartData() {
        // 查询所有的处理的报告数目  合同数目
        List<ReqContReview> reqContReviewList = iReqContReviewService.getALlList();
        // 根据月份划分日期
        List<Integer> reqContReviewData = iReqContReviewService.getBarChartData(reqContReviewList);

        // 查询所有的检验报告数目
        List<BusinessReportExamine> businessReportExamineList = businessRepExamService.getALlList();
        // 根据月份划分日期
        List<Integer> businessReportExamineData = businessRepExamService.getBarChartData(businessReportExamineList);

        // 将所有的报告数目和合同数目合并
        List<Integer> charData = new ArrayList<>();
        for (int i = 0; i < reqContReviewData.size(); i++) {
            charData.add(reqContReviewData.get(i) + businessReportExamineData.get(i));
        }

        return AjaxResult.success(charData);
    }

    /**
     * 获取饼状图数据
     * @return
     */
    @GetMapping("getPieChartData")
    public AjaxResult getPieChartData() {
        // 查询所有需要质检的设备
        List<ReportVo> reportList = businessRequestReportService.getAllReportList();

        // 根据报告列表统计分类数据并返回饼图数据 {value: 数量, name: 设备名称} 格式
        Map<String, Object> pieChartData = businessRequestReportService.getPieChartData(reportList);

        return AjaxResult.success(pieChartData);
    }
}
