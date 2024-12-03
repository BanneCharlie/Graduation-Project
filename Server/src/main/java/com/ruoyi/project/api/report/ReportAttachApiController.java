package com.ruoyi.project.api.report;

import com.ruoyi.project.business.utils.FileUtil;
import com.ruoyi.project.template.domain.ReportAttach;
import com.ruoyi.project.template.service.ReportAttachService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * --->
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-06-13 14:21:29
 */
@RestController
@RequestMapping("api/attach")
public class ReportAttachApiController {

    private static final FileUtil FILE_UTIL = new FileUtil();

    @Resource
    private ReportAttachService reportAttachService;
    /**
     * 针对于合同文件而言才可以使用
     * @param attachRowId 附件rowId
     */
    @GetMapping("/preview-attach/{attachRowId}")
    public void showImageInterfaceNoEncrypt(@PathVariable String attachRowId,
                                            HttpServletResponse response){
        try {
            ReportAttach reportAttach = reportAttachService.getById(attachRowId);
            FILE_UTIL.getFileNoDownloadByFileRowId(reportAttach.getFilePath(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
