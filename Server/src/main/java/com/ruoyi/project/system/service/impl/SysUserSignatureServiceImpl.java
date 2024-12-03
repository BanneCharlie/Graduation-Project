package com.ruoyi.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.domain.SysUserSignature;
import com.ruoyi.project.system.mapper.SysUserSignatureMapper;
import com.ruoyi.project.system.service.ISysUserSignatureService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-28 10:08 - 星期一
 * @package: com.ruoyi.project.system.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class SysUserSignatureServiceImpl extends BaseServiceImpl<SysUserSignatureMapper,SysUserSignature> implements ISysUserSignatureService {

    private SysUserSignatureMapper sysUserSignatureMapper;
    @Autowired
    public SysUserSignatureServiceImpl(SysUserSignatureMapper sysUserSignatureMapper) {
        this.sysUserSignatureMapper = sysUserSignatureMapper;
    }

    @Override
    public Map<String, Object> getCurrentUserSignature(boolean allFlag) {
        return getCurrentUserSignature(allFlag,null);
    }
    @Override
    public Map<String, Object> getCurrentUserSignature(boolean allFlag , String currentUserId) {
        final String historySignatureKey = "historySignature";
        final String currentSignatureKey = "currentSignature";
        Map<String,Object> resultMap = new HashMap<>(2);
        if (allFlag){
            resultMap.put(historySignatureKey,sysUserSignatureMapper.selectList(
                new QueryWrapper<SysUserSignature>()
                    .lambda()
                    .eq(SysUserSignature::getIsDelete,1)
                    .orderByAsc(SysUserSignature::getUserId)
            ));
            resultMap.put(currentSignatureKey,sysUserSignatureMapper.selectList(
                new QueryWrapper<SysUserSignature>()
                    .lambda()
                    .eq(SysUserSignature::getIsDelete,0)
                    .orderByAsc(SysUserSignature::getUserId)
            ));
        }else {
            resultMap.put(historySignatureKey,sysUserSignatureMapper.selectList(
                new QueryWrapper<SysUserSignature>()
                            .lambda()
                            .eq(SysUserSignature::getIsDelete,1)
                            .eq(SysUserSignature::getUserId,currentUserId)
                            .orderByAsc(SysUserSignature::getUserId)
            ));
            resultMap.put(currentSignatureKey,sysUserSignatureMapper.selectList(
                new QueryWrapper<SysUserSignature>()
                            .lambda()
                            .eq(SysUserSignature::getIsDelete,0)
                            .eq(SysUserSignature::getUserId,currentUserId)
                            .orderByAsc(SysUserSignature::getUserId)
            ));
        }
        return resultMap;
    }

    @Override
    public SysUserSignature selectOneIsUse(String userName) {
        QueryWrapper<SysUserSignature> queryWrapper = new QueryWrapper<>();
        try {
            return sysUserSignatureMapper.selectOne(
                    queryWrapper
                        .lambda()
                        .eq(SysUserSignature::getUserId,userName)
                        .eq(SysUserSignature::getIsDelete,0)
            );
        }catch (Exception selectOneIsNot){
            selectOneIsNot.printStackTrace();
            log.error("当前系统异常 ， 存在多个正在使用的图片");
        }
        return null;
    }

    @Override
    public List<SysUserSignature> selectListByUserId(String userId) {
        QueryWrapper<SysUserSignature> queryWrapper = new QueryWrapper<>();
        try {
            return sysUserSignatureMapper.selectList(
                    queryWrapper
                            .lambda()
                            .eq(SysUserSignature::getUserId,userId)
                            .eq(SysUserSignature::getIsDelete,0)
            );
        }catch (Exception list){
            list.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public SysUserSignature installSysUserSignatureInstance(
            SysUser currentLoginUser, String signaturePath,
            String signatureFileSize, String signatureUploadName,
            String signatureServerName, String fileType) {
        SysUserSignature sysUserSignature = new SysUserSignature();
        sysUserSignature.setUserId(currentLoginUser.getUserName());
        sysUserSignature.setUserName(currentLoginUser.getNickName());
        sysUserSignature.setSignatureUploadName(signatureUploadName);
        sysUserSignature.setSignatureServerName(signatureServerName);
        sysUserSignature.setIsDelete(0);
        sysUserSignature.setSignaturePath(signaturePath);
        sysUserSignature.setSignatureFileSize(signatureFileSize);
        sysUserSignature.setFileType(fileType);
        Date currentDate = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime());
        sysUserSignature.setCreateTime(currentDate);
        sysUserSignature.setUpdateTime(currentDate);

        return sysUserSignature;
    }
}
