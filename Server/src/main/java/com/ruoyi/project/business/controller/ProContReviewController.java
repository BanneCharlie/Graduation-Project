package com.ruoyi.project.business.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.business.domain.ProContReview;
import com.ruoyi.project.business.service.IProContReviewService;
import com.ruoyi.project.process.domian.ProcAdviceStrategy;
import com.ruoyi.project.process.domian.ProcSignature;
import com.ruoyi.project.process.service.IProcAdviceStrategyService;
import com.ruoyi.project.process.service.IProcSignatureService;
import com.ruoyi.project.process.service.IProcSxpzService;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.workflow.domain.EngineProcInst;
import com.ruoyi.project.workflow.service.EngineFlowService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 合同评审管理
 * @author niminui
 * @date 2021/5/28 15:32
 */
@RestController
@RequestMapping("pro-cont-review")
public class ProContReviewController extends BaseController {
    /**
     * 事项配置管理
     */
    @Resource
    private IProcSxpzService iProcSxpzService;

    @Resource
    private IProcSignatureService iProcSignatureService;

    @Resource
    private IProcAdviceStrategyService iProcAdviceStrategyService;

    @Resource
    private EngineFlowService engineFlowService;

    @Resource
    private IProContReviewService iProContReviewService;

    @Resource
    private ISysUserService iSysUserService;

    @Resource
    private ISysDeptService iSysDeptService;

    public ProContReviewController(IProcSxpzService iProcSxpzService) {
        this.iProcSxpzService = iProcSxpzService;
    }

    @GetMapping("get-list")
    public TableDataInfo getList(ProContReview proContReview) {
        startPage();
        List<ProContReview> list = iProContReviewService.getList(proContReview);
        return getDataTable(list);
    }

    @GetMapping("get-review-info")
    public AjaxResult getReviewInfo(@RequestParam(value = "rowId") String rowId) {
        ProContReview byId = iProContReviewService.getById(rowId);
        return AjaxResult.success(byId);
    }

    @PostMapping(value = "form-save")
    public AjaxResult formSave(@RequestBody Map<String, Object> parameterMap) {
        ProContReview promobj = JSON.parseObject(JSON.toJSONString(parameterMap.get("entity")), ProContReview.class);
        boolean flag = true;
        String rowId = promobj.getRowId();
        ProContReview beanDest = null;
        String adviceWord = " ";
        String opt = "save";
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        String saveIds = "";
        if (StringUtils.isNotEmpty(rowId)) {
            // 更新操作
            opt = "update";
            String atName = parameterMap.get("atid") == null ? "" : parameterMap.get("atid").toString();
            EngineProcInst in = engineFlowService.getProcInstByProcInstId(promobj.getPiid());
            beanDest = iProContReviewService.getById(rowId);
            saveIds = beanDest.getAttachmentResultIds();
            String title = beanDest.getTitle();
            String atid = parameterMap.get("atid") == null ? "" : parameterMap.get("atid").toString();
            //beanDest.setActId(atid);
            BeanUtils.copyProperties(promobj, beanDest);
            beanDest.setTitle(title);
        } else {
            // 保存操作
            beanDest = promobj;
            beanDest.setRowId(null);
            beanDest.setCreateUserId(user.getUserName());
            beanDest.setCreateUserName(user.getNickName());
            beanDest.setDeletes("0");
            SysDept dept = iSysDeptService.selectDeptById(user.getDeptId());
            beanDest.setCreateDeptId(String.valueOf(dept.getDeptId()));
            beanDest.setCreateDeptName(dept.getDeptName());
            String time = DateUtils.getTime();
            Date date = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", time);
            beanDest.setCreateTime(date);

            beanDest.setActName(StringUtils.getString(parameterMap.get("currentActName")));
            beanDest.setActId(StringUtils.getString(parameterMap.get("currentActId")));
            beanDest.setPtid(StringUtils.getString(parameterMap.get("processKey")));
            beanDest.setBusinessKey(StringUtils.getString(parameterMap.get("businessKey")));
            beanDest.setPiid(StringUtils.getString(parameterMap.get("processInstId")));
            beanDest.setTitle("产品中心合同评审管理（" + user.getNickName() + "）");
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
                beanDest.setAttachmentResultIds(saveIds);
            }

            String createUserId = user.getUserName();
            String createUserName = user.getNickName();
            if (StringUtils.isEmpty(beanDest.getRowId())) {
                iProContReviewService.save(beanDest);
            } else {
                iProContReviewService.updateById(beanDest);
            }
            // 1.更新对应的附件列表的ID
            String fwDestId = beanDest.getRowId();
            String ptid = StringUtils.getString(parameterMap.get("processKey"));
            String piid = StringUtils.getString(parameterMap.get("processInstId"));
            String atid = StringUtils.getString(parameterMap.get("currentActId"));
            String selectedUserNames = StringUtils.getString(parameterMap.get("selectedUserNames"));
            String currentActName = StringUtils.getString(parameterMap.get("currentActName"));
            String blfs = StringUtils.getString(parameterMap.get("blfs"));

            // 1.保存附件
            /*String attachmentIds = StringUtils.getString(parameterMap.get("attachmentIds"));
            if (attachmentIds != null && !attachmentIds.isEmpty()) {
                String[] noticeAttachmentIdAttr = attachmentIds.split(",");
                for (String id : noticeAttachmentIdAttr) {
                    FileAttachmentInstance attachment = attachmentInstanceManager
                            .get(id);
                    // 若之前有则不用更新对应的tableid
                    if (attachment.getTableId() == null
                            || attachment.getTableId().isEmpty()) {
                        attachment.setTableId(fwDestId);
                        attachment.setTableName("BD_CAR_USE_APPLY");
                        attachment.setAiId(atid);
                        attachment.setPiId(piid);

                        attachmentInstanceManager.save(attachment);
                    }
                }
            }*/
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
                    signature.setTableName("BD_CAR_USE_APPLY");
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
                if (selectedUserNames!=null&&selectedUserNames.length()>0) {
                    fillUserName += "默认给" + selectedUserNames;
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
     * 删除产品合同评审申请
     */
    @Log(title = "委托合同评审", businessType = BusinessType.DELETE)
    @DeleteMapping("del-apply/{rowIds}")
    public AjaxResult remove(@PathVariable String[] rowIds)
    {
        return toAjax(iProContReviewService.deleteBeanByIds(rowIds));
    }
}
