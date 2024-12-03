package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.business.domain.AttachmentInstance;
import com.ruoyi.project.business.domain.FileUploadInstance;
import com.ruoyi.project.business.service.FileUpLoadSaveDbSevice;
import com.ruoyi.project.business.service.IAttachInstService;
import com.ruoyi.project.business.service.IFileUploadService;
import com.ruoyi.project.business.temp.AttachmentInstanceTemp;
import com.ruoyi.project.business.temp.FileUploadInstanceTemp;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_06_01 _ 17:29__星期二
 * @package: com.ruoyi.project.business.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional
public class FileUpLoadSaveDbServiceImpl implements FileUpLoadSaveDbSevice {

    private IFileUploadService iFileUploadService;
    private IAttachInstService iAttachInstService;
    @Autowired
    public FileUpLoadSaveDbServiceImpl(IFileUploadService iFileUploadService, IAttachInstService iAttachInstService) {
        this.iFileUploadService = iFileUploadService;
        this.iAttachInstService = iAttachInstService;
    }

    //-------Methods-------------------------------------------------------------


    @Override
    public FileUploadInstance installFileUploadInstance(
            String uploadName,
            String filePath,
            String fileIdentifyName,
            String fileType,
            SysUser currentUser,
            Integer fileCategory
    ) {
        FileUploadInstance fileUploadInstance = new FileUploadInstance();
        fileUploadInstance.setUploadName(uploadName);
        fileUploadInstance.setFilePath(filePath);
        fileUploadInstance.setFileType(fileType);
        fileUploadInstance.setFileIdentifyName(fileIdentifyName);
        String time = DateUtils.getTime();
        Date date = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", time);
        fileUploadInstance.setCreateTime(date);
        fileUploadInstance.setUpdateTime(date);
        fileUploadInstance.setFileAttribute(fileCategory);
        fileUploadInstance.setIsDelete(0);
        fileUploadInstance.setCreateUserId(String.valueOf(currentUser.getUserId()));
        fileUploadInstance.setCreateUserName(currentUser.getNickName());

        return fileUploadInstance;
    }

    @Override
    public AttachmentInstance installAttachmentInstance(
            String uploadName,
            String filePath,
            String fileIdentifyName,
            String fileType,
            SysUser currentUser,
            Integer fileCategory) {
        AttachmentInstance attachmentInstance = new AttachmentInstance();
        attachmentInstance.setAttachmentName(uploadName);
        attachmentInstance.setFilePath(filePath);
        attachmentInstance.setFileType(fileType);
        attachmentInstance.setFileIdentifyName(fileIdentifyName);
        String time = DateUtils.getTime();
        Date date = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", time);
        attachmentInstance.setCreateTime(date);
        attachmentInstance.setUpdateTime(date);
        attachmentInstance.setFileAttribute(fileCategory);
        attachmentInstance.setIsDelete(0);
        attachmentInstance.setCreateUserId(String.valueOf(currentUser.getUserId()));
        attachmentInstance.setCreateUserName(currentUser.getNickName());

        return attachmentInstance;
    }

    @Override
    public List<FileUploadInstance> getFileUploadInstanceListByIds(String[] ids) {
        List<FileUploadInstance> result = new LinkedList<>();
        if (ids == null || ids.length == 0){
            return result;
        }
        for (String rowId : ids) {
            result.add(iFileUploadService.getById(rowId));
        }
        return result;
    }

    @Override
    public List<AttachmentInstance> getAttachmentInstanceListByIds(String[] ids) {
        List<AttachmentInstance> result = new LinkedList<>();
        if (ids == null || ids.length == 0){
            return result;
        }
        for (String rowId : ids) {
            result.add(iAttachInstService.getOne(
                    new QueryWrapper<AttachmentInstance>()
                        .lambda()
                        .eq(AttachmentInstance::getIsDelete, "0")
                        .eq(AttachmentInstance::getRowId, rowId)));
        }
        return result;
    }

    //    -----------------------

    @Override
    public boolean uploadFileToTempTable(FileUploadInstanceTemp fileUploadInstanceTemp) {
        return false;
    }

    @Override
    public boolean uploadAttachmentTempTable(AttachmentInstanceTemp attachmentInstanceTemp) {
        return false;
    }

    @Override
    public FileUploadInstanceTemp installFileUploadInstanceTemp(String... params) {
        return null;
    }

    @Override
    public AttachmentInstanceTemp installAttachmentInstancep(String... params) {
        return null;
    }


}
