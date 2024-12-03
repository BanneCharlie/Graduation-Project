package com.ruoyi.project.business.common.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.business.common.businessnumber.BusinessCommonNumber;
import com.ruoyi.project.business.domain.BusinessNumTemp;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.domain.ReqRepManage;
import com.ruoyi.project.business.service.BusinessNumTempService;
import com.ruoyi.project.business.service.BusinessRequestReportService;
import com.ruoyi.project.business.service.IReqContReviewService;
import com.ruoyi.project.business.service.IReqRepManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-17 15:15 - 星期四
 * @package: com.ruoyi.project.business.common.schedule
 * @JDK-Version : 1.8
 */
@Component
@EnableScheduling
public class BusinessSystemHandlerScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(BusinessSystemHandlerScheduleTask.class);

    private RedisCache redisCache;
    private BusinessNumTempService businessNumTempService;
    private IReqContReviewService iReqContReviewService;
    private IReqRepManageService iReqRepManageService;
    private BusinessRequestReportService businessRequestReportService;
    @Autowired
    private BusinessSystemHandlerScheduleTask(
            RedisCache redisCache,
            BusinessNumTempService businessNumTempService,
            IReqContReviewService iReqContReviewService,
            IReqRepManageService iReqRepManageService,
            BusinessRequestReportService businessRequestReportService) {
        this.redisCache = redisCache;
        this.businessNumTempService = businessNumTempService;
        this.iReqContReviewService = iReqContReviewService;
        this.iReqRepManageService = iReqRepManageService;
        this.businessRequestReportService = businessRequestReportService;
    }

    //    // 30 秒执行一次
//    private static final String customCron1 = "0/30 * * * * *";
//    // 每分钟执行一次
//    private static final String customCron2 = "0 0/1 * * * *";
    // 每天晚上 1 点 执行一次
    private static final String customCron3 = "0 0 1 * * *";
    // 每天晚上 2点 执行一次
    private static final String customCron4 = "0 0 2 * * *";
    // 每天晚上 3点 执行一次
    private static final String customCron5 = "0 0 3 * * *";
    /**
     * ---> 定时清空 redis中的缓存数据
     * @author xqh, 987072248@qq.com
     * @date 2021/6/25 21:32
     * @param
     * @return {@link }
     */
    @Scheduled(cron = customCron3)
    public void clearBusinessNumberToRedis(){
        logger.info("定时任务执行--清除redis缓存 , 释放redis中的异常number");
        BusinessCommonNumber[] deviceTypes = BusinessCommonNumber.values();
        for (BusinessCommonNumber deviceType : deviceTypes) {
            String deviceTypeValue = deviceType.getValue();
            if (redisCache.getBusinessNumberCacheMapValue(deviceTypeValue) != null){
                redisCache.clearBusinessNumberCacheMapOneValueByKey(deviceTypeValue);
            }
        }
    }
    /**
     * ---> 定时任务 ， 按照逻辑清除业务系统的所有不合法数据
     *      numerTemp -> contractReview -> reportManage
     * @author xqh, 987072248@qq.com
     * @date 2021/6/25 21:33
     * @param
     * @return {@link}
     */
    @Scheduled(cron = customCron4)
    @SuppressWarnings("all")
    public void checkDataBaseContractAndTempNumber(){
        logger.info("定时任务-->对比数据库中 合同评审流程列表和合同编号列表");
        List<Object> contractRowIds = iReqContReviewService.selectRowIds();
        List<Object> reportContractRowIds = iReqRepManageService.selectReqContractViewRowIds();
        List<Object> businessNumberContractRowIds = businessNumTempService.selectBusinessRowIds();

        for (Object businessNumberContractRowId : businessNumberContractRowIds) {
            final String currentBusinessNumberContractRowId = String.valueOf(businessNumberContractRowId).intern();
            for (int i = 0 , length = contractRowIds.size() ; i < length ; i++) {
                final String currentContractRowId = String.valueOf(contractRowIds.get(i)).intern();
                if (currentBusinessNumberContractRowId == currentContractRowId){
                    break;
                }else if (i == length - 1){
                    businessNumTempService.remove(new QueryWrapper<BusinessNumTemp>()
                            .lambda()
                            .eq(BusinessNumTemp::getBusinessRowId,currentBusinessNumberContractRowId));
                }
            }
        }
        for (Object reportContractRowId : reportContractRowIds) {
            final String currentReportContractRowId = String.valueOf(reportContractRowId).intern();
            for (int i = 0 , length = contractRowIds.size() ; i < length ; i++) {
                final String currentContractRowId = String.valueOf(contractRowIds.get(i)).intern();
                if (currentReportContractRowId == currentContractRowId){
                    break;
                }else if (i == length - 1){
                    iReqRepManageService.remove(new QueryWrapper<ReqRepManage>()
                        .lambda()
                            .eq(ReqRepManage::getContractId,currentReportContractRowId));
                }
            }
        }
    }

    /**
     * ---> 清除异常的报告记录
     * @author xqh, 987072248@qq.com
     * @date 2021/8/10 9:42
     * @param
     */
    @Scheduled(cron = customCron5)
    public void clearBusinessReportIsNotGenericReportRowData(){
        logger.info("清除异常出具的报告!");
        List<Object> removeIdsList = businessRequestReportService.listObjs(new QueryWrapper<BusinessRequestReport>()
                .lambda()
                .select(BusinessRequestReport::getRowId)
                .eq(BusinessRequestReport::getIsGenericReport, "0"));
        for (Object itemId : removeIdsList) {
            businessRequestReportService.removeById(String.valueOf(itemId));
        }
    }
}
