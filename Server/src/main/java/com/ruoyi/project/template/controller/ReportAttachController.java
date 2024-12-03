package com.ruoyi.project.template.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.business.utils.FileUtil;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.template.commons.enums.AttachDataType;
import com.ruoyi.project.template.domain.ReportAttach;
import com.ruoyi.project.template.service.ReportAttachService;
import com.ruoyi.project.template.vo.ReportAttachSearchFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ---> 附件接口
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-06-12 15:00:58
 */
@RestController
@RequestMapping("report-attach")
public class ReportAttachController extends BaseController {

    private static final FileUtil FILE_UTIL = new FileUtil();

    @Value("${ruoyi.reportAttachPath}")
    private String reportAttachPath;

    @Resource
    private ReportAttachService reportAttachService;

    /**
     * 获取当前登录用户的所有附件信息
     *
     * @return
     */
    @GetMapping("get-self-data")
    public TableDataInfo getSelfDataList(ReportAttachSearchFilter searchFilter) {

        String paramString = searchFilter.getDataType();
        if (!AttachDataType.judgeHasEnumType(paramString)) {
            return null;
        }
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();

        startPage();
        List<ReportAttach> reportAttachList = reportAttachService.list(new QueryWrapper<ReportAttach>()
                .lambda()
                .eq(ReportAttach::getDataType, paramString)
                .like(StringUtils.isNotEmpty(paramString = searchFilter.getFileName()), ReportAttach::getUploadName, paramString)
                .eq(ReportAttach::getCreateUserId, loginUser.getUserName())
                .orderByDesc(ReportAttach::getCreateTime));

        return getDataTable(reportAttachList);
    }

    /**
     * 上传附件信息
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("upload-attach")
    public AjaxResult uploadAttach(MultipartFile[] attachFiles, String dataType) throws IOException {

        if (!AttachDataType.judgeHasEnumType(dataType)) {
            return AjaxResult.error("枚举信息异常,请联系管理员!");
        }
        Date nowDate = new Date();
        String suffixPath = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();

        for (MultipartFile attachFile : attachFiles) {
            String uploadName = attachFile.getOriginalFilename();
            if (StringUtils.isEmpty(uploadName)) {
                continue;
            }
            String fileType = FileUtil.getExt(uploadName);
            String filePath = reportAttachPath + suffixPath + "/" + IdWorker.getIdStr() + "." + fileType;
            FILE_UTIL.createFile(filePath, attachFile.getBytes());
            ReportAttach reportAttach = ReportAttach.builder()
                    .createTime(nowDate)
                    .createUserId(loginUser.getUserName())
                    .dataType(dataType)
                    .fileType(fileType)
                    .uploadName(uploadName)
                    .filePath(filePath)
                    .build();
            reportAttachService.save(reportAttach);
        }

        return AjaxResult.success();
    }

    /**
     * 删除自己附件信息
     * @param attachId
     * @return
     */
    @DeleteMapping("remove-self-attach/{attachId}")
    public AjaxResult removeSelfAttachData(@PathVariable String attachId){

        ReportAttach reportAttach = reportAttachService.getById(attachId);
        boolean remove = reportAttachService.removeById(attachId);
        if (remove){
            FILE_UTIL.deleteFile(reportAttach.getFilePath());
        }

        return AjaxResult.success();
    }


}
