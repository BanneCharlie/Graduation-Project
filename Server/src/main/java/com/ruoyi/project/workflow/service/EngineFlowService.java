package com.ruoyi.project.workflow.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.process.domian.ProcAdviceStrategy;
import com.ruoyi.project.process.domian.ProcSignature;
import com.ruoyi.project.process.service.IProcAdviceStrategyService;
import com.ruoyi.project.process.service.IProcSignatureService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.workflow.domain.*;
import com.ruoyi.project.workflow.vo.NextStepVo;
import com.ruoyi.project.workflow.vo.ProcRunVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author niminui
 * @date 2021/5/25 11:28
 */
@Service
@SuppressWarnings("all")
public class EngineFlowService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final boolean AUTO = true;
    private static final String AUTO_NAME = "WFUser";
    private static final String AUTO_KEY = "jtusnpj8yp";
    //private static final String URL = "http://192.168.106.41:7003/20/WFProxy/engine";  //外网测试环境
    private static final String team_code = "NJDTOA";  //
    private static final String team_key = "NJDTOA";  //

    @Resource
    private IProcAdviceStrategyService IProcAdviceStrategyService;
    @Resource
    private IProcSignatureService IProcSignatureService;


    @Resource
    private IEngineProcActDefService IEngineProcActDefService;
    @Resource
    private IEngineProcActDefRelationService IEngineProcActDefRelationService;
    @Resource
    private IEngineProcUserorgService IEngineProcUserorgService;
    @Resource
    private IEngineProcDefService IEngineProcDefService;
    @Resource
    private IEngineProcInstService IEngineProcInstService;
    @Resource
    private IEngineProcActInstService IEngineProcActInstService;
    @Resource
    private IEngineProcWorkitemService IEngineProcWorkitemService;
    @Resource
    private ISysDeptService iSysDeptService;

    /**
     * 01获取流程启动第一步的下一步步骤与参与者
     *
     * @param procDefId 流程定义编号
     * @param user      用户参数
     * @return ResultEntity<List < NextStepVo>> 下一步操作连线LIST包含下一步参与者
     */
    public List<NextStepVo> queryWorkflowNextStepsStartByProcDefId(String procDefId, SysUser user) {
        //初始化VO
        List<NextStepVo> list = new ArrayList<>();
        //查找当前流程定义中类别为start的起始节点。
        QueryWrapper<EngineProcActDef> epQuery = new QueryWrapper<>();
        epQuery.lambda()
                .eq(EngineProcActDef::getProcDefId, procDefId)
                .eq(EngineProcActDef::getEnType, "start");
        EngineProcActDef engineProcActDef = IEngineProcActDefService.getOne(epQuery);
        System.out.println("当前流程定义中类别为start的起始节点：" + engineProcActDef);
        //根据起始点寻找起始点下一步参与者（流程未启动）
        String startActDefId = engineProcActDef.getActDefId();
        List<EngineProcActDefRelation> relationList =
                IEngineProcActDefRelationService.getEngineProcActDefRelationList(procDefId, startActDefId);
        for (EngineProcActDefRelation edr : relationList) {
            NextStepVo vo = new NextStepVo();
            vo.setCurrentActDefId(startActDefId);
            vo.setEnId(edr.getEnId());
            vo.setProcDefId(procDefId);
            vo.setNextActDefId(edr.getActDefId());
            vo.setNextActName(edr.getActName());
            List<EngineProcUserorg> engineProcUserOrgList =
                    IEngineProcUserorgService.getEngineProcUserOrgList(procDefId, edr.getActDefId());
            vo.setStep(engineProcUserOrgList);//待选人员列表
            list.add(vo);
        }
        System.out.println("返回的数据 " + list);
        return list;
    }

    @Resource
    private ISysUserService iSysUserService;
    /**
     * 02获取已经运行的流程实例的下一步参与者
     *
     * @param piid 流程实例ID
     * @param user 用户参数
     * @return ResultEntity<List < NextStepVo>> 下一步操作连线LIST包含下一步参与者
     */
    public List<NextStepVo> queryWorkflowNextStepsByProcInstd(String piid, String ptid, SysUser user) {
        //初始化VO
        List<NextStepVo> list = new ArrayList<>();
        QueryWrapper<EngineProcActInst> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcActInst::getProcInstId, piid)
                .eq(EngineProcActInst::getEnStatus, "running");
        EngineProcActInst engineProcActInst = IEngineProcActInstService.getOne(queryWrapper);
        if (engineProcActInst != null) {
            //根据起始点寻找起始点下一步参与者（流程启动）
            String startActDefId = engineProcActInst.getActInsId();
            List<EngineProcActDefRelation> relationList =
                    IEngineProcActDefRelationService.getEngineProcActDefRelationList(ptid, startActDefId);
            for (EngineProcActDefRelation edr : relationList) {
                NextStepVo vo = new NextStepVo();
                vo.setCurrentActDefId(startActDefId);
                vo.setEnId(edr.getEnId());
                vo.setProcDefId(ptid);
                vo.setNextActDefId(edr.getActDefId());
                vo.setNextActName(edr.getActName());
                vo.setNodeLineName(Objects.nonNull(edr.getNodeLineName()) ? edr.getNodeLineName() : edr.getActName());

                String actDefId = edr.getActDefId();
                List<EngineProcUserorg> engineProcUserOrgList = new ArrayList<>();
                // 返回到发起人 或者返回到开始节点就自动填充人员为发起流程的人员
                if ("fqr".equals(actDefId) || "shenqing".equals(actDefId) || "kaishi".equals(actDefId)){    // 获取流转到发起人步骤 ， 或者申请人
                    // 根据Piid 获取当前流程的流程实例并得到发起人
                    EngineProcInst processIntance = IEngineProcInstService.getById(engineProcActInst.getProcInstId());

                    SysUser proInstanceCreateUser = iSysUserService.selectUserByUserName(processIntance.getCreateUserId());
                    EngineProcUserorg engineProcUserorg = new EngineProcUserorg();
                    engineProcUserorg.setActDefId(engineProcActInst.getActInsId());
                    engineProcUserorg.setCreateTime(new Date());
                    engineProcUserorg.setProcDefId(engineProcActInst.getProcDefId());
                    engineProcUserorg.setUserId(String.valueOf(proInstanceCreateUser.getUserId()));
                    engineProcUserorg.setUserName(proInstanceCreateUser.getNickName());
                    engineProcUserorg.setOrgName(iSysDeptService.selectDeptById(user.getDeptId()).getDeptName());

                    engineProcUserOrgList.add(engineProcUserorg);
                }else {
                    engineProcUserOrgList = IEngineProcUserorgService.getEngineProcUserOrgList(ptid, actDefId);
                }
                vo.setStep(engineProcUserOrgList);//待选人员列表
                list.add(vo);
            }
        }
        return list;
    }

    /**
     * 02根据procDefId获取流程定义
     *
     * @param procDefId 流程应用编码
     * @param user      流程当前用户session对象
     * @return EngineProcDef 流程定义实体类
     */
    public EngineProcDef getDefinitionByProcDefId(String procDefId, SysUser user) {
        QueryWrapper<EngineProcDef> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcDef::getProcDefId, procDefId);
        return IEngineProcDefService.getOne(queryWrapper);
    }

    /**
     * 03根据procInstId获取流程实例ID
     *
     * @param InstId 流程实例ID
     * @return EngineProcInst 流程实例对象
     */
    public EngineProcInst getProcInstByProcInstId(String InstId) {
        return IEngineProcInstService.getById(InstId);
    }

    /**
     * 03根据ProcDefId获取流程起始ACTID
     *
     * @param ProcDefId 流程定义ID
     * @return EngineProcActDef 流程节点对象
     */
    public EngineProcActDef getStartActByProcDefId(String ProcDefId) {
        QueryWrapper<EngineProcActDef> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcActDef::getProcDefId, ProcDefId)
                .eq(EngineProcActDef::getEnType, "start");
        return IEngineProcActDefService.getOne(queryWrapper);
    }

    /**
     * 04根据ProcInstId获取待办人员清单
     *
     * @param ProcInstId 流程定义ID
     * @return list 用户列表
     */
    public List<EngineProcWorkitem> getTaskUsersByProcInstId(String ProcInstId) {
        QueryWrapper<EngineProcWorkitem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcWorkitem::getProcInstId, ProcInstId)
                .eq(EngineProcWorkitem::getEnStatus, "running");
        return IEngineProcWorkitemService.list(queryWrapper);
    }

    /**
     * 04根据ProcInstId获取待办人员清单
     *
     * @param ProcInstId 流程定义ID
     * @return list 用户列表
     */
    public EngineProcWorkitem getWorkItemByProcInstId(String ProcInstId, String taskId) {
        QueryWrapper<EngineProcWorkitem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EngineProcWorkitem::getProcInstId, ProcInstId)
                .eq(EngineProcWorkitem::getActInsId, taskId);
        return IEngineProcWorkitemService.getOne(queryWrapper);
    }

    /**
     * 03根据procDefId手持启动流程产生piid
     *      第一次运行
     * @param procDefId 流程应用编码
     * @param nextActId 下一步选择节点ID
     * @param users     Session登录用户
     * @param fromTitle 表单标题
     * @param fromTitle 表单标题
     * @return String piid 表单类别
     */
    public ProcRunVo startProcessByProcDefId(String procDefId, String nextActId, List<SysUser> users, SysUser user, String fromTitle, String fromType) {
        ProcRunVo vo = new ProcRunVo();
        String piid = null;
        EngineProcInst inst = new EngineProcInst();
        //Step1 读取流程定义信息
        EngineProcDef engineProcDef = IEngineProcDefService.getOne(new QueryWrapper<EngineProcDef>().
                lambda()
                .eq(EngineProcDef::getProcDefId, procDefId));
        //step2 读取流程定义节点
        EngineProcActDef sourceAct = IEngineProcActDefService.getOne(new QueryWrapper<EngineProcActDef>()
                .lambda()
                .eq(EngineProcActDef::getProcDefId, procDefId)
                .eq(EngineProcActDef::getEnType, "start"));
        EngineProcActDef nextAct = IEngineProcActDefService.getOne(new QueryWrapper<EngineProcActDef>()
                .lambda()
                .eq(EngineProcActDef::getProcDefId, procDefId)
                .eq(EngineProcActDef::getActDefId, nextActId));
        /**
         * 开始创建流程实例
         */
        if (sourceAct != null && nextAct != null) {
            inst.setEnId(null);
            inst.setCreateTime(new Date());
            inst.setCreateUserId(user.getUserName());
            inst.setCurrActId(nextAct.getActDefId());
            inst.setCurrActName(nextAct.getActName());
            inst.setProcInstId(null);//数据库触发器+sequence序列自动生成 判断条件为NULL
            inst.setProcName(engineProcDef.getProcName());
            inst.setEnStatus("running");//设置实例启动状态
            inst.setFromTitle(fromTitle);
            inst.setFromType(fromType);
            inst.setExt1(procDefId);
            IEngineProcInstService.save(inst);//保存流程实例并获取ID
            piid = inst.getEnId();
            logger.info("piid:" + piid);
            //创建流程节点
            //--------------------------------------------------------------------------------
            EngineProcActInst souraceActInst = new EngineProcActInst();//首先完成第一步活动节点
            souraceActInst.setEnId(null);
            souraceActInst.setCreateTime(new Date());
            souraceActInst.setEnStatus("finish");//标记完成
            souraceActInst.setProcInstId(inst.getEnId());//关联流程实例ID
            souraceActInst.setCreateUserId(user.getUserName());
            souraceActInst.setProcDefId(engineProcDef.getProcDefId());
            souraceActInst.setProcDefName(engineProcDef.getProcName());
            souraceActInst.setActName(sourceAct.getActName());
            souraceActInst.setEnType(sourceAct.getEnType());
            souraceActInst.setEnOrder(sourceAct.getEnOrder());
            souraceActInst.setActInsId(sourceAct.getActDefId());

            //--------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------
            EngineProcActInst nextActInst = new EngineProcActInst();//创建第二步活动节点
            nextActInst.setEnId(null);
            nextActInst.setCreateTime(new Date());
            nextActInst.setEnStatus("running");//标记完成
            nextActInst.setProcInstId(inst.getEnId());//关联流程实例ID
            nextActInst.setCreateUserId(user.getUserName());
            nextActInst.setProcDefId(engineProcDef.getProcDefId());
            nextActInst.setProcDefName(engineProcDef.getProcName());
            nextActInst.setActName(nextAct.getActName());
            nextActInst.setEnType(nextAct.getEnType());
            nextActInst.setEnOrder(nextAct.getEnOrder());
            nextActInst.setActInsId(nextAct.getActDefId());
            if (StringUtils.isEmpty(nextActInst.getEnId())) {
                IEngineProcActInstService.save(nextActInst);
            } else {
                IEngineProcActInstService.updateById(nextActInst);
            }
            souraceActInst.setOutActsId(nextActInst.getEnId());//第一步活动节点记录输出第二部流程实例节点的id
            IEngineProcActInstService.save(souraceActInst);
            if (StringUtils.isEmpty(souraceActInst.getEnId())) {
                IEngineProcActInstService.save(souraceActInst);
            } else {
                IEngineProcActInstService.updateById(souraceActInst);
            }

            //--------------------------------------------------------------------------------
            EngineProcWorkitem workitemfirst = new EngineProcWorkitem();//创建第一步得工作项
            workitemfirst.setCreateUserId(user.getUserName());
            workitemfirst.setWorkItemUserName(user.getNickName());
            workitemfirst.setUserId(user.getUserName());
            workitemfirst.setOrgId(String.valueOf(user.getDeptId()));
            workitemfirst.setOrgName(iSysDeptService.selectDeptById(user.getDeptId()).getDeptName());
            workitemfirst.setEnId(null);
            workitemfirst.setCreateTime(new Date());
            workitemfirst.setEnStatus("finish");
            workitemfirst.setFromTitle(fromTitle);
            workitemfirst.setFromType(fromType);
            workitemfirst.setEnType(sourceAct.getEnType());
            workitemfirst.setGiveUserId(user.getUserName());
            workitemfirst.setGiveUserName(user.getNickName());
            workitemfirst.setProcDefId(engineProcDef.getProcDefId());
            workitemfirst.setProcDefName(engineProcDef.getProcName());
            workitemfirst.setActInsId(souraceActInst.getEnId());
            workitemfirst.setActInsName(sourceAct.getActName());
            workitemfirst.setProcInstId(inst.getEnId());
            IEngineProcWorkitemService.save(workitemfirst);

            //01检测用户是否选择人员 开始创建工作项
            if (users.size() > 0) {
                for (SysUser u : users) {
                    EngineProcWorkitem workitem = new EngineProcWorkitem();
                    workitem.setCreateTime(new Date());
                    workitem.setEnId(null);
                    workitem.setOrgId(String.valueOf(u.getDeptId()));
                    workitem.setOrgName(iSysDeptService.selectDeptById(u.getDeptId()).getDeptName());
                    workitem.setUserId(u.getUserName());
                    workitem.setWorkItemUserName(u.getNickName());
                    workitem.setCreateUserId(u.getUserName());
                    workitem.setProcInstId(inst.getEnId());
                    workitem.setActInsId(nextActInst.getEnId());
                    workitem.setProcDefName(engineProcDef.getProcName());
                    workitem.setProcDefId(engineProcDef.getProcDefId());
                    workitem.setActInsName(nextAct.getActName());
                    workitem.setGiveUserId(user.getUserName());
                    workitem.setGiveUserName(user.getNickName());
                    workitem.setEnType(nextAct.getEnType());
                    workitem.setFromTitle(fromTitle);
                    workitem.setFromType(fromType);
                    workitem.setEnStatus("running");//设置运行状态
                    IEngineProcWorkitemService.save(workitem);
                }
            }
        }
        vo.setEngineProcDef(engineProcDef);
        vo.setProcInstId(piid);
        vo.setRe(true);
        logger.info("成功启动流程：" + piid);
        return vo;
    }

    /**
     * 03根据procInstId运行流程
     *      根据流程实例来运行流程
     * @param piid      流程应用编码
     * @param nextActId 下一步选择节点ID
     * @param users     Session登录用户
     * @param fromTitle 表单标题
     * @param fromTitle 表单标题
     * @return String piid 表单类别
     */
    public ProcRunVo runProcessByProcInstId(String piid, String nextActId, List<SysUser> users, SysUser user, String fromTitle, String fromType) {

        logger.info("into runProcessByProcInstId");
        ProcRunVo vo = new ProcRunVo();
        //获取流程实例
        EngineProcInst inst = IEngineProcInstService.getById(piid);
        //Step1 读取流程定义信息
        EngineProcDef engineProcDef = IEngineProcDefService.getOne(new QueryWrapper<EngineProcDef>()
                .lambda()
                .eq(EngineProcDef::getProcDefId, inst.getExt1()));

        //step2 读取流程实例节点
        EngineProcActInst currentActInst = IEngineProcActInstService.getOne(new QueryWrapper<EngineProcActInst>()
                .lambda()
                .eq(EngineProcActInst::getProcInstId, piid)
                .eq(EngineProcActInst::getEnStatus, "running"));

        EngineProcActDef nextAct = IEngineProcActDefService.getOne(new QueryWrapper<EngineProcActDef>()
                .lambda()
                .eq(EngineProcActDef::getProcDefId, engineProcDef.getProcDefId())
                .eq(EngineProcActDef::getActDefId, nextActId));

        //获取当前工作项
        EngineProcWorkitem currentWorkItem = IEngineProcWorkitemService.getOne(new QueryWrapper<EngineProcWorkitem>()
                .lambda()
                .eq(EngineProcWorkitem::getProcInstId, piid)
                .eq(EngineProcWorkitem::getActInsId, currentActInst.getEnId())
                .eq(EngineProcWorkitem::getUserId, user.getUserName()));

        /**
         * 开始运行流程实例
         */
        //1、是结束步骤
        if (nextActId.equals("end") || nextActId.equals("jieshu")) {
            //----------------------------------01更新流程实例
            inst.setUpdateTime(new Date());
            inst.setModifyUserId(user.getUserName());
            inst.setCurrActId(nextAct.getActDefId());
            inst.setCurrActName(nextAct.getActName());
            inst.setEnStatus("finish");//设置实例完成状态

            inst.setFromType(fromType);
            if (StringUtils.isEmpty(inst.getEnId())) {
                IEngineProcInstService.save(inst);
            } else {
                IEngineProcInstService.updateById(inst);//保存/更新流程实例并获取ID
            }

            //----------------------------------02更新当前完成节点
            currentActInst.setEnStatus("finish");
            currentActInst.setUpdateTime(new Date());
            currentActInst.setModifyUserId(user.getUserName());

            //----------------------------------03更新当前完成工作项
            currentWorkItem.setEnStatus("finish");
            currentWorkItem.setUpdateTime(new Date());
            currentWorkItem.setModifyUserId(user.getUserName());
            currentWorkItem.setFromType(fromType);
            currentWorkItem.setFromTitle(fromTitle);
            if (StringUtils.isEmpty(currentWorkItem.getEnId())) {//更新完成工作项
                IEngineProcWorkitemService.save(currentWorkItem);
            } else {
                IEngineProcWorkitemService.updateById(currentWorkItem);
            }
            //----------------------------------04创建下一步流程节点
            EngineProcActInst nextActInst = new EngineProcActInst();//首先完成第一步活动节点
            nextActInst.setEnId(null);
            nextActInst.setCreateTime(new Date());
            nextActInst.setEnStatus("finish");//标记运行
            nextActInst.setProcInstId(inst.getEnId());//关联流程实例ID
            nextActInst.setCreateUserId(user.getUserName());
            nextActInst.setProcDefId(engineProcDef.getProcDefId());
            nextActInst.setProcDefName(engineProcDef.getProcName());
            nextActInst.setActName(nextAct.getActName());
            nextActInst.setEnType(nextAct.getEnType());
            nextActInst.setEnOrder(nextAct.getEnOrder());
            nextActInst.setActInsId(nextAct.getActDefId());
            if (StringUtils.isEmpty(nextActInst.getEnId())) {
                IEngineProcActInstService.save(nextActInst);
            } else {
                IEngineProcActInstService.updateById(nextActInst);
            }
            currentActInst.setOutActsId(nextActInst.getEnId());
            if (StringUtils.isEmpty(currentActInst.getEnId())) {
                IEngineProcActInstService.save(currentActInst);
            } else {
                IEngineProcActInstService.updateById(currentActInst);
            }


            EngineProcWorkitem workitem = new EngineProcWorkitem();
            workitem.setCreateTime(new Date());
            workitem.setEnId(null);
            workitem.setProcInstId(inst.getEnId());
            workitem.setActInsId(nextActInst.getEnId());
            workitem.setProcDefName(engineProcDef.getProcName());
            workitem.setProcDefId(engineProcDef.getProcDefId());
            workitem.setActInsName(nextAct.getActName());
            workitem.setGiveUserId(user.getUserName());
            workitem.setGiveUserName(user.getNickName());
            workitem.setEnType(nextAct.getEnType());
            workitem.setFromTitle(fromTitle);
            workitem.setFromType(fromType);
            workitem.setEnStatus("finish");//设置结束状态
            IEngineProcWorkitemService.save(workitem);
        } else if (nextActId.equals("完成任务")) {
            //----------------------------------02更新当前完成节点
//            currentActInst.seteStatus("finish");
//            currentActInst.setUpdateTime(new Date());
//            currentActInst.setModifyUserId(user.getLogonName());
//            engineProcActInstManager.save(currentActInst);//完成当前任务节点
            //----------------------------------03更新当前完成工作项
            currentWorkItem.setEnStatus("finish");
            currentWorkItem.setUpdateTime(new Date());
            currentWorkItem.setModifyUserId(user.getUserName());
            currentWorkItem.setFromType(fromType);
            currentWorkItem.setFromTitle(fromTitle);

            if (StringUtils.isEmpty(currentWorkItem.getEnId())) {
                IEngineProcWorkitemService.save(currentWorkItem);//更新完成工作项
            } else {
                IEngineProcWorkitemService.updateById(currentWorkItem);
            }

        } else {
            if (currentActInst != null && nextAct != null) {
                logger.info("始运行流程实例");
                //01 最后节点非结束节点
                //----------------------------------01更新流程实例
                inst.setUpdateTime(new Date());
                inst.setModifyUserId(user.getUserName());
                inst.setCurrActId(nextAct.getActDefId());
                inst.setCurrActName(nextAct.getActName());
                inst.setEnStatus("running");//设置实例启动状态

                inst.setFromType(fromType);
                if (StringUtils.isEmpty(inst.getEnId())) {
                    IEngineProcInstService.save(inst);
                } else {
                    IEngineProcInstService.updateById(inst);//保存/更新流程实例并获取ID
                };//保存/更新流程实例并获取ID
                //----------------------------------02更新当前完成节点
                currentActInst.setEnStatus("finish");
                currentActInst.setUpdateTime(new Date());
                currentActInst.setModifyUserId(user.getUserName());

                //----------------------------------03更新当前完成工作项
                currentWorkItem.setEnStatus("finish");
                currentWorkItem.setUpdateTime(new Date());
                currentWorkItem.setModifyUserId(user.getUserName());
                currentWorkItem.setFromType(fromType);
                currentWorkItem.setFromTitle(fromTitle);
                if (StringUtils.isEmpty(currentWorkItem.getEnId())) {
                    IEngineProcWorkitemService.save(currentWorkItem);//更新完成工作项
                } else {
                    IEngineProcWorkitemService.updateById(currentWorkItem);
                }
                //----------------------------------04创建下一步流程节点
                EngineProcActInst nextActInst = new EngineProcActInst();//首先完成第一步活动节点
                nextActInst.setEnId(null);
                nextActInst.setCreateTime(new Date());
                nextActInst.setEnStatus("running");//标记运行
                nextActInst.setProcInstId(inst.getEnId());//关联流程实例ID
                nextActInst.setCreateUserId(user.getUserName());
                nextActInst.setProcDefId(engineProcDef.getProcDefId());
                nextActInst.setProcDefName(engineProcDef.getProcName());
                nextActInst.setActName(nextAct.getActName());
                nextActInst.setEnType(nextAct.getEnType());
                nextActInst.setEnOrder(nextAct.getEnOrder());
                nextActInst.setActInsId(nextAct.getActDefId());
                if (StringUtils.isEmpty(nextActInst.getEnId())) {
                    IEngineProcActInstService.save(nextActInst);
                } else {
                    IEngineProcActInstService.updateById(nextActInst);
                }
                currentActInst.setOutActsId(nextActInst.getEnId());
                if (StringUtils.isEmpty(nextActInst.getEnId())) {//完成当前任务节点
                    IEngineProcActInstService.save(currentActInst);
                } else {
                    IEngineProcActInstService.updateById(currentActInst);
                }
                //----------------------------------05创建一下一步工作项
                if (users.size() > 0) {
                    for (SysUser u : users) {
                        EngineProcWorkitem workitem = new EngineProcWorkitem();
                        workitem.setCreateTime(new Date());
                        workitem.setEnId(null);
                        workitem.setOrgId(String.valueOf(u.getDeptId()));
                        workitem.setOrgName(iSysDeptService.selectDeptById(u.getDeptId()).getDeptName());
                        workitem.setUserId(u.getUserName());
                        workitem.setWorkItemUserName(u.getNickName());
                        workitem.setCreateUserId(u.getUserName());
                        workitem.setProcInstId(inst.getEnId());
                        workitem.setActInsId(nextActInst.getEnId());
                        workitem.setProcDefName(engineProcDef.getProcName());
                        workitem.setProcDefId(engineProcDef.getProcDefId());
                        workitem.setActInsName(nextAct.getActName());
                        workitem.setGiveUserId(user.getUserName());
                        workitem.setGiveUserName(user.getNickName());
                        workitem.setEnType(nextAct.getEnType());
                        workitem.setFromTitle(fromTitle);
                        workitem.setFromType(fromType);
                        workitem.setEnStatus("running");//设置运行状态
                        IEngineProcWorkitemService.save(workitem);
                    }
                }
            }
        }

        vo.setEngineProcDef(engineProcDef);
        vo.setProcInstId(piid);
        vo.setRe(true);
        logger.info("成功启动流程：" + piid);
        return vo;
    }

    /**
     * 流程撤回
     *
     * @param eid
     * @return
     */
    public String pushBack(String eid) {
        String result = "撤回成功！";
        EngineProcWorkitem procWorkitem = IEngineProcWorkitemService.getById(eid);
        EngineProcActInst currentEngineProcActInst = IEngineProcActInstService.getById(procWorkitem.getActInsId());//找出实例节点
        //找到该节点的下一步骤节点
        if (currentEngineProcActInst.getOutActsId() != null) {
            EngineProcActInst nextEngineProcActInst = IEngineProcActInstService.getById(currentEngineProcActInst.getOutActsId());
            if (nextEngineProcActInst.getOutActsId() != null) {
                result = "下一人员已经完成任务，请联系下一步人员，先行撤回！";
            } else {
                //删除一步骤工作项,多人办理时，检查是否有人已经办结
                List<EngineProcWorkitem> nextEngineProcWorkItems = IEngineProcWorkitemService.list(new QueryWrapper<EngineProcWorkitem>()
                        .lambda()
                        .eq(EngineProcWorkitem::getActInsId, nextEngineProcActInst.getEnId()));
                for (EngineProcWorkitem workItem : nextEngineProcWorkItems) {
                    if (!workItem.getEnType().equals("end") && workItem.getEnStatus().equals("finish")) {
                        result = "下一人员已经完成任务，请联系下一步人员，先行撤回！";
                        break;
                    }
                }
                if (!result.equals("下一人员已经完成任务，请联系下一步人员，先行撤回！")) {
                    for (EngineProcWorkitem workitem : nextEngineProcWorkItems) {
                        IEngineProcWorkitemService.removeById(workitem.getEnId());
                    }
                    IEngineProcActInstService.removeById(nextEngineProcActInst.getEnId());//下一步骤删除
                    //改变当前节点状态
                    currentEngineProcActInst.setOutActsId(null);
                    currentEngineProcActInst.setEnStatus("running");
                    if (StringUtils.isEmpty(currentEngineProcActInst.getEnId())) {
                        IEngineProcActInstService.save(currentEngineProcActInst);
                    } else {
                        IEngineProcActInstService.updateById(currentEngineProcActInst);
                    }
                    procWorkitem.setEnStatus("running");
                    IEngineProcWorkitemService.updateById(procWorkitem);
                    //删除意见及办理过程
                    List<ProcAdviceStrategy> procAdviceStrategyList = IProcAdviceStrategyService.list(new QueryWrapper<ProcAdviceStrategy>()
                            .lambda()
                            .eq(ProcAdviceStrategy::getAiid, procWorkitem.getActInsId()));
                    for (ProcAdviceStrategy p : procAdviceStrategyList) {
                        IProcAdviceStrategyService.removeById(p.getAdviceStrategyId());
                    }
                    List<ProcSignature> signatureList = IProcSignatureService.list(new QueryWrapper<ProcSignature>()
                            .lambda()
                            .eq(ProcSignature::getAiid, procWorkitem.getActInsId()));
                    for (ProcSignature s : signatureList) {
                        IProcSignatureService.removeById(s.getSignatureId());
                    }
                }
            }
        } else {
            //撤回多人办理的情况下。已经完成的人的步骤
            procWorkitem.setEnStatus("running");
            //删除意见及办理过程
            List<ProcAdviceStrategy> procAdviceStrategyList = IProcAdviceStrategyService.list(new QueryWrapper<ProcAdviceStrategy>()
                    .lambda().eq(ProcAdviceStrategy::getAiid, procWorkitem.getActInsId()));
            for (ProcAdviceStrategy p : procAdviceStrategyList) {
                IProcAdviceStrategyService.removeById(p.getAdviceStrategyId());
            }
            List<ProcSignature> signatureList = IProcSignatureService.list(new QueryWrapper<ProcSignature>()
                    .lambda()
                    .eq(ProcSignature::getAiid, procWorkitem.getActInsId()));
            for (ProcSignature s : signatureList) {
                IProcSignatureService.removeById(s.getSignatureId());
            }
            IEngineProcWorkitemService.save(procWorkitem);
        }
        return result;
    }

    /**
     * 根据流程实例id获取开始节点
     *
     * @param piId
     * @return
     */
    public EngineProcWorkitem getFirstWorkItem(String piId) {
        List<EngineProcWorkitem> procWorkItemTemp = IEngineProcWorkitemService.list(new QueryWrapper<EngineProcWorkitem>()
                .lambda()
                .eq(EngineProcWorkitem::getProcInstId, piId)
                .eq(EngineProcWorkitem::getEnType, "start"));
        EngineProcWorkitem procWorkItem = null;
        if (procWorkItemTemp.size() > 0) {
            procWorkItem = procWorkItemTemp.get(0);
        }
        return procWorkItem;
    }

}
