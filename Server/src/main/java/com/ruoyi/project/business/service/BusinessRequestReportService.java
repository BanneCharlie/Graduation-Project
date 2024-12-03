package com.ruoyi.project.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.vo.ReportVo;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-05 14:58 - 星期四
 * @package: com.ruoyi.project.business.service
 * @JDK-Version : 1.8
 */
public interface BusinessRequestReportService extends IService<BusinessRequestReport> {

    List<ReportVo> getMimeReportVo(Map<String, String> queryParams);
    /**
     * ---> 改变当前报告的启动流程状态为 1
     * @author xqh, 987072248@qq.com
     * @date 2021/8/24 13:52
     * @param reportId 报告实例的rowId
     */
    boolean changeReportStartStatus(String reportId);

    String changeReportGenericContextByDeviceNumber(BusinessRequestReport srcGenericReport);

    public String importPdf(MultipartFile file, BusinessRequestReport report, String userName);
}
