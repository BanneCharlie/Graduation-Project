package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.mapper.BusinessRequestReportMapper;
import com.ruoyi.project.business.service.BusinessRequestReportService;
import com.ruoyi.project.business.utils.FileUtil;
import com.ruoyi.project.business.vo.ReportVo;
import com.ruoyi.project.process.vo.PieVo;
import com.ruoyi.project.system.domain.SysUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Override
    public List<ReportVo> getAllReportList() {
        List<ReportVo> reportVoList = businessRequestReportMapper.getAllReportList();

        if (reportVoList == null && reportVoList.size() <= 0) {
            throw new CustomException("没有查询到报告信息！");
        }

        return reportVoList;
    }

    /**
     * 根据报告列表统计分类数据并返回饼图数据
     *
     * @param reportList 报告列表
     * @return 包含饼图数据的 Map
     */
    @Override
    public Map<String, Object> getPieChartData(List<ReportVo> reportList) {
        Map<String, Integer> categoryCountMap = new HashMap<>();
        categoryCountMap.put("DT", 0);
        categoryCountMap.put("CJ", 0);
        categoryCountMap.put("QZ", 0);
        categoryCountMap.put("ZJ", 0);
        categoryCountMap.put("KJ", 0);
        categoryCountMap.put("QT", 0);

        // 遍历报告列表并统计分类数量
        for (ReportVo report : reportList) {
            String category = report.getTemplateCategory();
            categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
        }

        // 创建饼图数据列表
        List<PieVo> pieVoList = new ArrayList<>();
        pieVoList.add(new PieVo(categoryCountMap.get("DT"), "电梯"));
        pieVoList.add(new PieVo(categoryCountMap.get("CJ"), "起重机械"));
        pieVoList.add(new PieVo(categoryCountMap.get("QZ"), "场内专用车辆"));
        pieVoList.add(new PieVo(categoryCountMap.get("ZJ"), "质检"));
        pieVoList.add(new PieVo(categoryCountMap.get("KJ"), "科研"));
        pieVoList.add(new PieVo(categoryCountMap.get("QT"), "其他"));

        // 计算总计数
        int total = reportList.size();

        // 组装返回数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", total);
        resultMap.put("pieColumn", Arrays.asList("电梯", "起重机械", "场内专用车辆", "质检", "科研", "其他"));
        resultMap.put("pieData", pieVoList);

        return resultMap;
    }

}
