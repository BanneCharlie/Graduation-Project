package com.ruoyi.project.workflow.ctl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.SysLoginService;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.process.utils.DateUtil;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.template.commons.model.PageParams;
import com.ruoyi.project.template.commons.model.ResultBody;
import com.ruoyi.project.template.util.StringUtil;
import com.ruoyi.project.template.util.StringUtils;
import com.ruoyi.project.workflow.domain.EngineProcActInst;
import com.ruoyi.project.workflow.domain.EngineProcInst;
import com.ruoyi.project.workflow.domain.EngineProcWorkitem;
import com.ruoyi.project.workflow.service.EngineProcWorkItemSaveService;
import com.ruoyi.project.workflow.service.IEngineProcActInstService;
import com.ruoyi.project.workflow.service.IEngineProcInstService;
import com.ruoyi.project.workflow.service.IEngineProcWorkitemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

/**
 * 引擎后台管理控制器-流程实例
 * 
 * @author optimus
 * @version 1.0
 */
@RestController
@RequestMapping("engine-mgmt-inst")
@SuppressWarnings("all")
public class EngineProcInstManagerController {

    private static final Logger logger = LoggerFactory.getLogger(EngineProcInstManagerController.class);

	@Autowired
	private IEngineProcInstService iEngineProcInstService;
	@Autowired
	private IEngineProcActInstService iEngineProcActInstService;
	@Autowired
	private IEngineProcWorkitemService iEngineProcWorkitemService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysUserMapper sysUserMapper;
	/**
	 * 01进入流程实例管理列表
	 *
	 * @return
	 */
	@GetMapping("/process-inst-mgmt")
	public ResultBody goProcessMgmt(
			@RequestParam(required = false) Map map
	) {
		PageParams pageParams = new PageParams(map);
		EngineProcInst query = pageParams.mapToObject(EngineProcInst.class);
		QueryWrapper<EngineProcInst> queryWrapper = new QueryWrapper();
		queryWrapper.lambda()
				.like(StringUtils.isNotBlank(query.getFromTitle()), EngineProcInst::getFromTitle, query.getFromTitle());
		return ResultBody.ok().data(iEngineProcInstService.page(pageParams, queryWrapper));
	}

	/**
	 * 02 流程实例 编辑 回显数据
	 * @return
	 */
    @GetMapping("/process-inst-input")
	public ResultBody definput(
			@RequestParam(value = "rowId") String rowId
	){
		return ResultBody.ok().data(iEngineProcInstService.getById(rowId));
	}
	
	/**
     * 03流程实例删除
	 *
	 * @return
	 */
	@GetMapping("/process-def-remove")
	public ResultBody defRemove(
			@RequestParam("delids") String delids
	){
		String message = "删除成功";
		int statusCode = 200;
		try {
		    if (StringUtil.validateStringNotNull(delids)){
                for (String rowId : delids.split(",")) {
                    iEngineProcInstService.removeById(rowId);
                }
            }
		}catch (Exception e){
			message = "删除失败";
			statusCode = 500;
			logger.error(e.getMessage());
		}

		return ResultBody.ok().msg(message).code(statusCode);
	}


    /**
     * 04流程实例保存/更新
     *
     * @param engineProcInst
     * @return
     */
    @PostMapping("/process-inst-save")
    public ResultBody save (
           EngineProcInst engineProcInst
    ){
        String message = "操作成功";
        int statusCode = 200;

        try {
            EngineProcInst dest = null;
            final SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
            final String requestId = engineProcInst.getEnId();

            if (StringUtils.isNotBlank(requestId)){
                // 更新
                dest = iEngineProcInstService.getById(requestId);
                BeanUtils.copyBeanProp(dest,engineProcInst);
                dest.setCreateTime(new Date());
                dest.setModifyUserId(user.getUserName());
                iEngineProcInstService.updateById(dest);
            }else {
                // 添加
                dest = engineProcInst;
                dest.setEnId(null);
                dest.setProcInstId(null);
                dest.setCreateTime(new Date());
                dest.setCreateUserId(user.getUserName());
                dest.setEnStatus("active");
                iEngineProcInstService.save(dest);
            }
        }catch (Exception e){
            message = "操作失败";
            statusCode = 500;
            logger.error(e.getMessage());
        }

        return ResultBody.ok().msg(message).code(statusCode);
    }

    //------------------------------------------------------------------ 流程节点
    /**
     * 流程实例节点
     */
    @GetMapping("/proc-inst-act-list")
    public ResultBody proceInstActList(
            @RequestParam(required = false) Map map
    ){
        PageParams pageParams = new PageParams(map);
        EngineProcActInst query = pageParams.mapToObject(EngineProcActInst.class);
        QueryWrapper<EngineProcActInst> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(query.getProcInstId()) , EngineProcActInst::getProcInstId , query.getProcInstId())
                .like(StringUtils.isNotBlank(query.getActName()), EngineProcActInst::getActName, query.getActName());

        return ResultBody.ok().data(iEngineProcActInstService.page(pageParams, queryWrapper));
    }

    /**
     * 	 * 流程实例节点编辑 回显数据
     */
    @GetMapping("/process-inst-act-input")
    public ResultBody instActInput(
            @RequestParam(value = "rowId") String rowId
    ){
        return ResultBody.ok().data(iEngineProcActInstService.getById(rowId));
    }

    /**
     * 流程实例节点保存
     */
    @PostMapping("/process-inst-act-save")
    public ResultBody saveInstAct(
         EngineProcActInst engineProcInst
    ) {
        String message = "操作成功";
        int statusCode = 200;

        try {
            EngineProcActInst dest = null;
            final SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
            final String requestId = engineProcInst.getEnId();

            if (StringUtils.isNotBlank(requestId)) {
                // 更新
                dest = iEngineProcActInstService.getById(requestId);
                BeanUtils.copyBeanProp(dest, engineProcInst);
                dest.setCreateTime(new Date());
                dest.setModifyUserId(user.getUserName());
                iEngineProcActInstService.updateById(dest);
            } else {
                // 添加
                dest = engineProcInst;
                dest.setEnId(null);
                dest.setCreateTime(new Date());
                dest.setCreateUserId(user.getUserName());
                dest.setEnStatus("active");
                iEngineProcActInstService.save(dest);
            }
        } catch (Exception e) {
            message = "操作失败";
            statusCode = 500;
            logger.error(e.getMessage());
        }
        return ResultBody.ok().msg(message).code(statusCode);
    }

    /**
     * 	 * 流程实例节点删除
     */
    @GetMapping("/process-inst-act-remove")
    public ResultBody instActRemove(
            @RequestParam("delids") String delids
    ){
        String message = "操作成功";
        int statusCode = 200;

        try {
            if (StringUtils.isNotBlank(delids)){
                for (String rowId : delids.split(",")) {
                    if (null != rowId){
                        iEngineProcActInstService.removeById(rowId);
                    }
                }
            }

        }catch (Exception e){
            message = "删除失败";
            statusCode = 500;
            logger.error(e.getMessage());
        }

        return ResultBody.ok().msg(message).code(statusCode);
    }

    /**
     * 	 * 流程实例节点参与者页面
     */
    @GetMapping("/act-userlist")
    public ResultBody userlist(
        @RequestParam(required = false) Map map
    ){
        PageParams pageParams = new PageParams(map);
        EngineProcWorkitem query = pageParams.mapToObject(EngineProcWorkitem.class);
        QueryWrapper<EngineProcWorkitem> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(query.getProcInstId()), EngineProcWorkitem::getProcInstId, query.getProcInstId())
                .eq(StringUtils.isNotBlank(query.getActInsId()), EngineProcWorkitem::getActInsId , query.getActInsId())
                .like(StringUtils.isNotBlank(query.getWorkItemUserName()), EngineProcWorkitem::getWorkItemUserName , query.getWorkItemUserName());
        return ResultBody.ok().data(iEngineProcWorkitemService.page(pageParams, queryWrapper));
    }

    /**
     * 添加用户页面 , 显示当前 流程节点的用户
     */
    @GetMapping("/act-user-edit")
    public ResultBody actUserAdd(
            @RequestParam("rowId") String rowId
    ){
        // 根据 workItem 中的 Eid 来获取该流程节点的关联人员
        return ResultBody.ok().data(iEngineProcWorkitemService.getById(rowId));
    }

    @Autowired
    private EngineProcWorkItemSaveService engineProcWorkItemSaveService;
    /**
     *	 * 流程工作项的修改保存
     */
    @GetMapping("/saveWorkItem")
    public ResultBody saveUserToAct(
            @RequestParam("actInsId") String actInsId,
            @RequestParam("procInstId") String procInstId,
            // 选择用户的 Id 字串
            @RequestParam(value = "userIds", required = false) String userIds
    ){
        String message = "保存成功";
        int statusCode = 200;
        /*
            先获取 当前 流程实例 对象信息  和流程节点对象信息
         */
        try {
            final EngineProcActInst engineProcActInst = iEngineProcActInstService.getById(actInsId);
            final EngineProcInst engineProcInst = iEngineProcInstService.getById(procInstId);
            EngineProcWorkitem engineProcWorkitem = null;
            /*
                根据传递过来的 用户 Ids 来 操作用户
             */
            String[] userIdGroup = userIds.split(",");
            List<String> uIdList = new ArrayList<String>();
            for (String id : userIdGroup) {// 去重复的值
                if (!uIdList.contains(id)) {
                    uIdList.add(id);
                }
            }
            // 先根据id 查询当前添加的用户
            for (String userRowId : uIdList) {
                final SysUser currentInsertUser = sysUserMapper.selectUserByIdString(userRowId);
                if (currentInsertUser == null){
                    throw new NullPointerException();
                }
                engineProcWorkitem = engineProcWorkItemSaveService.engineProcWorkitem(procInstId,actInsId,currentInsertUser,engineProcActInst,engineProcInst);

                iEngineProcWorkitemService.save(engineProcWorkitem);
            }


        }
        catch (NullPointerException nullException){
            message = "请联系管理员";
            statusCode = 500;
            logger.error(nullException.getMessage());
        }
        catch (Exception e){
            message = "操作失败";
            statusCode = 500;
            logger.error(e.getMessage());
        }
        return ResultBody.ok().msg(message).code(statusCode);

    }

    /**
     * 	 * 节点关联用户的删除
     */
    @GetMapping("/act-removeUser")
    public ResultBody actUserRemove(
            @RequestParam("delids") String delids
    ){
        String message = "保存成功";
        int statusCode = 200;

        try {
            if (delids != null && delids.length() > 0) {
                String[] enIds = delids.split(",");
                for (String enId : enIds) {
                    iEngineProcWorkitemService.removeById(enId);
                }
            }

        }catch (Exception e){
            message = "操作失败";
            statusCode = 500;

            logger.error(e.getMessage());
        }
        logger.info(message);
        return ResultBody.ok().msg(message).code(statusCode);
    }

    /**
     * 	 * 选择关联用户页面
     */
    @GetMapping("/act-userlookup")
    public ResultBody userlookup(){
        List<SysUser> sysUsers = sysUserMapper.selectAllVUser();
        List<Map<String,String>> result = new ArrayList<>();
        for (SysUser sysUser : sysUsers) {
            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("userId",String.valueOf(sysUser.getUserId()));
            resultMap.put("nickName",sysUser.getNickName());
            resultMap.put("deptName",sysUser.getDept() != null ? sysUser.getDept().getDeptName() : "");
            resultMap.put("userName",sysUser.getUserName());
            result.add(resultMap);
        }

        return ResultBody.ok().data(result);
    }

    /**
     * 判断流程是否已经结束，1为已结束，0为未结束，-1为无该流程
     * @return
     */
    @GetMapping("/is-end/{piId}")
    public AjaxResult isEndByPiId(@PathVariable String piId) {
        Integer res = iEngineProcActInstService.isEndNode(piId);
        return AjaxResult.success(res);
    }
}