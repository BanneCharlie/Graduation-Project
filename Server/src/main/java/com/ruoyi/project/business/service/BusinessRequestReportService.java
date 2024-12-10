package com.ruoyi.project.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.vo.ReportVo;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface BusinessRequestReportService extends IService<BusinessRequestReport> {

    List<ReportVo> getMimeReportVo(Map<String, String> queryParams);

    boolean changeReportStartStatus(String reportId);

    String changeReportGenericContextByDeviceNumber(BusinessRequestReport srcGenericReport);

    public String importPdf(MultipartFile file, BusinessRequestReport report, String userName);

    List<ReportVo> getAllReportList();

    Map<String, Object> getPieChartData(List<ReportVo> reportList);
}
