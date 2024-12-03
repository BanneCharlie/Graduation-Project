package com.ruoyi.project.template.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.lang.UUID;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.business.service.BusinessRequestReportService;
import com.ruoyi.project.business.service.IReqContReviewService;
import com.ruoyi.project.business.service.IReqRepManageService;
import com.ruoyi.project.business.utils.FileUtil;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.template.commons.model.ResultBody;
import com.ruoyi.project.template.domain.ReportTemplate;
import com.ruoyi.project.template.service.DataSourceConfigService;
import com.ruoyi.project.template.service.ReportTemplateService;
import com.ruoyi.project.template.util.ReportRowResolve;
import com.ruoyi.project.template.util.ReportTemplateIdResolve;
import com.ruoyi.project.template.util.RuoYiRequestParam;
import com.ruoyi.project.template.util.StringUtils;
import com.ruoyi.project.template.util.TemplateHtmlUtils;
import com.ruoyi.project.template.vo.UploadParams;
import io.swagger.annotations.Api;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author niminui
 * @date 2020/11/23 10:31
 */
@Api(tags = "报告模板Controller")
@RestController
@RequestMapping("report-template")
public class ReportTemplateController extends BaseController {
    // 嵌入二维码的图片路径
    @Value("${ruoyi.qrcode.insertFilePath}")
    private String imgPath;
    // 生成的二维码的路径及名称
    @Value("${ruoyi.qrcode.filePath}")
    private String destPath;
    private static FileUtil fileUtil = new FileUtil();

    @Resource
    private ReportTemplateService reportTemplateService;
    @Resource
    private DataSourceConfigService dataSourceConfigService;
    @Resource
    private ReportRowResolve reportRowResolve;
    @Resource
    private TokenService tokenService;
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private IReqRepManageService iReqRepManageService;
    @Resource
    private IReqContReviewService iReqContReviewService;

    @Resource
    private BusinessRequestReportService businessRequestReportService;

    @Autowired
    private ServerConfig serverConfig;

    @GetMapping("list")
    public TableDataInfo getList(ReportTemplate report) {
        String temp;
        startPage();
        List<ReportTemplate> list = reportTemplateService.list(new QueryWrapper<ReportTemplate>()
            .lambda()
                .select(
                        ReportTemplate::getTemplateId,
                        ReportTemplate::getTemplateName,
                        ReportTemplate::getDataSourceId,
                        ReportTemplate::getDataSourceName,
                        ReportTemplate::getTemplateCategory,
                        ReportTemplate::getCreateTime,
                        ReportTemplate::getUpdateTime,
                        ReportTemplate::getCreateUserId,
                        ReportTemplate::getCreateUserName,
                        ReportTemplate::getOrderNum
                )
                .like(StringUtils.isNotEmpty(temp = report.getTemplateName()) , ReportTemplate::getTemplateName , temp)
                .eq(StringUtils.isNotEmpty(temp = report.getTemplateCategory()) , ReportTemplate::getTemplateCategory , temp)
                .orderByAsc(ReportTemplate::getOrderNum)
                .orderByDesc(ReportTemplate::getCreateTime)
        );
        return getDataTable(list);
    }

    @GetMapping("getAll")
    public AjaxResult getAllTemplate() {
        return AjaxResult.success(reportTemplateService.list());
    }

    @GetMapping(value = "getById/{templateId}")
    public AjaxResult getTemplateInstance(@PathVariable String templateId) {
        return AjaxResult.success(reportTemplateService.getById(templateId));
    }

    @PostMapping(value = "save")
    public AjaxResult save(@RequestBody ReportTemplate report) {
        ReportTemplate dest = null;
        String id = report.getTemplateId();
        boolean isTrue;
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());

        if (StringUtils.isNotEmpty(id)) {
            dest = reportTemplateService.getById(id);
            String dataSourceId = dest.getDataSourceId();
            String dataSourceName = dest.getDataSourceName();

            dest.setTemplateCategory(report.getTemplateCategory());
            dest.setTemplateName(report.getTemplateName());
            dest.setOrderNum(report.getOrderNum());

            dest.setUpdateTime(new Date());
            dest.setUpdateUserId(user.getUserName());
            dest.setUpdateUserName(user.getNickName());
            dest.setDataSourceId(dataSourceId);
            dest.setDataSourceName(dataSourceName);
            isTrue = reportTemplateService.updateById(dest);
        } else {
            dest = report;
            dest.setTemplateId(null);
            dest.setCreateTime(new Date());
            dest.setCreateUserId(user.getUserName());
            dest.setCreateUserName(user.getNickName());
            isTrue = reportTemplateService.save(dest);
        }
        return toAjax(isTrue);
    }

    @PostMapping("/save-content")
    @Transactional
    public AjaxResult saveContent(@RequestBody Map<String, Object> paramsMap) {
//        RuoYiRequestParam.stdoutRequestBodyParam(paramsMap);

        String content = (String) paramsMap.get("content");
        String templateId = (String) paramsMap.get("templateId");

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();

        ReportTemplate template = reportTemplateService.getById(templateId);
        template.setTemplateContent(content);
        template.setUpdateTime(new Date());
        template.setUpdateUserId(user.getUserName());
        reportTemplateService.updateById(template);
        return AjaxResult.success();
    }


    @PostMapping("/save-generic-content")
    @Transactional
    public AjaxResult saveGenericContent(@RequestBody Map<String, Object> paramsMap) {
        // return AjaxResult.success();

        String genericContent = (String) paramsMap.get("genericContent");
        String templateId = (String) paramsMap.get("templateId");

        ReportTemplate template = reportTemplateService.getById(templateId);
        template.setTemplateGenericContent(genericContent);
        template.setUpdateTime(new Date());
        template.setUpdateUserId(SecurityUtils.getUsername());
        boolean result = reportTemplateService.updateById(template);
        if (!result) {
            return AjaxResult.error();
        }

        // 异步为模板信息追加 class属性
         baseThreadPool.execute(() -> {
             String afterReplaceContent = TemplateHtmlUtils.directStartingReplace(genericContent);
             template.setTemplateGenericContent(afterReplaceContent);
             reportTemplateService.updateById(template);
         });
         return AjaxResult.success();
    }

    /**
     * 根据报告模板id获取报告模板信息，并根据报告模板内容中配置的动态数据，即${xxx},拼接sql语句并执行，
     * 获取结果后替换原来的动态数据字符串
     *
     * @return
     */
    @GetMapping("/getTemplateIdResolve")
    @SuppressWarnings("all")
    public ResultBody getTemplateIdResolve(
            @RequestParam(value = "templateId", required = false, defaultValue = "") String templateId,
            @RequestParam(value = "contractId", required = false, defaultValue = "") String contractId,
            @RequestParam(value = "reportId" , required = false,defaultValue = "") String reportId) {

        ReportTemplate entity = reportTemplateService.getById(templateId);
        //如果不是报告审核那么才走这个逻辑
        if (StringUtils.isEmpty(reportId) && StringUtils.isEmpty(entity.getTemplateContent()) && StringUtils.isEmpty(entity.getTemplateGenericContent())) {
            return ResultBody.ok().data(entity);
        }

        if (StringUtils.isNotEmpty(reportId) && StringUtils.isNotEmpty(contractId)){
            final String baseUrl = "http://221.6.30.202:20009";
            // 重定向到的目标路由地址
            final String redirectRouterPath = baseUrl + "/template/phone?reportId=" + reportId;
            StringBuilder qrcodeFilePath = new StringBuilder(destPath);
            final String imgName = UUID.randomUUID().toString();
            final String filePath = qrcodeFilePath.append(templateId).append("/").append(imgName).append(".jpg").toString();
            //解析并添加报告模板中由检验流程传过来的值
            BusinessRequestReport srcReportBean = businessRequestReportService.getById(reportId);
            ReqContReview reqContReview = iReqContReviewService.getById(contractId);
            // if (StringUtils.isEmpty(srcReportBean.getIsResolveQrcodePath())){
            //     // 如果是第一次查看那么就生成二维码 ， 如果不是那么就跳过
            //     try {
            //         QRCodeUtils.encode(redirectRouterPath, imgPath, filePath, false);
            //         logger.info("生成新的可以解析模板变量的的二维标识码--> :" + (QRCodeUtils.decode(filePath)));
            //     } catch (Exception e) {
            //         e.printStackTrace();
            //     }
            //     srcReportBean.setIsResolveQrcodePath(filePath);
            // }
            String resolveContent = ReportTemplateIdResolve.resolveByReqCheck(srcReportBean.getReportGenericContext(), reqContReview , srcReportBean.getBusinessDeviceNumber());
            srcReportBean.setReportGenericContext(resolveContent);
            reportTemplateService.updateById(entity);
            businessRequestReportService.updateById(srcReportBean);
            entity.setTemplateGenericContent(resolveContent);
            entity.setIsResolveQrcodePath(srcReportBean.getIsResolveQrcodePath());
        }
        String temp;
        entity.setTemplateContent(
                (StringUtils.isNotEmpty(temp = entity.getTemplateGenericContent()) ? temp : "") +
                (StringUtils.isNotEmpty(temp = entity.getTemplateContent()) ? temp : "")
        );
        return ResultBody.ok().data(entity);
    }

    @DeleteMapping("delete/{rowIds}")
    public AjaxResult remove(@PathVariable String[] rowIds) {
        return toAjax(reportTemplateService.deleteReportByIds(rowIds));
    }

    // 更改当前用户的 信息推送标志位
    @PutMapping("changeNotice/{changeFlag}")
    public AjaxResult changeNitoce(
            @PathVariable Integer changeFlag,
            HttpServletRequest request){
        SysUser user = tokenService.getLoginUser(request).getUser();
        user.setIsPrompt(changeFlag);
        return iSysUserService.updateUserNotice(user) ? AjaxResult.success("应用成功") : AjaxResult.error("应用失败");
    }

    @GetMapping("currentUserNotice")
    public AjaxResult currentLoginUserNoticeStatus(HttpServletRequest request){
        SysUser user = tokenService.getLoginUser(request).getUser();
        return AjaxResult.success(iSysUserService.selectUserByUserName(user.getUserName()).getIsPrompt());
    }


    @Log(title = "报告导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, UploadParams uploadParams)
    {
        String id = uploadParams.getId();
        BusinessRequestReport report = businessRequestReportService.getById(id);
        try
        {
            InputStream inputStream = file.getInputStream();
            // 加载PDF文档
            PDDocument document = PDDocument.load(inputStream);
            int pageCount = document.getNumberOfPages();
            report.setFilePages(pageCount);
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            report.setFileName(file.getOriginalFilename());
            report.setFilePath(url);
            report.setFileRelativeName(fileName);
            report.setFileUploadTime(new Date());
            businessRequestReportService.updateById(report);
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }
}
