package com.ruoyi.project.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.process.domian.SysUserReportAuthor;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-07-23 09:21 - 星期五
 * @package: com.ruoyi.project.process.service
 * @JDK-Version : 1.8
 */
public interface SysUserReportAuthorService extends IService<SysUserReportAuthor> {

    boolean checkCurrentUserAuthor(SysUserReportAuthor sysUserReportAuthor , String reportType);

}
