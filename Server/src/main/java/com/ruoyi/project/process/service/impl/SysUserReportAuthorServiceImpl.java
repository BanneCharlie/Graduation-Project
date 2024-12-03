package com.ruoyi.project.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.project.business.common.businessnumber.BusinessCommonNumber;
import com.ruoyi.project.process.domian.SysUserReportAuthor;
import com.ruoyi.project.process.mapper.SysUserReportAuthorMapper;
import com.ruoyi.project.process.service.SysUserReportAuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-07-23 09:22 - 星期五
 * @package: com.ruoyi.project.process.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserReportAuthorServiceImpl extends ServiceImpl<SysUserReportAuthorMapper, SysUserReportAuthor> implements SysUserReportAuthorService {
    @Resource
    private SysUserReportAuthorMapper sysUserReportAuthorMapper;

    @Override
    @SuppressWarnings("all")
    public boolean checkCurrentUserAuthor(final SysUserReportAuthor sysUserReportAuthor,String reportType) {
        boolean returnFlag = false;
        reportType = reportType.intern();
        if (BusinessCommonNumber.DT.getValue().intern() == reportType && sysUserReportAuthor.getDt().equals("1")){
            returnFlag = true;
        }else if (BusinessCommonNumber.CJ.getValue().intern() == reportType && sysUserReportAuthor.getCj().equals("1")){
            returnFlag = true;
        }else if (BusinessCommonNumber.QZ.getValue().intern() == reportType && sysUserReportAuthor.getQz().equals("1")){
            returnFlag = true;
        }else if (BusinessCommonNumber.KJ.getValue().intern() == reportType && sysUserReportAuthor.getKj().equals("1")){
            returnFlag = true;
        }else if (BusinessCommonNumber.QT.getValue().intern() == reportType && sysUserReportAuthor.getQt().equals("1")){
            returnFlag = true;
        }else if (BusinessCommonNumber.ZJ.getValue().intern() == reportType && sysUserReportAuthor.getZj().equals("1")){
            returnFlag = true;
        }
        return returnFlag;
    }
}
