package com.ruoyi.project.common;

import com.ruoyi.common.utils.QRCodeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.service.BusinessRequestReportService;
import com.ruoyi.project.template.commons.model.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-30 15:33 - 星期三
 * @package: com.ruoyi.project.common
 * @JDK-Version : 1.8
 */
@Slf4j
@RestController
public class QRCodeController {
    @Resource
    private BusinessRequestReportService businessRequestReportService;
    /*
     * ---> 供外部访问的接口 ， 扫描二维码会访问这个接口
     * @author xqh, 987072248@qq.com
     * @date 2021/7/1 14:06
     * @param templateId
     * @return {@link com.ruoyi.project.template.commons.model.ResultBody}
     */
    @GetMapping("/showReportData")
    @SuppressWarnings("unchecked")
    public ResultBody<BusinessRequestReport> showReportData(
            @RequestParam(defaultValue = "") String reportId
    ) throws Exception {

        BusinessRequestReport srcTemplateReportBean = businessRequestReportService.getById(reportId);
        log.info("扫描二维，解析地址为--->" + QRCodeUtils.decode(srcTemplateReportBean.getIsResolveQrcodePath()));
        String temp;
        srcTemplateReportBean.setReportTemplateContext(
                (StringUtils.isNotEmpty(temp = srcTemplateReportBean.getReportGenericContext()) ? temp : "")
                +
                (StringUtils.isNotEmpty(temp = srcTemplateReportBean.getReportTemplateContext()) ? temp : "")
        );
        return ResultBody.ok().data(srcTemplateReportBean);
    }

}
