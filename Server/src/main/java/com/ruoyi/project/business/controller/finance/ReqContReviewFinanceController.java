package com.ruoyi.project.business.controller.finance;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.business.common.constant.enums.ContractPaymentHandleEnum;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.business.service.IReqContReviewService;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * --->
 *
 * @author xqh , 987072248@qq.com
 * @data 2022-08-17 14:11:46
 */
@RestController
@RequestMapping("contract-review-finance")
public class ReqContReviewFinanceController extends BaseController {

    @Resource
    private IReqContReviewService iReqContReviewService;

    /**
     * 获取财务人员 待确认打印报告的 合同评审列表
     * @return
     */
    @GetMapping("get-finance-confirm-list")
    public TableDataInfo getFinanceConfirmList(){
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();

        startPage();
        // 获取年终缴费 并且流程执行结束的合同列表
        List<ReqContReview> yearPaymentAndProcessFinishDataList = iReqContReviewService.list(new QueryWrapper<ReqContReview>()
                .lambda()
                .eq(ReqContReview::getPaymentType, ContractPaymentHandleEnum.YEAR_END_PAYMENT.getPaymentCode())
                .eq(ReqContReview::getConfirmPrint , 0)
                .eq(ReqContReview::getIsEnd, 1));

        return getDataTable(yearPaymentAndProcessFinishDataList);
    }

    /**
     * 确认可执行打印
     * @param contractReviewRowId
     * @return
     */
    @PutMapping("confirm-print/{contractReviewRowId}")
    public AjaxResult confirmPrintByContractRowId(@PathVariable String contractReviewRowId){

        ReqContReview waitConfirmContract = iReqContReviewService.getById(contractReviewRowId);
        waitConfirmContract.setConfirmPrint(1);
        return AjaxResult.success(iReqContReviewService.updateById(waitConfirmContract));
    }

}
