package com.ruoyi.project.common.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.business.domain.*;
import com.ruoyi.project.business.service.*;
import com.ruoyi.project.common.vo.ProcessUrlVo;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author niminui
 * @date 2021/5/27 9:04
 */
@RestController
@RequestMapping("template")
public class TemplateController {

    @Resource
    private IReqContReviewService iReqContReviewService;
    @Resource
    private IReqRepManageService iReqRepManageService;
    @Resource
    private IProContReviewService iProContReviewService;
    @Resource
    private IProRepManageService iProRepManageService;
    @Resource
    private IBusinessDeviceService iBusinessDeviceService;
    @Resource
    private BusinessRepExamService businessRepExamService;

    private TokenService tokenService;
    @Autowired
    private TemplateController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("go-template")
    public AjaxResult goTemplate(@RequestBody Map<String, Object> param, HttpServletRequest request) {
        SysUser currentLoginUser = tokenService.getLoginUser(request).getUser();
        //拿出所有参数
        String taskId = StringUtils.getString(param.get("taskId"));
        String templateType = StringUtils.getString(param.get("templateType"));
        String processKey = StringUtils.getString(param.get("processKey"));
        String businessKey = StringUtils.getString(param.get("businessKey"));
        String processInstId = StringUtils.getString(param.get("processInstId"));
        String sxpzId = StringUtils.getString(param.get("sxpzId"));
        String type = StringUtils.getString(param.get("type"));
        String blfs = StringUtils.getString(param.get("blfs"));
        String pageSize = StringUtils.getString(param.get("pageSize"));
        String pageCurrent = StringUtils.getString(param.get("pageCurrent"));
        String totalCount = StringUtils.getString(param.get("totalCount"));
        String ssot = StringUtils.getString(param.get("ssot"));
        String toDoType = StringUtils.getString(param.get("toDoType"));
        String swtofw = StringUtils.getString(param.get("swtofw"));
        String swId = StringUtils.getString(param.get("swId"));
        String jhid = StringUtils.getString(param.get("jhid"));
        String flag = StringUtils.getString(param.get("flag"));
        String fileRelationBeanId = StringUtils.getString(param.get("fileRelationBeanId"));
        String fjId = StringUtils.getString(param.get("fjId"));
        String types = StringUtils.getString(param.get("types"));
        String dwOrgId = StringUtils.getString(param.get("dwOrgId"));
        String swtaskId = StringUtils.getString(param.get("swtaskId"));
        String realtionBusinessKey = StringUtils.getString(param.get("realtionBusinessKey"));
        String dbid = StringUtils.getString(param.get("dbid"));
        String currentName = StringUtils.getString(param.get("currentName"));
        String pkType = StringUtils.getString(param.get("pkType"));
        String contractId = StringUtils.getString(param.get("contractId"));
        String deviceRowId = StringUtils.getString(param.get("deviceRowId"));
        // report examine
        String reportType = StringUtils.getString(param.get("reportType"));
        String reportId = StringUtils.getString(param.get("reportId"));

        // 嵌套页面初始化参数
        String formUrl = "";// 业务表单
        String fileUrl = "";// 附件表单
        String adviceUrl = "";// 意见表单
        String processUrl = "";// 流程表单
        String operateUrl = "";// 按钮表单
        String redirectUrl = "";// 提交后跳转URL
        String fileRelationUrl = "";// 文件关联跳转URL
        String wordUrl = "";// 阅知过程跳转URL
        String sendReadUrl = "";// 阅知过程跳转URL
        //流程开始页面
        if (StringUtils.equals(templateType, "start")) {
            final String PROCESS_URL = "/process/startProcess?" + "processKey=" + processKey + "&businessKey=" + businessKey;
            final String OPERATE_URL = "/common/operate";
            // 附件路由地址 21 - 6 -3 这个值在启动流程时 就已经固定了
            final String FILE_URL = "/common/attachment?processType=start&createUserNickName=" + currentLoginUser.getNickName();

            // 检验报告审核 流程 start
            if (businessKey.equals("key-report-examine")){
                processUrl = PROCESS_URL + "&reportType=" + reportType + "&reportId=" + reportId;
                formUrl = "/core/reportExamine/start?configId=" + sxpzId + "&swId=" + swId + "" +
                        "&processKey=" + processKey + "&dwOrgId=" + dwOrgId + "&flag=0";  // 表单所在路由地址
                operateUrl = OPERATE_URL;// 按钮表单
            }
            // 委托检验 -- 合同评审申请
            else if (businessKey.equals("key-contract-review")) {//委托合同评审申请
                processUrl = PROCESS_URL + "&reportType=" + reportType + "&reportId=" + reportId;
                formUrl = "/core/requestCheckWorkManager/ReqContReview/start?configId=" + sxpzId + "&swId=" + swId + "" +
                        "&processKey=" + processKey + "&dwOrgId=" + dwOrgId + "&flag=0";  // 表单所在路由地址
                fileUrl = FILE_URL  + "&category=0" ;
                operateUrl = OPERATE_URL;// 按钮表单
                fileRelationUrl = "/file-relation/list.do?piid=" + processInstId + "&docType=sw";
                // 委托检验 -- 检验报告申请
            } else if (businessKey.equals("key-report-manage")) {//委托检验报告管理
                processUrl = PROCESS_URL;
                formUrl = "/core/requestCheckWorkManager/ReqRepManage/start?configId=" + sxpzId + "&swId=" + swId + "" +
                        "&processKey=" + processKey + "&dwOrgId=" + dwOrgId + "&flag=0";  // 表单所在路由地址
                fileUrl = FILE_URL + "&category=1";
                operateUrl = OPERATE_URL;// 按钮表单
                fileRelationUrl = "/file-relation/list.do?piid=" + processInstId + "&docType=sw";
                //如果是从合同检验跟踪快速进入流程的，表单参数需要加上合同编号
                if (StringUtils.isNotEmpty(contractId)) {
                    formUrl += "&contractId=" + contractId;
                }
                if (StringUtils.isNotEmpty(deviceRowId)) {
                    formUrl += "&deviceRowId=" + deviceRowId;
                }

            } else if (businessKey.equals("key-pro-contract-review")) {//产品合同评审申请
                processUrl = PROCESS_URL;
                formUrl = "/core/productCenterWorkManager/cotractReview/start?configId=" + sxpzId + "&swId=" + swId + "" +
                        "&processKey=" + processKey + "&dwOrgId=" + dwOrgId + "&flag=0";  // 表单所在路由地址
                fileUrl = FILE_URL  + "&category=2";
                operateUrl = OPERATE_URL;// 按钮表单
            } else if (businessKey.equals("key-pro-report-manage")){
                processUrl = PROCESS_URL;
                formUrl = "/core/productCenterWorkManager/checkReport/start?configId=" + sxpzId + "&swId=" + swId + "" +
                        "&processKey=" + processKey + "&dwOrgId=" + dwOrgId + "&flag=0";  // 表单所在路由地址
                fileUrl = FILE_URL + "&category=3";
                operateUrl = OPERATE_URL;// 按钮表单
            }
        } else if (StringUtils.equals(templateType, "update")) { //流程更新页面
            final String FILE_URL = "/common/attachment?processType=update&createUserNickName=" + currentLoginUser.getNickName();
            if ((businessKey.equals("key-report-examine"))){
                BusinessReportExamine applyBean = businessRepExamService.getOne(new QueryWrapper<BusinessReportExamine>()
                        .lambda()
                        .eq(BusinessReportExamine::getPiid, processInstId));
                processUrl = "/process/processRuned?";
                processUrl = processUrl + "&processKey=" + processKey
                        + "&taskId=" + taskId + "&businessKey=" + businessKey
                        + "&processInstanceId=" + processInstId + "&ssot=" + ssot
                        + "&reportType=" + applyBean.getReportCategory() ;
                formUrl = "/core/reportExamine/update?piid=" + processInstId + "&rowId=" + applyBean.getRowId() + "&opt=update&taskId=" + taskId;
                operateUrl = "/common/operate";// 按钮表单
            }
            else if (businessKey.equals("key-contract-review")) {//合同评审申请
                ReqContReview apply = iReqContReviewService.getOne(new QueryWrapper<ReqContReview>()
                        .lambda()
                        .eq(ReqContReview::getPiid, processInstId));
                processUrl = "/process/processRuned?";
                processUrl = processUrl + "processKey=" + processKey
                        + "&taskId=" + taskId + "&businessKey=" + businessKey
                        + "&processInstanceId=" + processInstId + "&ssot=" + ssot;
                formUrl = "/core/requestCheckWorkManager/ReqContReview/update?piid="
                        + processInstId + "&rowId=" + apply.getRowId() + "&opt=update&taskId=" + taskId;
                /*adviceUrl = "/dispatch/dispatch-advice-strategy-list.do?piid="
                    + processInstId;*/
                adviceUrl = "/common/advice?piid=" + processInstId;
                fileUrl = FILE_URL  + "&category=0";
                operateUrl = "/common/operate";// 按钮表单
            } else if (businessKey.equals("key-report-manage")) {
                ReqRepManage apply = iReqRepManageService.getOne(new QueryWrapper<ReqRepManage>()
                        .lambda()
                        .eq(ReqRepManage::getPiid, processInstId));
                processUrl = "/process/processRuned?";
                processUrl = processUrl + "processKey=" + processKey
                        + "&taskId=" + taskId + "&businessKey=" + businessKey
                        + "&processInstanceId=" + processInstId + "&ssot=" + ssot;
                formUrl = "/core/requestCheckWorkManager/ReqRepManage/update?piid=" + processInstId
                        + "&opt=update&rowId=" + apply.getRowId() + "&taskId=" + taskId;
                adviceUrl = "/common/advice?piid=" + processInstId;
                fileUrl = FILE_URL  + "&category=1";
                operateUrl = "/common/operate";// 按钮表单
            } else if (businessKey.equals("key-pro-contract-review")) { //合同检验报告管理
                ProContReview apply = iProContReviewService.getOne(new QueryWrapper<ProContReview>()
                        .lambda()
                        .eq(ProContReview::getPiid, processInstId));
                processUrl = "/process/processRuned?";
                processUrl = processUrl + "processKey=" + processKey
                        + "&taskId=" + taskId + "&businessKey=" + businessKey
                        + "&processInstanceId=" + processInstId + "&ssot=" + ssot;
                formUrl = "/core/productCenterWorkManager/cotractReview/update?piid=" + processInstId
                        + "&opt=update&rowId=" + apply.getRowId() + "&taskId=" + taskId;
                adviceUrl = "/common/advice?piid=" + processInstId;
                fileUrl = FILE_URL + "&category=2";
                operateUrl = "/common/operate";// 按钮表单
            } else if (businessKey.equals("key-pro-report-manage")){   // 产品中心检验报告管理
                ProRepManage apply = iProRepManageService.getOne(new QueryWrapper<ProRepManage>()
                        .lambda()
                        .eq(ProRepManage::getPiid, processInstId));
                processUrl = "/process/processRuned?";
                processUrl = processUrl + "processKey=" + processKey
                        + "&taskId=" + taskId + "&businessKey=" + businessKey
                        + "&processInstanceId=" + processInstId + "&ssot=" + ssot;
                formUrl = "/core/productCenterWorkManager/checkReport/update?piid=" + processInstId
                        + "&opt=update&rowId=" + apply.getRowId() + "&taskId=" + taskId;
                adviceUrl = "/common/advice?piid=" + processInstId;
                fileUrl = FILE_URL + "&category=3";
                operateUrl = "/common/operate";// 按钮表单

            }
        } else { //流程查看(只读)页面
            final String FILE_URL = "/common/attachment?processType=view&createUserNickName=" + currentLoginUser.getNickName();
            if ((businessKey.equals("key-report-examine"))){
                BusinessReportExamine applyBean = businessRepExamService.getOne(new QueryWrapper<BusinessReportExamine>()
                    .lambda()
                        .eq(BusinessReportExamine::getPiid, processInstId));
                adviceUrl = "/common/advice?piid=" + processInstId;
                formUrl = "/core/reportExamine/view?piid=" + processInstId +
                        "&rowId=" + applyBean.getRowId() + "&opt=view&taskId=" + taskId;
                operateUrl = "/common/operate/view";// 按钮表单
            }
            else if (businessKey.equals("key-contract-review")) {//合同评审申请
                ReqContReview apply = iReqContReviewService.getOne(new QueryWrapper<ReqContReview>()
                        .lambda()
                        .eq(ReqContReview::getPiid, processInstId));
                adviceUrl = "/common/advice?piid=" + processInstId;
                formUrl = "/core/requestCheckWorkManager/ReqContReview/view?piid=" + processInstId
                        + "&opt=view&rowId=" + apply.getRowId() + "&taskId=" + taskId;
                operateUrl = "/common/operate/view";// 按钮表单
                fileUrl = FILE_URL  + "&category=0";
            } else if (businessKey.equals("key-report-manage")) {//委托检验报告管理
                ReqRepManage apply = iReqRepManageService.getOne(new QueryWrapper<ReqRepManage>()
                        .lambda()
                        .eq(ReqRepManage::getPiid, processInstId));
                adviceUrl = "/common/advice?piid=" + processInstId;
                formUrl = "/core/requestCheckWorkManager/ReqRepManage/view?piid=" + processInstId
                        + "&opt=view&rowId=" + apply.getRowId() + "&taskId=" + taskId + "&blfs=" + blfs;
                operateUrl = "/common/operate/view";// 按钮表单
                fileUrl = FILE_URL  + "&category=1";
                if (StringUtils.equals(blfs, "1")) {
                    iBusinessDeviceService.updateReadByCheckId(apply.getRowId());
                }
            } else if (businessKey.equals("key-pro-contract-review")) {//委托检验报告管理
                ProContReview apply = iProContReviewService.getOne(new QueryWrapper<ProContReview>()
                        .lambda()
                        .eq(ProContReview::getPiid, processInstId));
                adviceUrl = "/common/advice?piid=" + processInstId;
                formUrl = "/core/productCenterWorkManager/cotractReview/view?piid=" + processInstId
                        + "&opt=view&rowId=" + apply.getRowId() + "&taskId=" + taskId;
                operateUrl = "/common/operate/view";// 按钮表单
                fileUrl = FILE_URL+ "&category=2";
            } else if (businessKey.equals("key-pro-report-manage")){
                ProRepManage apply = iProRepManageService.getOne(new QueryWrapper<ProRepManage>()
                        .lambda()
                        .eq(ProRepManage::getPiid, processInstId));
                adviceUrl = "/common/advice?piid=" + processInstId;
                formUrl = "/core/productCenterWorkManager/checkReport/view?piid=" + processInstId
                        + "&opt=view&rowId=" + apply.getRowId() + "&taskId=" + taskId;
                operateUrl = "/common/operate/view";// 按钮表单
                fileUrl = FILE_URL+ "&category=3";
            }
        }

        ProcessUrlVo vo = new ProcessUrlVo();
        vo.setFormUrl(formUrl);
        vo.setFileUrl(fileUrl);
        vo.setFileRelationUrl(fileRelationUrl);
        vo.setAdviceUrl(adviceUrl);
        vo.setProcessUrl(processUrl);
        vo.setOperateUrl(operateUrl);
        vo.setRedirectUrl(redirectUrl);
        vo.setTemplateType(templateType);
        vo.setSendReadUrl(sendReadUrl);
        vo.setWordUrl(wordUrl);
        vo.setBusinessKey(businessKey);
        return AjaxResult.success(vo);
    }

}
