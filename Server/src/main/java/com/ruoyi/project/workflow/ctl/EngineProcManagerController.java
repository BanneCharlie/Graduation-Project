package com.ruoyi.project.workflow.ctl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.process.service.IProcSxpzService;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.workflow.domain.EngineProcDef;
import com.ruoyi.project.workflow.service.IEngineProcDefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 引擎后台管理控制器-流程定义
 * 
 * @author optimus
 * @version 1.0
 */
@RestController
@RequestMapping("engine-mgt")
public class EngineProcManagerController extends BaseController {
	// 日志
	private static final Logger logger = LoggerFactory.getLogger(EngineProcManagerController.class);

    @Resource
	private IProcSxpzService iProcSxpzService;
	@Resource
	private ISysDeptService iSysDeptService;
	@Resource
	private IEngineProcDefService iEngineProcDefService;

	/**
	 * 01进入流程模板管理
	 * @return
	 */
	@RequestMapping("process-def-mgt")
	public TableDataInfo goProcessMgt(EngineProcDef engineProcDef) {
		startPage();
		List<EngineProcDef> list = iEngineProcDefService.selectEngineList(engineProcDef.getProcName());
		return getDataTable(list);
	}

	@Log(title = "流程模板管理", businessType = BusinessType.EXPORT)
	@GetMapping("export/{enIds}")
	public AjaxResult export(@PathVariable String[] enIds)
	{
		List<EngineProcDef> list = iEngineProcDefService.selectEngineByIds(enIds);
		ExcelUtil<EngineProcDef> util = new ExcelUtil<EngineProcDef>(EngineProcDef.class);
		return util.exportExcel(list, "流程模板数据");
	}

	/**
	 * 02根据流程定义编号获取详细信息
	 */
	@GetMapping(value = "get-engine-proc/{enId}")
	public AjaxResult getInfo(@PathVariable String enId)
	{
		return AjaxResult.success(iEngineProcDefService.selectEngineById(enId));
	}

	/**
	 * 03流程定义删除
	 */
	@Log(title = "流程模板管理", businessType = BusinessType.DELETE)
	@DeleteMapping("delete/{enIds}")
	public AjaxResult remove(@PathVariable String[] enIds)
	{
		return toAjax(iEngineProcDefService.deleteEngineProcByIds(enIds));
	}

	/**
	 * 04流程定义保存/更新
	 *
	 * @param engineProcDef
	 * @return
	 */
	@Log(title = "角色管理", businessType = BusinessType.INSERT)
	@PostMapping(value = "process-def-save")
	public AjaxResult save(@RequestBody EngineProcDef engineProcDef) {
		EngineProcDef dest = null;
		String id = engineProcDef.getEnId();
		boolean isTrue = false;

		if (id != null && id.length() > 0) {
			dest = iEngineProcDefService.getById(id);
			BeanUtils.copyBeanProp(dest, engineProcDef);
			dest.setUpdateTime(new Date());
			dest.setModifyUserId(SecurityUtils.getUsername());
			isTrue = iEngineProcDefService.updateById(dest);
		} else {
			dest = engineProcDef;
			dest.setEnId(null);
			dest.setCreateTime(new Date());
			dest.setCreateUserId(SecurityUtils.getUsername());
			dest.setEnStatus("active");
			isTrue = iEngineProcDefService.save(dest);
		}
		return toAjax(isTrue);

	}

}