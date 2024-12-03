package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.mapper.BusinessRequestReportMapper;
import com.ruoyi.project.business.service.BusinessRequestReportService;
import com.ruoyi.project.business.utils.FileUtil;
import com.ruoyi.project.business.vo.ReportVo;
import com.ruoyi.project.system.domain.SysUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-05 14:58 - 星期四
 * @package: com.ruoyi.project.business.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessRequestReportServiceImpl extends ServiceImpl<BusinessRequestReportMapper, BusinessRequestReport> implements BusinessRequestReportService {


    private static Pattern p = Pattern.compile("报告编号\"");
    @Resource
    private BusinessRequestReportMapper businessRequestReportMapper;


    @Override
    public List<ReportVo> getMimeReportVo(Map<String, String> queryParams) {
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();
        return businessRequestReportMapper.getReportVoList(
                loginUser.getUserId(),
                Objects.isNull(queryParams) ? new HashMap<>() : queryParams ,
                StringUtils.isNotEmpty(queryParams.get("templateCategory")) ? queryParams.get("templateCategory") : "");
    }

    @Override
    public boolean changeReportStartStatus(String reportId) {
        BusinessRequestReport report = businessRequestReportMapper.selectById(reportId);
        report.setIsStartFlow("1");
        report.setUpdateTime(new Date());
        try {
            if (businessRequestReportMapper.updateById(report) < 1){
                throw new Exception("操作异常!");
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String changeReportGenericContextByDeviceNumber(BusinessRequestReport srcGenericReport){
        String context = srcGenericReport.getReportGenericContext();
        String key ="报告编号";
        //String result = addBusinessDeviceNumber(srcGenericReport);
        String result = context.replaceAll(key,key+"："+srcGenericReport.getBusinessDeviceNumber());

        return result;
    }

    @Override
    public String importPdf(MultipartFile file,BusinessRequestReport report,String userName){
        try {
            String result = FileUtil.pdfToHtml(file);
            report.setReportTemplateContext(result);
            businessRequestReportMapper.updateById(report);
            return "导入成功！";
        }catch (Exception e){
            return "导入失败！";
        }
    }



}
