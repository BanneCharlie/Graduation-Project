package com.ruoyi.project.business.controller.common;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.business.common.exception.BaseValidException;
import com.ruoyi.project.business.common.exception.InputParamException;
import com.ruoyi.project.business.domain.AttachmentInstance;
import com.ruoyi.project.business.domain.PaymentCredence;
import com.ruoyi.project.business.service.FileUpLoadSaveDbSevice;
import com.ruoyi.project.business.service.IAttachInstService;
import com.ruoyi.project.business.service.PaymentCredenceService;
import com.ruoyi.project.business.utils.FileUtil;
import com.ruoyi.project.business.utils.StringSplitUtil;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.domain.SysUserSignature;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.system.service.ISysUserSignatureService;
import com.ruoyi.project.template.commons.model.ResultBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_31 _ 09:33__星期一
 * @package: com.ruoyi.project.business.controller
 * @JDK-Version : 1.8
 */
@RestController
@RequestMapping("/fileModule")
public class FileModuleController {
    @Value("${ruoyi.disk}")
    private String disk;
    @Value("${ruoyi.attachmentDirPath}")
    private String attachmentDirPath;
    @Value("${ruoyi.signatureDirPath}")
    private String signatureFilePath;
    @Value("${ruoyi.paymentCredenceDirPath}")
    private String paymentCredencePath;

    private static final FileUtil fileUtil = new FileUtil();
    private static final Logger fileLogger = LoggerFactory.getLogger(FileModuleController.class);

    private IAttachInstService iAttachInstService;
    private TokenService tokenService;
    private FileUpLoadSaveDbSevice fileUpLoadSaveDbSevice;
    private PaymentCredenceService paymentCredenceService;
    private ISysUserSignatureService iSysUserSignatureService;
    private ISysUserService iSysUserService;

    @Autowired
    public FileModuleController(IAttachInstService iAttachInstService,
                                TokenService tokenService,
                                FileUpLoadSaveDbSevice fileUpLoadSaveDbSevice,
                                PaymentCredenceService paymentCredenceService,
                                ISysUserSignatureService iSysUserSignatureService,
                                ISysUserService iSysUserService)
    {
        this.iAttachInstService = iAttachInstService;
        this.tokenService = tokenService;
        this.fileUpLoadSaveDbSevice = fileUpLoadSaveDbSevice;
        this.paymentCredenceService = paymentCredenceService;
        this.iSysUserSignatureService = iSysUserSignatureService;
        this.iSysUserService = iSysUserService;
    }
//---------------------------------------------------------------------------------------------------------------------

    @PostMapping("/upload")
    public ResultBody fileUpload(
            @RequestParam("uploadWork") MultipartFile[] file,
            @RequestParam("category") String category, // 文件类别
            HttpServletRequest request
    ){
        String message = "上传成功";
        int code = 200;
        // 前端已经做了控制 不可能为空数组
        // 返回本次文件上传所有文件的 rowId
        String resultRowIds = "";
        StringBuilder sb = new StringBuilder(resultRowIds);
        try {
            final SysUser currentLoginUser = tokenService.getLoginUser(request).getUser();
            final Integer fileCategory = Integer.parseInt(category);

            for (MultipartFile multipartFile : file) {
                final String originalFilename = multipartFile.getOriginalFilename();
                if (!StringUtils.checkFiledIsValid(originalFilename)) {
                    throw new InputParamException("传入参数有误!");
                }
                final String fileType = fileUtil.getFileType(originalFilename);
                final String decodeFileName = fileUtil.getDecodeFileName(originalFilename);   // 存入加密的文件编号
                final String filePath;
                // 服务器存储文件地址
                final String serverSavePath;
                if (!StringUtils.checkFiledIsValid(decodeFileName, fileType, disk)) {
                    throw new RuntimeException("系统异常");
                }

                filePath = attachmentDirPath;
                serverSavePath = fileUtil.installDateFilePath(disk, filePath , decodeFileName);
                File saveFile = fileUtil.createFile(serverSavePath, multipartFile.getBytes());// 判断是否存在 并创建文件夹 和文件
                if (null != saveFile){
                    AttachmentInstance attachmentInstance = fileUpLoadSaveDbSevice.installAttachmentInstance(
                            originalFilename,
                            serverSavePath,
                            decodeFileName,
                            fileType,
                            currentLoginUser,
                            fileCategory
                    );
                    iAttachInstService.save(attachmentInstance);
                    sb.append(attachmentInstance.getRowId()).append(",");
                }
            }

            resultRowIds = StringSplitUtil.trimSplitAppend(sb.toString());
        }catch (BaseValidException bve) {
            message = bve.getMessage();
            code = 400;
            bve.printStackTrace();
        }catch (IOException ioE){
            message = "文件系统异常";
            code = 505;
            ioE.printStackTrace();
        }catch (Exception ex) {
            message = "系统异常";
            code = 500;
            ex.printStackTrace();
        }
        return ResultBody.ok().msg(message).code(code).data(resultRowIds);
    }
    /**
     *  根据 文件 唯一 rowId 获取文件实例列表 和 附件实例列表
     ///     * @param fileIds
     * @param attachmentIds
     * @return
     */
    @GetMapping("/fileList")
    public ResultBody getFileList(
            @RequestParam(required = false,value = "attachmentResultIds",defaultValue = "") String attachmentIds
    ){
        Map<String,Object> resMap = new HashMap<>(1);
        resMap.put("previousAttachmentList", fileUpLoadSaveDbSevice.getAttachmentInstanceListByIds(attachmentIds.split(",")) );

        return ResultBody.ok().data(resMap);
    }
    /**
     * --->  上传电子签名
     * @author xqh, 987072248@qq.com
     * @date 2021/6/28 14:18
     * @param file
     * @param request
     * @return {@link AjaxResult}
     */
    @PostMapping("/uploadSignature")
    public AjaxResult uploadSignature(
            @RequestParam("signatureFile") MultipartFile[] file,
            HttpServletRequest request
    ){
        String resultMessage = "上传成功";
        boolean uploadFlag = true;

        for (MultipartFile signatureFile : file) {
            String extension = FileUploadUtils.getExtension(signatureFile);
            boolean allowedExtension = FileUploadUtils.isAllowedExtension(extension, MimeTypeUtils.IMAGE_EXTENSION);
            if (!allowedExtension){
                uploadFlag = false;
                resultMessage = "上传失败!,存在未经允许的文件类型!";
                break;
            }
        }

        if (!uploadFlag){
            return AjaxResult.error(resultMessage);
        }

        try {
            for (MultipartFile signatureFile : file) {
                final SysUser currentLoginUser = tokenService.getLoginUser(request).getUser();
                final String uploadName = signatureFile.getOriginalFilename();
                final String fileType = fileUtil.getFileType(uploadName);
                final String serverName = fileUtil.getDecodeFileName(uploadName);
                final long signatureFileSize = signatureFile.getSize();
                final String realServerFilePath = fileUtil.installSignatureFilePath(disk, signatureFilePath, currentLoginUser.getUserName(), serverName);
                File createFile = fileUtil.createFile(realServerFilePath, signatureFile.getBytes());
                if (createFile != null){
                    SysUserSignature sysUserSignature = iSysUserSignatureService.installSysUserSignatureInstance(
                            currentLoginUser,
                            realServerFilePath,
                            String.valueOf((float) signatureFileSize / 1000),
                            uploadName,
                            serverName,
                            fileType
                    );

                    SysUserSignature currentSysUserSignature = iSysUserSignatureService.selectOneIsUse(currentLoginUser.getUserName());
                    if (currentSysUserSignature != null){
                        currentSysUserSignature.setIsDelete(1);
                        iSysUserSignatureService.updateById(currentSysUserSignature);
                    }
                    sysUserSignature.setIsDelete(0);
                    iSysUserSignatureService.save(sysUserSignature);
                }
            }
        }
        catch (IOException ioE){
            ioE.printStackTrace();
            fileLogger.error("文件系统异常");
            resultMessage = "文件系统异常";
            return AjaxResult.error(resultMessage);
        }
        catch (Exception e){
            fileLogger.error("系统异常");
            e.printStackTrace();
            resultMessage = "系统异常";
            return AjaxResult.error(resultMessage);
        }

        return AjaxResult.success(resultMessage);
    }


    @PostMapping("/uploadPaymentCredence")
    public AjaxResult uploadPaymentCredence(
            @RequestParam("paymentCredenceFile") MultipartFile[] file,
            @RequestParam("businessRowId") String businessRowId,
            @RequestParam("businessCategory") String businessCategory,
            @RequestParam("realMoney") Integer realMoney,
            @RequestParam("paymentType") Integer paymentType,
            HttpServletRequest request
    ){

        String resultMessage = "上传成功";
        try {
            for (MultipartFile signatureFile : file) {
                final SysUser currentLoginUser = tokenService.getLoginUser(request).getUser();
                final String uploadName = signatureFile.getOriginalFilename();
                final String fileType = fileUtil.getFileType(uploadName);
                final String serverName = fileUtil.getDecodeFileName(uploadName);
                final long credenceFileSize = signatureFile.getSize();
                final String realServerFilePath = fileUtil.installPaymentCredenceFilePath(disk, paymentCredencePath, businessRowId,serverName);
                File createFile = fileUtil.createFile(realServerFilePath, signatureFile.getBytes());
                if (createFile != null){
                    PaymentCredence paymentCredence = paymentCredenceService.installPaymentCredenceInstance(
                            businessCategory,
                            businessRowId,
                            realMoney,
                            paymentType,
                            uploadName,
                            realServerFilePath,
                            String.valueOf((float) credenceFileSize / 1000),
                            fileType,
                            currentLoginUser);
                    paymentCredenceService.save(paymentCredence);
                }
            }
        }
        catch (IOException ioE){
            ioE.printStackTrace();
            fileLogger.error("文件系统异常");
            resultMessage = "文件系统异常";
            return AjaxResult.error(resultMessage);
        }
        catch (Exception e){
            fileLogger.error("系统异常");
            e.printStackTrace();
            resultMessage = "系统异常";
            return AjaxResult.error(resultMessage);
        }

        return AjaxResult.success(resultMessage);
    }

//    --------------------------------------- 通用接口 ， 检验文件存在性和 下载文件 显示图片 是通用接口
    @GetMapping("/checkFile")
    public AjaxResult checkFileExist(
            @RequestParam(value = "serverFilePath") String serverFilePath
    ){
        serverFilePath = FileUtil.base64Decode(serverFilePath);
        File checkFile = new File(serverFilePath);
        if (checkFile.exists()){
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
    /**
     * ---> 通用文件下载
     * @author xqh, 987072248@qq.com
     * @date 2021/7/7 14:27
     * @param serverFilePath base64加密后的 服务器文件地址
     * @param realFileName   base64加密后的 文件真实名称
     * @param request
     * @param response
     * @return {@link null}
     */
    @GetMapping("/download")
    public void fileDownload(
            @RequestParam(value = "serverFilePath") String serverFilePath,
            @RequestParam(value = "realFileName") String realFileName,
            HttpServletRequest request,
            HttpServletResponse response) {
        serverFilePath = FileUtil.base64Decode(serverFilePath);
        realFileName = FileUtil.base64Decode(realFileName);
        fileUtil.downloadFile(serverFilePath,request,response,realFileName);
    }
    /**
     * ---> 显示图片
     * @author xqh, 987072248@qq.com
     * @date 2021/6/30 9:33
     * @param imagePath
     * @param response
     * @return {@link}
     */
    @GetMapping("/showImage")
    public void showImageInterface(
            @RequestParam("imagePath") String imagePath,
            HttpServletResponse response
    ){
        try {
            fileUtil.getFileNotDown(FileUtil.base64Decode(imagePath), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示预览签名
     */
    @GetMapping("show-signature/{signatureId}")
    public void showSignatureData(@PathVariable String signatureId , HttpServletResponse response) throws IOException {
        SysUserSignature nowSignature = iSysUserSignatureService.getById(signatureId);
        fileUtil.getFileNotDown(nowSignature.getSignaturePath(), response);
    }

}
