package com.ruoyi.project.business.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.business.common.constant.enums.ContractPaymentHandleEnum;
import com.ruoyi.project.business.domain.BusinessNumTemp;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.business.service.BusinessNumTempService;
import com.ruoyi.project.business.service.IReqContReviewService;
import com.ruoyi.project.process.domian.ProcAdviceStrategy;
import com.ruoyi.project.process.domian.ProcSignature;
import com.ruoyi.project.process.service.IProcAdviceStrategyService;
import com.ruoyi.project.process.service.IProcSignatureService;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.workflow.domain.EngineProcActInst;
import com.ruoyi.project.workflow.service.EngineFlowService;
import com.ruoyi.project.workflow.service.IEngineProcActInstService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 合同评审管理
 * @author niminui
 * @date 2021/5/28 15:32
 */
@RestController
@RequestMapping("req-cont-review")
public class ReqContReviewController extends BaseController {
    private static Lock lock = new ReentrantLock();

    @Resource
    private IProcSignatureService iProcSignatureService;

    @Resource
    private IProcAdviceStrategyService iProcAdviceStrategyService;

    @Resource
    private EngineFlowService engineFlowService;

    @Resource
    private IEngineProcActInstService iEngineProcActInstService;

    @Resource
    private IReqContReviewService iReqContReviewService;

    @Resource
    private ISysUserService iSysUserService;

    @Resource
    private ISysDeptService iSysDeptService;

    @Resource
    private BusinessNumTempService businessNumTempService;

    private RedisCache redisCache;
    @Autowired
    public ReqContReviewController(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @GetMapping("get-list")
    public TableDataInfo getList(ReqContReview reqContReview) {

        Map<String, Object> params = reqContReview.getParams();
        String beginTime = null;
        String endTime = null;
        if (params != null && params.size() != 0){
            beginTime = (String) params.get("beginTime");
            endTime = (String) params.get("endTime");
        }
        startPage();
        List<ReqContReview> list = iReqContReviewService.getList(reqContReview,beginTime,endTime);
        return getDataTable(list);
    }

    /**
     * ---> 检测当前合同是否缴费完成
     * @author xqh, 987072248@qq.com
     * @date 2021/7/23 14:09
     * @param rowId  合同流水id
     */
    @GetMapping("check-payment-status")
    public AjaxResult checkPaymentStatus(@RequestParam String rowId){
        ReqContReview paymentReqContract = iReqContReviewService.getById(rowId);
        return AjaxResult.success(ContractPaymentHandleEnum.resolveHandlePayment(paymentReqContract.getPaymentType() , paymentReqContract.getIsPaymentSuccess() , paymentReqContract.getConfirmPrint()));
    }

    @GetMapping("get-review-info")
    public AjaxResult getReviewInfo(@RequestParam(value = "rowId") String rowId) {
        ReqContReview byId = iReqContReviewService.getById(rowId);
        return AjaxResult.success(byId);
    }

    @PostMapping(value = "form-save")
    public AjaxResult formSave(@RequestBody Map<String, Object> parameterMap) {
        ReqContReview promobj = JSON.parseObject(JSON.toJSONString(parameterMap.get("entity")), ReqContReview.class);
        boolean flag = true;
        String rowId = promobj.getRowId();
        ReqContReview beanDest = null;
        String adviceWord = " ";
        String opt = "save";
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        String saveIds = "";
        if (StringUtils.isNotEmpty(rowId)) {
            // 更新操作
//            opt = "update";
//            String atName = parameterMap.get("atid") == null ? "" : parameterMap.get("atid").toString();
//            EngineProcInst in = engineFlowService.getProcInstByProcInstId(promobj.getPiid());
            beanDest = iReqContReviewService.getById(rowId);
            saveIds = beanDest.getAttachmentResultIds() == null ? "" : beanDest.getAttachmentResultIds();
            String title = beanDest.getTitle();
//            String atid = parameterMap.get("atid") == null ? "" : parameterMap.get("atid").toString();
            //beanDest.setActId(atid);
            BeanUtils.copyProperties(promobj, beanDest);
            beanDest.setTitle(title);
        } else {
            // 保存操作
            beanDest = promobj;
            beanDest.setRowId(null);
            beanDest.setConfirmPrint(0);
            beanDest.setIsEnd(0);
            beanDest.setCreateUserId(user.getUserName());
            beanDest.setCreateUserName(user.getNickName());
            beanDest.setDeletes("0");
            SysDept dept = iSysDeptService.selectDeptById(user.getDeptId());
            beanDest.setCreateDeptId(String.valueOf(dept.getDeptId()));
            beanDest.setCreateDeptName(dept.getDeptName());
            String time = DateUtils.getTime();
            Date date = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", time);
            beanDest.setCreateTime(date);
            beanDest.setIsPaymentSuccess(0);    // 默认0 未缴费
            beanDest.setActName(StringUtils.getString(parameterMap.get("currentActName")));
            beanDest.setActId(StringUtils.getString(parameterMap.get("currentActId")));
            beanDest.setPtid(StringUtils.getString(parameterMap.get("processKey")));
            beanDest.setBusinessKey(StringUtils.getString(parameterMap.get("businessKey")));
            beanDest.setPiid(StringUtils.getString(parameterMap.get("processInstId")));
            beanDest.setIsRead("1"); //该委托合同是否开启检验申请，1为未申请，0位已申请
            beanDest.setTitle("委托业务合同评审管理（" + user.getNickName() + "）");
        }
        try {
            /*
                如果原来有文件链接 rowID , 那么就拼接 rowId
             */
            final String currentStepIds = StringUtils.isNull
                    (promobj.getAttachmentResultIds()) ?
                                                    "" :
                       promobj.getAttachmentResultIds();

            if (StringUtils.checkFiledIsValid(currentStepIds)){
                if (StringUtils.isNotBlank(saveIds)){
                    saveIds += ("," + currentStepIds);
                }else {
                    saveIds = currentStepIds;
                }
            }
            beanDest.setAttachmentResultIds(saveIds);

            String createUserId = user.getUserName();
            String createUserName = user.getNickName();
            if (StringUtils.isNotEmpty(beanDest.getFileResultIds())) {
                beanDest.setFileResultIds(beanDest.getFileResultIds().replaceAll("\\[", ""));
                beanDest.setFileResultIds(beanDest.getFileResultIds().replaceAll("]", ""));
            }

            // 1.更新对应的附件列表的ID
            String fwDestId = beanDest.getRowId();
            String ptid = StringUtils.getString(parameterMap.get("processKey"));
            String piid = StringUtils.getString(parameterMap.get("processInstId"));
            String atid = StringUtils.getString(parameterMap.get("currentActId"));
            String selectedUserNames = StringUtils.getString(parameterMap.get("selectedUserNames"));
            String currentActName = StringUtils.getString(parameterMap.get("currentActName"));
            String blfs = StringUtils.getString(parameterMap.get("blfs"));

            BusinessNumTemp businessNumTempServiceById = null;
            // 只有当第一次 启动流程并提交时才会 触发 redis ， 这样可以避免 用户 生成之后不提交流程出现占号现象
            if (StringUtils.isEmpty(beanDest.getRowId())) {
                iReqContReviewService.save(beanDest);
                /*
                    获取前端传递过来的 本次 编号操作数和编号
                 */
                final Integer number = (Integer) parameterMap.get("number");
                final String currentOperationIdentifying = (String) parameterMap.get("currentOperationIdentifying");
                final String currentDeviceType = beanDest.getDeviceCategory();
                /*
                    为编号表关联 编号业务表单字段 ， 新建业务表时 创建之后更新其 业务表单键值
                 */
                BusinessNumTemp businessNumTemp = new BusinessNumTemp();
                businessNumTemp.setType("contract");
                businessNumTemp.setDeviceType(currentDeviceType);
                businessNumTemp.setBusinessRowId(beanDest.getRowId());
                businessNumTemp.setCurrentOperationIdentifying(currentOperationIdentifying);
                businessNumTemp.setNumber(number);
                businessNumTemp.setIsUse("0");
                businessNumTempService.save(businessNumTemp);
            } else {

                businessNumTempServiceById = businessNumTempService.getOne(new QueryWrapper<BusinessNumTemp>()
                    .lambda()
                    .eq(BusinessNumTemp::getBusinessRowId,rowId));
                // 判断如果当前流程为最后一步 那么就 将现在临时存在的编号的 isUse属性设置为1 证明已经用在合同中了
                EngineProcActInst engineProcActInst = (iEngineProcActInstService.list(
                        new QueryWrapper<EngineProcActInst>()
                                .lambda()
                                .eq(EngineProcActInst::getProcInstId, piid)
                                .orderByDesc(EngineProcActInst::getEnOrder)
                )).get(0);
                if ("end".equals(engineProcActInst.getEnType())){
                    businessNumTempServiceById.setIsUse("1");
                    beanDest.setIsEnd(1);
                    /*
                        当合同评审到达提交最后一步的时候 ，则为当前合同添加关联人员 -- 即申请人
                     */
                    businessNumTempService.updateById(businessNumTempServiceById);
                }
                iReqContReviewService.updateById(beanDest);
            }

            // 2.保存意见
            // 意见部分表单命名规则：advice_wdw_huiqian，即advice_表单作用
            Set<String> keyset = parameterMap.keySet();
            List<String> advicekeyList = new ArrayList<String>();// 参数中，所有意见部分的参数key
            // list
            String advicePrefix = "advice_";// 所有意见部分的参数前缀
            for (String s : keyset) {
                if (s.startsWith(advicePrefix)) {
                    advicekeyList.add(s);
                }
            }
            StringBuilder adviceStrategyContent = new StringBuilder();
            // 遍历意见参数keylist，取出关键字：控制名字
            for (String key : advicekeyList) {
                Object obj = parameterMap.get(key);
                String keyVal = obj.toString();
                if (!keyVal.isEmpty()) {// 参数的value不为空才保存
                    adviceStrategyContent.append(keyVal).append(",");
                    String controlName = key.substring(advicePrefix.length());
                    ProcSignature signature = new ProcSignature();
                    signature.setControlName(controlName);
                    signature.setOptionContext(keyVal);
                    signature.setTableId(fwDestId);
                    signature.setTableName("BUSINESS_PRODUCT_CONTRACT_REVIEW");
                    signature.setCreateTime(new Date());
                    signature.setCreatorId(createUserId);
                    signature.setDeleted("0");
                    signature.setCreatorName(createUserName);
                    signature.setPiid(piid);
                    iProcSignatureService.save(signature);
                }

            }
            adviceStrategyContent = new StringBuilder(adviceStrategyContent.toString().endsWith(",") ? adviceStrategyContent
                    .substring(0, adviceStrategyContent.length() - 1)
                    : adviceStrategyContent.toString());
            // 3.保存意见处理(多个意见存在一条记录中)
            ProcAdviceStrategy adviceStrategy = new ProcAdviceStrategy();
            adviceStrategy.setCreateTime(new Date());
            adviceStrategy.setCreatorUserId(createUserId);
            adviceStrategy.setCreatorUsername(createUserName);
            adviceStrategy.setTableId(fwDestId);
            adviceStrategy.setTableName("BUSINESS_PRODUCT_CONTRACT_REVIEW");
            adviceStrategy.setAdviceContent(adviceStrategyContent.toString());// 意见内容
            adviceStrategy.setPiid(piid);
            adviceStrategy.setPtid(ptid);
            adviceStrategy.setAiid(atid);
            adviceStrategy.setDocType("0");
            adviceStrategy.setCurrentActName(currentActName);
            adviceStrategy.setFillTime(new Date());
            String fillUserName = createUserName;
            if (blfs.equals("1")) {
                if (selectedUserNames != null && selectedUserNames.length() > 0) {
                    fillUserName += "提交给" + selectedUserNames;
                }else{
                    fillUserName="办结归档";
                }
            } else if (blfs.equals("3")) {
                fillUserName += "新增任务给" + selectedUserNames;

            }// 处理方式
            adviceStrategy.setFillUserName(fillUserName);
            adviceStrategy.setRecordId(adviceWord);
            if (!blfs.equals("admin")) {
                iProcAdviceStrategyService.save(adviceStrategy);
            }
        } catch (Exception e) {

            flag = false;
            e.printStackTrace();
        }
        return toAjax(flag);
    }

    /**
     * 删除委托合同评审申请
     */
    @Log(title = "委托合同评审", businessType = BusinessType.DELETE)
    @DeleteMapping("del-apply/{rowIds}")
    public AjaxResult remove(@PathVariable String[] rowIds)
    {
        return toAjax(iReqContReviewService.deleteBeanByIds(rowIds));
    }

}
