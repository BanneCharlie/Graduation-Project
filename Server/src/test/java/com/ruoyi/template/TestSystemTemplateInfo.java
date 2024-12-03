package com.ruoyi.template;

import com.ruoyi.project.template.domain.ReportTemplate;
import com.ruoyi.project.template.service.ReportTemplateService;
import com.ruoyi.project.template.util.TemplateHtmlUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ---> 测试系统模板信息
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-07-27 14:23:27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSystemTemplateInfo {

    @Resource
    private ReportTemplateService reportTemplateService;


    /**
     * 1. 初始化所有模板信息 添加class属性
     */
    @Test
    public void initialAllTemplateAddClassAttribute() throws InterruptedException {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        List<ReportTemplate> allTemplateData = reportTemplateService.list();
        CountDownLatch countDownLatch = new CountDownLatch(allTemplateData.size());

        for (ReportTemplate templateInfo : allTemplateData) {
            cachedThreadPool.execute(() -> {
                String afterReplaceContent = TemplateHtmlUtils.directStartingReplace(templateInfo.getTemplateGenericContent());
                templateInfo.setTemplateGenericContent(afterReplaceContent);
                reportTemplateService.updateById(templateInfo);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        System.out.println("End");
        cachedThreadPool.shutdown();
    }

    @Test
    public void initialSingletonTemplateAddClassAttribute(){
        ReportTemplate test = new ReportTemplate();
        ReportTemplate templateInfo = reportTemplateService.getList(test).get(0);

        String afterReplaceContent = TemplateHtmlUtils.directStartingReplace(templateInfo.getTemplateGenericContent());
        templateInfo.setTemplateGenericContent(afterReplaceContent);
        reportTemplateService.updateById(templateInfo);
    }

}
