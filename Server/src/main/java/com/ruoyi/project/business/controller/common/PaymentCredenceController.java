package com.ruoyi.project.business.controller.common;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.business.service.PaymentCredenceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/***
 * ---->    缴费接口
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-07-07 10:20 - 星期三
 * @package: com.ruoyi.project.business.controller
 * @JDK-Version : 1.8
 */
@RestController
@RequestMapping("/paymentCredence/")
public class PaymentCredenceController {
    @Resource
    private PaymentCredenceService paymentCredenceService;

    // 获取所有当前业务的缴费凭证 ， 并且按照文件类型来分类
    @GetMapping("getAllCategoryCredenceFile/{businessRowId}")
    public AjaxResult getCredenceFileByFileType(@PathVariable String businessRowId){
        Map<String , Object> resultMap = paymentCredenceService.getCategoryFileListMap(businessRowId);
        return AjaxResult.success(resultMap);
    }

    @PutMapping("setCurrentFileDefault/{rowId}")
    public AjaxResult setOperationCurrentFileIsDefault(@PathVariable String rowId){
         return paymentCredenceService.applyCurrentRowIsDefault(rowId) ? AjaxResult.success("设置成功!") : AjaxResult.error("操作失败!");
    }

    @DeleteMapping("deleteCurrentOperationFile/{rowId}")
    public AjaxResult deleteOperationRow(@PathVariable String rowId){
        return paymentCredenceService.removeById(rowId) ?
                        AjaxResult.success("操作成功!")  :
                        AjaxResult.error("操作失败!")    ;
    }

    @PostMapping("updateRealMoneyApply")
    public AjaxResult updateCurrentBusinessRealMoney(
            @RequestParam("applyType") String applyType,
            @RequestParam("businessRowId") String businessRowId,
            @RequestParam("applyRealMoney") Integer applyRealMoney
    ){
        boolean resultFlag;
        if ("append".equals(applyType)){
            resultFlag = paymentCredenceService.applyContractRealMoney(businessRowId, applyRealMoney , true);
        }else {
            resultFlag = paymentCredenceService.applyContractRealMoney(businessRowId, applyRealMoney);
        }
        return resultFlag ? AjaxResult.success("应用成功!") : AjaxResult.error("应用失败!");
    }

    @PutMapping("confirmBusinessPaymentIsSuccess/{businessRowId}")
    public AjaxResult confirmCurrentBusinessIsSuccess(@PathVariable String businessRowId){
        return paymentCredenceService.confirmCurrentBusinessPaymentIsSuccess(businessRowId) ?
                           AjaxResult.success("更新状态成功!")  :
                           AjaxResult.error("更新失败，联系管理员!");
    }
}
