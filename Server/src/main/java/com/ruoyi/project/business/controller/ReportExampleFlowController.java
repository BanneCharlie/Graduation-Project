package com.ruoyi.project.business.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.business.common.constant.enums.RedisConstantEnum;
import com.ruoyi.project.business.domain.BusinessReportExamine;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.service.BusinessRepExamService;
import com.ruoyi.project.business.service.BusinessRequestReportService;
import com.ruoyi.project.business.vo.ReportMessageVo;
import com.ruoyi.project.monitor.domain.SysLogininfor;
import com.ruoyi.project.monitor.service.ISysLogininforService;
import com.ruoyi.project.process.domian.ProcAdviceStrategy;
import com.ruoyi.project.process.domian.ProcSignature;
import com.ruoyi.project.process.service.IProcAdviceStrategyService;
import com.ruoyi.project.process.service.IProcSignatureService;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.workflow.domain.EngineProcActInst;
import com.ruoyi.project.workflow.service.IEngineProcActInstService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-13 14:38 - 星期五
 * @package: com.ruoyi.project.business.controller
 * @JDK-Version : 1.8
 */
@Slf4j
@RestController
@RequestMapping("report-examine")
public class ReportExampleFlowController extends BaseController {
    @Resource
    private IProcSignatureService iProcSignatureService;
    @Resource
    private IProcAdviceStrategyService iProcAdviceStrategyService;
    @Resource
    private IEngineProcActInstService iEngineProcActInstService;
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private ISysDeptService iSysDeptService;

    @Resource
    private BusinessRepExamService businessRepExamService;
    @Resource
    private BusinessRequestReportService businessRequestReportService;
    @Resource
    private ISysLogininforService logininforService;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;
    @Resource
    private RedisCache redisCache = new RedisCache();

//    @GetMapping("get-list")
//    public TableDataInfo getList(BusinessReportExamine queryParam){
//
//        return getDataTable(null);
//    }

    @PostMapping("form-save")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult formSave(@RequestBody Map<String, Object> parameterMap) {

        BusinessReportExamine promobj = JSON.parseObject(JSON.toJSONString(parameterMap.get("entity")), BusinessReportExamine.class);
        boolean flag = true;
        String rowId = promobj.getRowId();
        BusinessReportExamine beanDest = null;
        String adviceWord = " ";
        String opt = "save";
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        String saveIds = "";
        String time = DateUtils.getTime();
        Date date = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", time);
        if (StringUtils.isNotEmpty(rowId)) {
            // 更新操作
            beanDest = businessRepExamService.getById(rowId);
            String title = beanDest.getTitle();
            beanDest.setUpdateTime(date);
            BeanUtils.copyProperties(promobj, beanDest);
            beanDest.setTitle(title);
        } else {
            // 保存操作
            beanDest = promobj;
            beanDest.setRowId(null);
            beanDest.setCreateUserId(user.getUserName());
            beanDest.setCreateUserName(user.getNickName());
            SysDept dept = iSysDeptService.selectDeptById(user.getDeptId());
            beanDest.setCreateDeptId(String.valueOf(dept.getDeptId()));
            beanDest.setCreateDeptName(dept.getDeptName());
            beanDest.setCreateTime(date);
            beanDest.setUpdateTime(date);
            beanDest.setActName(StringUtils.getString(parameterMap.get("currentActName")));
            beanDest.setAtid(StringUtils.getString(parameterMap.get("currentActId")));
            beanDest.setPtid(StringUtils.getString(parameterMap.get("processKey")));
            beanDest.setBusinessKey(StringUtils.getString(parameterMap.get("businessKey")));
            beanDest.setPiid(StringUtils.getString(parameterMap.get("processInstId")));
            beanDest.setTitle("报告出具审核（" + user.getNickName() + "）");
            beanDest.setDeletes("0");
        }
        try {
            String createUserId = user.getUserName();
            String createUserName = user.getNickName();
            // 1.更新对应的附件列表的ID
            String fwDestId = beanDest.getRowId();
            String ptid = StringUtils.getString(parameterMap.get("processKey"));
            String piid = StringUtils.getString(parameterMap.get("processInstId"));
            String atid = StringUtils.getString(parameterMap.get("currentActId"));
            String selectedUserNames = StringUtils.getString(parameterMap.get("selectedUserNames"));
            String currentActName = StringUtils.getString(parameterMap.get("currentActName"));
            String blfs = StringUtils.getString(parameterMap.get("blfs"));
            // 只有当第一次 启动流程并提交时才会 触发 redis ， 这样可以避免 用户 生成之后不提交流程出现占号现象
            if (StringUtils.isEmpty(beanDest.getRowId())) {
                if (businessRepExamService.save(beanDest)) {
                    businessRequestReportService.changeReportStartStatus(beanDest.getReportId());
                }
            } else {
                businessRepExamService.updateById(beanDest);
                String reportId = beanDest.getReportId();

                BusinessRequestReport reportBean = businessRequestReportService.getById(reportId);
                reportBean.setIsReset("0");
                // 1. 判断如果当前流程为最后一步 那么就 将现在临时存在的编号的 isUse属性设置为1 证明已经用在合同中了
                List<EngineProcActInst> engineProcActInstList = (iEngineProcActInstService.list(
                        new QueryWrapper<EngineProcActInst>()
                                .lambda()
                                .eq(EngineProcActInst::getProcInstId, piid)
                                .eq(EngineProcActInst::getEnStatus, "running")));
                // 不存在正在运行的活动实例则为结束
                if (CollectionUtils.isEmpty(engineProcActInstList)) {
                    reportBean.setIsPrint("1");
                    reportBean.setUpdateTime(new Date());
                }
                // 2. 如果返回了申请人
                else {
                    EngineProcActInst actInst = engineProcActInstList.get(0);
                    if ("shenqing".equals(actInst.getActInsId()) || "sqr".equals(actInst.getActInsId())) {
                        log.info("[{}]:报告返回了申请人部分" , reportId);
                        reportBean.setIsReset("1");
                    }
                }
                businessRequestReportService.updateById(reportBean);
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
                    signature.setTableName("business_report_examine");
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
                } else {
                    fillUserName = "办结归档";
                }
            } else if (blfs.equals("3")) {
                fillUserName += "检验报告审核" + selectedUserNames;

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

    @GetMapping("get-info")
    public AjaxResult getInfo(@RequestParam(value = "rowId") String rowId) {
        return AjaxResult.success(businessRepExamService.getById(rowId));
    }

    @GetMapping("get-info-by-reportId")
    public AjaxResult getInfoByReportId(@RequestParam("reportId") String reportId) {
        return AjaxResult.success(businessRepExamService.getOne(
                new QueryWrapper<BusinessReportExamine>()
                        .lambda()
                        .eq(BusinessReportExamine::getReportId, reportId))
        );
    }

    @PutMapping("set-notice-cache")
    public AjaxResult setNoticeCache() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        final String redisKey = RedisConstantEnum.CURRENT_LOGIN_USER_REDIS_NOTICE.getRedisKey() + loginUser.getUsername();
        if (redisCache.getCacheObject(redisKey) == null) {
            redisCache.setCacheObject(redisKey, "isNotice", expireTime, TimeUnit.MINUTES);
            return AjaxResult.success("写入成功");
        }
        return AjaxResult.success("已经存在");
    }

    @GetMapping("get-end-login-info")
    public AjaxResult getEndLoginInfo() {
        String loginUserName = SecurityUtils.getLoginUser().getUsername();
        SysLogininfor createTempBean = new SysLogininfor();
        createTempBean.setUserName(loginUserName);
        createTempBean.setMsg("登录成功");
        startPage();
        List<SysLogininfor> sysLogininfors = logininforService.selectLogininforList(createTempBean);
        if (sysLogininfors.size() == 1 || redisCache.getCacheObject(RedisConstantEnum.CURRENT_LOGIN_USER_REDIS_NOTICE.getRedisKey() + loginUserName) != null) { // 第一次登录 ， 或者已经通知过了
            return AjaxResult.success();
        }
        Date lastLoginTime = sysLogininfors.get(1).getLoginTime();
        ReportMessageVo resultVo = ReportMessageVo.createVoidInstance();

        List<BusinessRequestReport> newChangeReportList = businessRequestReportService.list(new QueryWrapper<BusinessRequestReport>()
                .lambda()
                .eq(BusinessRequestReport::getCreateUserId, loginUserName)
                .ge(BusinessRequestReport::getUpdateTime, lastLoginTime)
                .eq(BusinessRequestReport::getIsPrint, "1"));
        if (newChangeReportList.size() == 0) {   // 只显示登录时间
            resultVo.setLastLoginTime(lastLoginTime);
            return AjaxResult.success(resultVo);
        }
        resultVo.setLastLoginTime(lastLoginTime);
        List<String> deviceNumbers = newChangeReportList
                .stream()
                .map(BusinessRequestReport::getBusinessDeviceNumber)
                .collect(Collectors.toList());
        resultVo.setReportNumbers(deviceNumbers);
        return AjaxResult.success(resultVo);    // 登录时间和新增报告数
    }
}
