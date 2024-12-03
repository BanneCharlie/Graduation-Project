package com.ruoyi.project.system.service;

import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.domain.SysUserSignature;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-28 10:06 - 星期一
 * @package: com.ruoyi.project.system.service
 * @JDK-Version : 1.8
 */
public interface ISysUserSignatureService extends IBaseService<SysUserSignature> {

    Map<String, Object> getCurrentUserSignature(boolean allFlag , String currentUserId);

    Map<String, Object> getCurrentUserSignature(boolean allFlag);

    SysUserSignature selectOneIsUse(String userName);

    List<SysUserSignature> selectListByUserId(String userId);

    SysUserSignature installSysUserSignatureInstance(
            SysUser currentLoginUser ,
            String signaturePath ,
            String signatureFileSize ,
            String signatureUploadName ,
            String signatureServerName ,
            String fileType
            );

}
