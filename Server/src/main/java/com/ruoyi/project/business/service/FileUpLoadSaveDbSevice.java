package com.ruoyi.project.business.service;

import com.ruoyi.project.business.domain.AttachmentInstance;
import com.ruoyi.project.business.domain.FileUploadInstance;
import com.ruoyi.project.business.temp.AttachmentInstanceTemp;
import com.ruoyi.project.business.temp.FileUploadInstanceTemp;
import com.ruoyi.project.system.domain.SysUser;
import io.swagger.models.auth.In;

import java.util.List;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_06_01 _ 17:27__星期二
 * @package: com.ruoyi.project.business.service
 * @JDK-Version : 1.8
 */
public interface FileUpLoadSaveDbSevice {
    /**
     * 组装文件上传实例
     * @param uploadName 上传的文件名
     * @param filePath    文件存放服务器路径
     * @param fileIdentifyName 文件存放服务器编码名称
     * @param fileType  文件类型
     * @param currentUser 当前用户
     * @param fileCategory 文件类别
     * @return
     */
    FileUploadInstance installFileUploadInstance(
            String uploadName,
            String filePath,
            String fileIdentifyName,
            String fileType,
            SysUser currentUser,
            Integer fileCategory
            );

    /**
     *  组装附件上传实例
     * @param uploadName 上传的文件名
     * @param filePath    文件存放服务器路径
     * @param fileIdentifyName 文件存放服务器编码名称
     * @param fileType  文件类型
     * @param currentUser 当前用户
     * @param fileCategory 文件类别
     * @return
     */
    AttachmentInstance installAttachmentInstance(
            String uploadName,
            String filePath,
            String fileIdentifyName,
            String fileType,
            SysUser currentUser,
            Integer fileCategory
    );

    /**
     *  获取文件列表
     * @param ids 文件记录的 唯一 rowId
     * @return
     */
    List<FileUploadInstance> getFileUploadInstanceListByIds(String[] ids);
    List<AttachmentInstance> getAttachmentInstanceListByIds(String[] ids);

    /**
     *
     * @param fileUploadInstanceTemp 需要上传的 中间文件实体
     * @return
     */
    boolean uploadFileToTempTable(
            FileUploadInstanceTemp fileUploadInstanceTemp
    );
    /**
     *
     * @param attachmentInstanceTemp 需要上传的 中间附件 实体
     * @return
     */
    boolean uploadAttachmentTempTable(
            AttachmentInstanceTemp attachmentInstanceTemp
    );
    /**
     *
     * @param params 根据params 参数列表 组装出一个 FileUploadInstanceTemp
     * @return
     */
    FileUploadInstanceTemp installFileUploadInstanceTemp(String... params);

    /**
     *
     * @param params 根据 params参数列表 组装出一个 AttachmentInstanceTemp
     * @return
     */
    AttachmentInstanceTemp installAttachmentInstancep(String... params);
}
