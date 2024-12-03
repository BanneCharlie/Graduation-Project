package com.ruoyi.project.api.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.api.pojo.ResolveReportFormVo;
import com.ruoyi.project.business.domain.BusinessReportExamine;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.business.service.BusinessRepExamService;
import com.ruoyi.project.business.service.BusinessRequestReportService;
import com.ruoyi.project.business.service.IReqContReviewService;
import com.ruoyi.project.system.domain.SysUserSignature;
import com.ruoyi.project.system.service.ISysUserSignatureService;
import com.ruoyi.project.template.service.ReportTemplateService;
import com.ruoyi.project.template.util.ReportTemplateIdResolve;
import com.ruoyi.project.template.vo.ReportSignaturePart;
import com.ruoyi.project.workflow.domain.EngineProcActInst;
import com.ruoyi.project.workflow.service.IEngineProcActInstService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * --->
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-06-07 15:57:44
 */
@RestController
@RequestMapping("api/report")
public class ReportSourceDataApiController {

    @Resource
    private BusinessRequestReportService businessRequestReportService;
    @Resource
    private IReqContReviewService iReqContReviewService;
    @Resource
    private BusinessRepExamService businessRepExamService;
    @Resource
    private IEngineProcActInstService iEngineProcActInstService;
    @Resource
    private ISysUserSignatureService iSysUserSignatureService;
    @Resource
    private ReportTemplateService reportTemplateService;

    /**
     * 预览具体的报告实例
     *
     * @param reportId 报告Id
     * @return
     */
    @GetMapping("report-instance")
    public AjaxResult reportInstance(@RequestParam String reportId) {
        return AjaxResult.success(businessRequestReportService.getById(reportId));
    }

    /**
     * 获取模板实例
     *
     * @param templateId 模板Id
     * @return
     */
    @GetMapping("template-instance")
    public AjaxResult templateInstance(@RequestParam String templateId){
        return AjaxResult.success(reportTemplateService.getById(templateId));
    }

    /**
     * 仅在报告出具审批流程过后打印报告时 才可以调用此接口
     * 获取转换过字段信息后的报告信息
     *
     * @return
     */
    @PostMapping("resolve-report-and-return")
    public AjaxResult getResolveTemplateReportInfo(@RequestBody ResolveReportFormVo resolveReportFormVo) {

        String reportId = resolveReportFormVo.getReportId();
        BusinessRequestReport reportInstance = businessRequestReportService.getById(reportId);
        // 已经解析过直接返回报告内容
        if ("1".equals(reportInstance.getIsResolve())) {
            return AjaxResult.success(reportInstance);
        }
        String contractReviewId = resolveReportFormVo.getContractReviewId();
        ReqContReview contReviewInstance = iReqContReviewService.getById(contractReviewId);

        BusinessRequestReport resultReport = ReportTemplateIdResolve.resolveReportInfo(reportInstance, contReviewInstance);
        resultReport.setIsResolve("1");
        businessRequestReportService.updateById(resultReport);

        return AjaxResult.success(resultReport);
    }

    /**
     * 获取指定报告Id 历史流程信息
     *
     * @param reportId 报告Id
     * @return
     */
    @GetMapping("get-report-process-info/{reportId}")
    public AjaxResult getReportProcessHistoryInfo(@PathVariable String reportId) {

        BusinessReportExamine examineReportProcess = businessRepExamService.getOne(new QueryWrapper<BusinessReportExamine>()
                .lambda()
                .eq(BusinessReportExamine::getReportId, reportId));

        if (null == examineReportProcess){
            return AjaxResult.success();
        }

        List<EngineProcActInst> processActiveRecord = iEngineProcActInstService.list(new QueryWrapper<EngineProcActInst>()
                .lambda()
                .eq(EngineProcActInst::getProcInstId, examineReportProcess.getPiid())
                .eq(EngineProcActInst::getEnStatus, "finish"));

        Map<String, List<EngineProcActInst>> recordProcessTypeListMap = processActiveRecord.stream()
                .sorted(
                        Comparator.comparing(EngineProcActInst::getEnOrder)
                                .reversed())
                .collect(Collectors.groupingBy(EngineProcActInst::getActName));

        Map<String, ReportSignaturePart> resultActiveProcessSignatureIdMapping = new HashMap<>();

        for (Map.Entry<String, List<EngineProcActInst>> entry : recordProcessTypeListMap.entrySet()) {
            String actName = entry.getKey();
            List<EngineProcActInst> activeProcessList = entry.getValue();

            String executeUserName;
            EngineProcActInst referenceActiveInfo = activeProcessList.get(0);
            Date referenceTime;
            if (actName.contains("申请") || actName.contains("打印")) {
                executeUserName = referenceActiveInfo.getCreateUserId();
                referenceTime = referenceActiveInfo.getCreateTime();
            } else {
                executeUserName = referenceActiveInfo.getModifyUserId();
                referenceTime = referenceActiveInfo.getUpdateTime();
            }

            SysUserSignature executeUserSignature = iSysUserSignatureService.selectOneIsUse(executeUserName);
            ReportSignaturePart signaturePart = ReportSignaturePart.builder()
                    .userName(executeUserName)
                    .signatureId(Objects.nonNull(executeUserSignature) ? executeUserSignature.getRowId() : null)
                    .executeTime(referenceTime)
                    .build();
            resultActiveProcessSignatureIdMapping.put(actName, signaturePart);
        }


        return AjaxResult.success(resultActiveProcessSignatureIdMapping);
    }


}
