package com.ruoyi.project.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.business.domain.BusinessRelationDeviceUser;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-05 15:45 - 星期四
 * @package: com.ruoyi.project.business.service
 * @JDK-Version : 1.8
 */
public interface BusinessRelationDeviceUserService extends IService<BusinessRelationDeviceUser> {

    /**
     * ---> 查询当前用户是否当前合同中生成报告了
     *  当前合同  不同报告 已生成报告  并且没有关联到这个人那么就为true
     * @author xqh, 987072248@qq.com
     * @date 2021/8/25 16:12
     */
    boolean selectChangeUserIsGenericReport(String contractNumber);
}
