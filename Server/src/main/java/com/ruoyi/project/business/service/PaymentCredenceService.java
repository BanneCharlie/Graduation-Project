package com.ruoyi.project.business.service;

import com.ruoyi.project.business.domain.PaymentCredence;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;

import java.util.Map;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-07-02 15:59 - 星期五
 * @package: com.ruoyi.project.business.service
 * @JDK-Version : 1.8
 */
public interface PaymentCredenceService extends IBaseService<PaymentCredence> {

    PaymentCredence installPaymentCredenceInstance(
            String businessCategory,
            String businessRowId,
            Integer realMoney,
            Integer paymentType,
            String uploadFileName,
            String serverFilePath,
            String fileSize,
            String fileType,
            SysUser currentLoginUser
    );

    Map<String,Object> getCategoryFileListMap(String businessRowId);

    boolean applyCurrentRowIsDefault(String rowId);

    boolean applyContractRealMoney(String businessRowId,Integer applyRealMoney , boolean append);

    boolean applyContractRealMoney(String businessRowId,Integer applyRealMoney);

    boolean confirmCurrentBusinessPaymentIsSuccess(String businessRowId);
}
