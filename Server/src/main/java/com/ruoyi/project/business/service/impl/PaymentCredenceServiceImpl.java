package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.business.domain.PaymentCredence;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.business.mapper.PaymentCredenceMapper;
import com.ruoyi.project.business.mapper.ReqContReviewMapper;
import com.ruoyi.project.business.service.PaymentCredenceService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.template.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-07-02 15:59 - 星期五
 * @package: com.ruoyi.project.business.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PaymentCredenceServiceImpl extends BaseServiceImpl<PaymentCredenceMapper, PaymentCredence> implements PaymentCredenceService {
    @Resource
    private PaymentCredenceMapper paymentCredenceMapper;
    @Resource
    private ReqContReviewMapper reqContReviewMapper;


    @Override
    public Map<String, Object> getCategoryFileListMap(String businessRowId) {
        final String imageFileCredenceKey = "imageFileCredence";
        final String otherFileCredenceKey = "otherFileCredence";
        Map<String,Object> resultMap = new HashedMap(2);

        resultMap.put(imageFileCredenceKey,
                paymentCredenceMapper.selectList(
                        new QueryWrapper<PaymentCredence>()
                            .lambda()
                            .eq(PaymentCredence::getBusinessRowId,businessRowId)
                            .in(PaymentCredence::getCredenceFileType,"jpg","png")
                            .orderByDesc(PaymentCredence::getCreateTime))
        );
        resultMap.put(otherFileCredenceKey,
                paymentCredenceMapper.selectList(
                        new QueryWrapper<PaymentCredence>()
                            .lambda()
                            .eq(PaymentCredence::getBusinessRowId,businessRowId)
                            .notIn(PaymentCredence::getCredenceFileType,"jpg","png")
                            .orderByDesc(PaymentCredence::getCreateTime))
        );

        return resultMap;
    }


    @Override
    public boolean applyCurrentRowIsDefault(String rowId) {

        try {
            PaymentCredence currentDefaultInstance = paymentCredenceMapper.selectOne(new QueryWrapper<PaymentCredence>()
                    .lambda()
                    .eq(PaymentCredence::getIsDefault, 1));
            if (currentDefaultInstance != null){
                currentDefaultInstance.setIsDefault(0);
                paymentCredenceMapper.updateById(currentDefaultInstance);
            }
            PaymentCredence waitChangeInstance = paymentCredenceMapper.selectOne(new QueryWrapper<PaymentCredence>()
                    .lambda()
                    .eq(PaymentCredence::getRowId, rowId));
            waitChangeInstance.setIsDefault(1);

            paymentCredenceMapper.updateById(waitChangeInstance);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean applyContractRealMoney(String businessRowId, Integer applyRealMoney , boolean append) {
        ReqContReview reqContReview = reqContReviewMapper.selectById(businessRowId);
        if (append){
            int dbRealMoney = Integer.parseInt(StringUtils.isNotBlank(reqContReview.getRealMoney()) ? reqContReview.getRealMoney() : "0");
            applyRealMoney += dbRealMoney;
        }
        reqContReview.setRealMoney(String.valueOf(applyRealMoney));
        return reqContReviewMapper.updateById(reqContReview) > 0;
    }

    @Override
    public boolean applyContractRealMoney(String businessRowId, Integer applyRealMoney) {
        return applyContractRealMoney(businessRowId, applyRealMoney,false);
    }

    @Override
    public boolean confirmCurrentBusinessPaymentIsSuccess(String businessRowId) {
        ReqContReview reqContReview = reqContReviewMapper.selectById(businessRowId);
        reqContReview.setIsPaymentSuccess(1);
        return reqContReviewMapper.updateById(reqContReview) > 0;
    }

    @Override
    public PaymentCredence installPaymentCredenceInstance(
            String businessCategory,
            String businessRowId,
            Integer realMoney,
            Integer paymentType,
            String uploadFileName,
            String serverFilePath,
            String fileSize,
            String fileType,
            SysUser currentLoginUser
        )
    {
        PaymentCredence paymentCredence = new PaymentCredence();
        paymentCredence.setBusinessRowId(businessRowId);
        paymentCredence.setBusinessCategory(businessCategory);
        paymentCredence.setRealMoney(new BigDecimal(realMoney));
        paymentCredence.setPaymentType(paymentType);
        paymentCredence.setCredenceUploadFileName(uploadFileName);
        paymentCredence.setCredenceFilePath(serverFilePath);
        paymentCredence.setCredenceFileType(fileType);
        paymentCredence.setCredenceFileSize(fileSize);
        paymentCredence.setConfirmUserName(currentLoginUser.getUserName());
        paymentCredence.setConfirmUserNickname(currentLoginUser.getNickName());
        paymentCredence.setIsDefault(0);
        Date currentDate = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime());
        paymentCredence.setCreateTime(currentDate);
        return paymentCredence;
    }
}
