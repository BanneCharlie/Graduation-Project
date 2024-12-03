package com.ruoyi.project.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.business.domain.BusinessDevice;
import com.ruoyi.project.business.domain.BusinessRelationDeviceUser;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.business.service.BusinessRelationDeviceUserService;
import com.ruoyi.project.business.service.BusinessRequestReportService;
import com.ruoyi.project.business.service.IBusinessDeviceService;
import com.ruoyi.project.business.service.IReqContReviewService;
import com.ruoyi.project.business.vo.DeviceVo;
import com.ruoyi.project.business.vo.ReqCheckVo;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.template.domain.ReportTemplate;
import com.ruoyi.project.template.service.ReportTemplateService;
import com.ruoyi.project.workflow.domain.EngineProcActInst;
import com.ruoyi.project.workflow.service.IEngineProcActInstService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author niminui
 * @date 2021/6/28 9:59
 */
@RestController
@RequestMapping("device-task")
public class DeviceTaskController extends BaseController {

    @Resource
    private IBusinessDeviceService iBusinessDeviceService;
    @Resource
    private IReqContReviewService iReqContReviewService;
    @Resource
    private IEngineProcActInstService iEngineProcActInstService;
    @Resource
    private BusinessRequestReportService businessRequestReportService;
    @Resource
    private ReportTemplateService reportTemplateService;
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private BusinessRelationDeviceUserService businessRelationDeviceUserService;

    /**
     * --->  展示所有
     *             1. 当前部门
     *             2. 完成的合同评审流程的数据
     *        列表
     * @author xqh,  987072248@qq.com
     * @date 2021/7/27 11:29
     * @param
     */
    @GetMapping("contract-send")
    public TableDataInfo getContractFinishProcessAndSelfDeptData(ReqContReview reqContReview){
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();
        List<ReqContReview> thisDeptBusiness = iReqContReviewService.list(new QueryWrapper<ReqContReview>()
                .lambda()
                .eq(ReqContReview::getCreateDeptId, loginUser.getDeptId()));
        List<String> isContent = new ArrayList<>();
        for (ReqContReview deptBusiness : thisDeptBusiness) {
            List<EngineProcActInst> isEndList = iEngineProcActInstService.list(new QueryWrapper<EngineProcActInst>()
                    .lambda()
                    .eq(EngineProcActInst::getProcInstId, deptBusiness.getPiid())
                    .eq(EngineProcActInst::getEnStatus, "finish")
                    .eq(EngineProcActInst::getEnType, "end")
            );
            // 结束的流程结果集 不为 0 则结束
            if (!isEndList.isEmpty()){
                isContent.add(deptBusiness.getRowId());
            }
        }
        if (thisDeptBusiness.isEmpty() || isContent.isEmpty()){
            return getDataTable(new ArrayList<>());
        }
        String temp;
        startPage();
        return getDataTable(
                iReqContReviewService.list(new QueryWrapper<ReqContReview>()
                    .lambda()
                        .in(ReqContReview::getRowId,isContent)
                        .like(StringUtils.isNotEmpty(temp = reqContReview.getCreateUserName()),ReqContReview::getCreateUserName,temp)
                        .like(StringUtils.isNotEmpty(temp = reqContReview.getContractNumber()),ReqContReview::getContractNumber,temp)
                        .like(StringUtils.isNotEmpty(temp = reqContReview.getCreateDeptName()),ReqContReview::getCreateDeptName,temp)
                        .orderByDesc(ReqContReview::getCreateTime)
        ));
    }

    @GetMapping("getSendTaskUser")
    public TableDataInfo getSendTaskUser(
            @RequestParam("rowId") String rowId
    ){
        ReqContReview srcData = iReqContReviewService.getById(rowId);
        String checkUserId = srcData.getCheckUserId();
        startPage();
        if (StringUtils.isEmpty(checkUserId)){
            return getDataTable(new ArrayList<>());
        }
        String[] userIds;
        if (!checkUserId.contains(",")){
            userIds = new String[]{checkUserId};
        }else {
            userIds = checkUserId.split(",");
        }

        return getDataTable(iSysUserService.getUserByIds(userIds));
    }

    @GetMapping("getWaitSendTaskCount")
    public AjaxResult getSendTaskCount(){
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();
        List<ReqContReview> getAllList = iReqContReviewService.list(new QueryWrapper<ReqContReview>()
                .lambda()
                .eq(ReqContReview::getCreateDeptId, loginUser.getDeptId())
                .isNull(ReqContReview::getCheckUserId)
                .or()
                .eq(ReqContReview::getCheckUserId,"")
        );
        List<String> isRequireList = new ArrayList<>();
        for (ReqContReview deptBusiness : getAllList) {
            List<EngineProcActInst> isEndList = iEngineProcActInstService.list(new QueryWrapper<EngineProcActInst>()
                    .lambda()
                    .eq(EngineProcActInst::getProcInstId, deptBusiness.getPiid())
                    .eq(EngineProcActInst::getEnStatus, "finish")
                    .eq(EngineProcActInst::getEnType, "end")
            );
            if (isEndList.size() != 0){
                isRequireList.add(deptBusiness.getRowId());
            }
        }
        return AjaxResult.success(isRequireList.size());
    }

    /**
     * ---> 派发选择人员之后䣌确定操作
     * @author xqh, 987072248@qq.com
     * @date 2021/8/25 15:59
     * @param rowIds
     * @param userIds
     */
    @PostMapping("sendTaskRunning/{rowIds}/{userIds}")
    public AjaxResult sendTaskRunning(
            @PathVariable String[] rowIds,
            @PathVariable Long[] userIds
    ){
        if (rowIds.length == 1){
            ReqContReview srcObj = iReqContReviewService.getById(rowIds[0]);
            String checkUserId = srcObj.getCheckUserId();
            String userIdString = Arrays.toString(userIds).replace("[","").replaceAll("]","").replaceAll(" ","");
            if (StringUtils.isEmpty(checkUserId)){
                srcObj.setCheckUserId(userIdString);
            }else {
                srcObj.setCheckUserId(checkUserId+ "," + userIdString);
            }
            return toAjax(iReqContReviewService.updateById(srcObj));
        }else{
            logger.error("已抛弃的代码逻辑");
        }

        return AjaxResult.success();
    }

    /** 删除关联的用户数据
     * @author xqh, 987072248@qq.com
     * @date 2021/8/3 15:27
     * @param rowId
     * @param userIds
     */
    @DeleteMapping("deleteRelationUser/{rowId}/{userIds}")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteRelationUser(
            @PathVariable String rowId,
            @PathVariable String[] userIds){

        ReqContReview srcDataObj = iReqContReviewService.getById(rowId);
        String[] split = srcDataObj.getCheckUserId().split(",");
        int removeStep = split.length - userIds.length;
        if (removeStep == 0){   // remove all users
            srcDataObj.setCheckUserId("");
            return AjaxResult.success(iReqContReviewService.updateById(srcDataObj));
        }
        // @param : resultIds --> 表示经过删除之后的userIds 数组
        String[] resultIds = new String[removeStep];

        for (int i = 0 , step = 0 , maxLength = split.length; i < maxLength; i++) {
            for (int j = 0 , maxUserIdLength = userIds.length; j < maxUserIdLength; j++) {
                // not equals and this Array current Item is Last params
                if (split[i].equals(userIds[j])){
                    break;
                }else {
                    if (j == maxUserIdLength - 1){
                        resultIds[step++] = split[i];
                    }
                }
            }
            // update application in runtimer
            // 当前步骤==需要删除的步骤 结束循环
            if (step == removeStep) {
                break;
            }
        }
        srcDataObj.setCheckUserId(Arrays.toString(resultIds).replace("[","").replaceAll("]","").replaceAll(" ",""));

        return toAjax(iReqContReviewService.updateById(srcDataObj));
    }

    /**
     * 获取任务池所有任务任务
     *
     * @update: 2022-4-21 不能包含已经进行任务流程的数据
     * @param deviceVo 查询参数
     */
    @GetMapping(value = "task-receive")
    public TableDataInfo getTaskReceive(DeviceVo deviceVo) {
//        startPage();
        List<DeviceVo> list = iBusinessDeviceService.getTaskReceive(deviceVo);
        return getDataTable(list);
    }

    /**
     * 获取待领取任务总数
     * @return
     */
    @GetMapping(value = "task-receive-count")
    public AjaxResult getTaskReceiveCount() {
        return AjaxResult.success(iBusinessDeviceService.getTaskReceiveCount());
    }

    /**
     * 根据rowIds领取任务
     * @param rowIds 设备ids
     * @return
     */
    @PostMapping("receive-task/{rowIds}")
    public AjaxResult receiveTask(@PathVariable String rowIds) {
        return AjaxResult.success(iBusinessDeviceService.receiveTask(rowIds.split(",")));
    }

    /**
     * ---> 获取当前任务的所有关联人员
     * @param deviceId
     * @author xqh, 987072248@qq.com
     * @date 2021/8/9 15:44
     * @param
     */
    @GetMapping("receive-relation-list")
    public TableDataInfo listRelationUsers(@RequestParam("deviceId") String deviceId){
        startPage();
        return getDataTable(
                businessRelationDeviceUserService.list(new QueryWrapper<BusinessRelationDeviceUser>()
                    .lambda()
                        .eq(BusinessRelationDeviceUser::getDeviceId,deviceId))
        );
    }
    /**
     * ---> 领取任务的人 可以对当前任务进行关联人员 , 最终都可以看到当前任务的报告
     * @author xqh, 987072248@qq.com
     * @date 2021/8/9 15:32
     * @param deviceId  当前设备Id
     * @param userIds   关联的 user
     */
    @PostMapping("receive-relation-user/{deviceId}/{userIds}")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult relationUsers(
            @PathVariable String deviceId,
            @PathVariable Long[] userIds){

        BusinessDevice deviceBean = iBusinessDeviceService.getById(deviceId);
        for (Long userId : userIds) {
            SysUser itemUser = iSysUserService.selectUserById(userId);
            BusinessRelationDeviceUser operateBean = new BusinessRelationDeviceUser();
            operateBean.setDeviceId(deviceId);
            operateBean.setContractId(deviceBean.getContractId());
            operateBean.setUserId(String.valueOf(itemUser.getUserId()));
            operateBean.setUserPhone(itemUser.getPhonenumber());
            operateBean.setUserName(itemUser.getNickName());
            operateBean.setUserDeptName(itemUser.getDept().getDeptName());
            businessRelationDeviceUserService.save(operateBean);
        }
        return AjaxResult.success();
    }
    /**
     * ---> 根据关系user id 表来删除 关联关系
       ---> 删除关联的人员 ， 需要根据后来添加的业务 是否已经生成报告来判断
     * @author xqh, 987072248@qq.com
     * @date 2021/8/9 15:55
     * @param rowIds    需要删除的关系记录  rowIds
     */
    @DeleteMapping("receive-relation-delete/{rowIds}")
    public AjaxResult relationDeleteUsers(@PathVariable String[] rowIds){
        for (String rowId : rowIds) {
            if (businessRelationDeviceUserService.selectChangeUserIsGenericReport(rowId)){
                return AjaxResult.error("您选中的人员中存在已经生成报告的人员，不能取消关联!",false);
            }
        }
        for (String rowId : rowIds) {
            if (! businessRelationDeviceUserService.removeById(rowId)) {
                return AjaxResult.error();
            }
        }
        return AjaxResult.success(true);
    }

    /**
     * 根据rowIds归还任务
     * @param rowIds
     * @return
     */
    @PostMapping("revert-task/{rowIds}")
    public AjaxResult revertTask(@PathVariable String rowIds) {
        return AjaxResult.success(iBusinessDeviceService.revertTask(rowIds.split(",")));
    }

    /**
     * 查询待处理任务 is_read = 1
     * @param deviceVo 查询参数
     * @return
     */
    @RequestMapping(value = "task-pending")
    public TableDataInfo getTaskPending(DeviceVo deviceVo) {
        startPage();
        List<DeviceVo> list = iBusinessDeviceService.getTaskPending(deviceVo);
        return getDataTable(list);
    }

    /**
     * 查询已处理任务 is_read = 0
     * @param reqCheckVo 查询参数
     * @return
     */
    @RequestMapping(value = "task-processed")
    public TableDataInfo getTasksProcessed(ReqCheckVo reqCheckVo) {
        startPage();
        List<ReqCheckVo> list = iBusinessDeviceService.getTasksProcessed(reqCheckVo);
        return getDataTable(list);
    }

//    --------------------------------------- 我的报告 -------------------------
    /**
     * ---> 获取当前登录用户的所有报告
     * @author xqh, 987072248@qq.com
     * @date 2021/8/5 15:09
     */
    @GetMapping("mime-report-list")
    public TableDataInfo getMimeReportList(@RequestParam Map<String, String> queryParams){
        startPage();
        return getDataTable(businessRequestReportService.getMimeReportVo(queryParams));
    }

    @GetMapping("reportInstance/{rowId}")
    public AjaxResult getReportInstance(@PathVariable String rowId){
        return AjaxResult.success(businessRequestReportService.getById(rowId));
    }

    @PostMapping("replaceReportContext")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult replaceReportContext(
            @RequestParam("reportId") String reportId,
            @RequestParam("targetContext") String targetContext,
            @RequestParam("replaceContext") String replaceContext){
        boolean flag = true;
        try {
            BusinessRequestReport reportBean = businessRequestReportService.getById(reportId);
            String srcGenericContext = reportBean.getReportGenericContext();
            if (StringUtils.isNotEmpty(srcGenericContext)){
                reportBean.setReportGenericContext(srcGenericContext.replace(targetContext,replaceContext));
                businessRequestReportService.updateById(reportBean);
            }

        }catch (Exception e){
            e.printStackTrace();
            flag = false;
        }

        return toAjax(flag);
    }
    /**
     * ---> 选择完模板并确认之后 就会生成一份报告书 ， 但是 is_generic_report 为 0  是未生成的 ， 在点击生成报告之后才会被关联人员看到
     * ---> 或者是 生成报告
     * @author xqh, 987072248@qq.com
     * @date 2021/8/6 9:51
     * @param
     */
    @PostMapping("operate-report")
    public AjaxResult saveCheckReport(
        @RequestParam(value = "rowId" , defaultValue = "") String rowId,
        @RequestParam(value = "deviceId" , defaultValue = "") String deviceId,
        @RequestParam(value = "contractRowId" , defaultValue = "") String contractRowId,
        @RequestParam(value = "templateId" , defaultValue = "") String templateId
    ){
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();
        BusinessDevice deviceBean = iBusinessDeviceService.getById(deviceId);
        if (StringUtils.isEmpty(rowId)){ // generic report operate
            ReportTemplate srcReportTemplate = reportTemplateService.getById(templateId);
            BusinessRequestReport genericReport = new BusinessRequestReport();
            genericReport.setBusinessContractReviewId(contractRowId);
            genericReport.setBusinessDeviceId(deviceId);
            genericReport.setBusinessDeviceNumber(deviceBean.getDeviceNumber());
            genericReport.setCreateUserId(loginUser.getUserName());
            genericReport.setCreateUserName(loginUser.getNickName());
            genericReport.setCreateTime(new Date());
            genericReport.setUpdateTime(new Date());
            genericReport.setTemplateId(templateId);
            genericReport.setTemplateName(srcReportTemplate.getTemplateName());
            genericReport.setTemplateCategory(srcReportTemplate.getTemplateCategory());
            genericReport.setIsGenericReport("0");
            genericReport.setIsPrint("0");      // 没有发起审核流程不能进行打印
            genericReport.setIsStartFlow("0");  // 没有开始流程
            genericReport.setIsResolve("0");    // 没有解析过信息
            genericReport.setIsReset("0");      // 还未审批不需要重置
            genericReport.setReportGenericContext(srcReportTemplate.getTemplateGenericContent());
            genericReport.setReportTemplateContext(srcReportTemplate.getTemplateContent());
            return AjaxResult.success("操作成功",businessRequestReportService.save(genericReport) ? genericReport.getRowId() : null);
        }
        BusinessRequestReport srcGenericReport = businessRequestReportService.getById(rowId);
        srcGenericReport.setIsGenericReport("1");
        srcGenericReport.setUpdateTime(new Date());
        srcGenericReport.setReportGenericContext(businessRequestReportService.changeReportGenericContextByDeviceNumber(srcGenericReport));
        deviceBean.setIsRead(0);
        deviceBean.setIsGenericReport(0);
        iBusinessDeviceService.updateById(deviceBean);
        businessRequestReportService.updateById(srcGenericReport);
        return AjaxResult.success();
    }

    /**
     * ---> 保存检验报告模板
     * @author xqh, 987072248@qq.com
     * @date 2021/8/7 20:49
     * @param paramsMap
     */
    @PostMapping("save-report-template")
    public AjaxResult saveReportTemplate(
            @RequestBody Map<String, Object> paramsMap
    ){
        String reportContent = (String) paramsMap.get("reportContent");
        String reportId = (String) paramsMap.get("reportId");
        BusinessRequestReport srcBean = businessRequestReportService.getById(reportId);
        if (StringUtils.isNotEmpty(reportContent)){
            srcBean.setReportTemplateContext(reportContent);
        }
        String reportGeneric = (String) paramsMap.get("reportGeneric");
        srcBean.setReportGenericContext(reportGeneric);
        boolean result = businessRequestReportService.updateById(srcBean);
        if (!result) {
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

}
