package com.ruoyi.project.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.business.controller.common.FileModuleController;
import com.ruoyi.project.business.domain.ProDataLedger;
import com.ruoyi.project.business.service.ProDataLedgerService;
import com.ruoyi.project.business.utils.FileUtil;
import com.ruoyi.project.business.vo.ProdDataLedgerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * --->
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-03-02 15:10:27
 */
@RestController
@RequestMapping("prod-data-ledger")
public class ProdDataLedgerController extends BaseController {

    private static final FileUtil fileUtil = new FileUtil();
    private static final Logger fileLogger = LoggerFactory.getLogger(FileModuleController.class);

    @Resource
    private ProDataLedgerService proDataLedgerService;


    @GetMapping("get-data-list")
    public TableDataInfo getDataList(ProdDataLedgerFilter prodDataLedgerFilter) {

        String paramString;
        startPage();
        List<ProDataLedger> dataList = proDataLedgerService.list(new QueryWrapper<ProDataLedger>()
                .lambda()
                .like(StringUtils.isNotEmpty(paramString = prodDataLedgerFilter.getEquipmentArchiveNumber()) , ProDataLedger::getEquipmentArchiveNumber , paramString)
                .like(StringUtils.isNotEmpty(paramString = prodDataLedgerFilter.getInspectorOne()) , ProDataLedger::getInspectorOne , paramString)
                .like(StringUtils.isNotEmpty(paramString = prodDataLedgerFilter.getMaintenanceUnit()) , ProDataLedger::getMaintenanceUnit , paramString));

        return getDataTable(dataList);
    }

    /**
     * ---> 下载当前的导入客户模板
     *
     * @param
     * @author xqh, 987072248@qq.com
     * @date 2021/11/19 15:06
     */
    @GetMapping("importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<ProDataLedger> util = new ExcelUtil<>(ProDataLedger.class);
        return util.importTemplateExcel("导入数据记录");
    }

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     *
     * @return
     */
    @PostMapping("importData")
    public AjaxResult importContractData(MultipartFile file) throws Exception {

        ExcelUtil<ProDataLedger> util = new ExcelUtil<>(ProDataLedger.class);
        List<ProDataLedger> importDataList = util.importExcel(file.getInputStream());

        int DATA_COUNT = importDataList.size();
        int TASK_GROUP_SIZE = 118;
        int TASK_COUNT = DATA_COUNT / TASK_GROUP_SIZE + 1;

        CountDownLatch countDownLatch = new CountDownLatch(TASK_COUNT);
        for (int i = 0; i < TASK_COUNT; i++) {
            int finalI = i;
            threadPoolTaskExecutor.execute(() -> {
                List<ProDataLedger> threadDataList = importDataList.subList(finalI * TASK_GROUP_SIZE, Math.min((finalI + 1) * TASK_GROUP_SIZE , DATA_COUNT));
                for (ProDataLedger item : threadDataList) {
                    proDataLedgerService.save(item);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        return AjaxResult.success();
    }
}
