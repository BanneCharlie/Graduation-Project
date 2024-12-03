package com.ruoyi.project.template.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.template.commons.model.ResultBody;
import com.ruoyi.project.template.domain.ReportRow;
import com.ruoyi.project.template.domain.ReportTemplate;
import com.ruoyi.project.template.service.ReportRowService;
import com.ruoyi.project.template.util.RuoYiRequestParam;
import com.ruoyi.project.template.util.StringUtils;
import com.ruoyi.project.template.vo.RowNode;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author niminui
 * @date 2020/11/24 13:03
 */
@Api(tags = "报告模板Controller")
@RestController
@RequestMapping("template-row")
public class ReportRowController extends BaseController {

    @Resource
    private ReportRowService reportRowService;

    @Resource
    private ISysUserService iSysUserService;

    /**
     * 获取自定义列信息
     * @return
     */
    @GetMapping("tree-list")
    public TableDataInfo getList(ReportRow row){
        startPage();
        List<RowNode> treeNode = reportRowService.getRowTree(row);
        return getDataTable(treeNode);
    }

    @GetMapping(value = "list-row")
    public AjaxResult listRow() {
        return AjaxResult.success(reportRowService.list());
    }

    @GetMapping(value = "get-info/{rowId}")
    public AjaxResult getInfoByRowId(@PathVariable String rowId) {
        ReportRow bean = reportRowService.getById(rowId);
        if (StringUtils.isEmpty(bean.getParentId())) {
            bean.setParentId("0");
        }
        return AjaxResult.success(bean);
    }

    @PostMapping("save")
    public AjaxResult save(@RequestBody ReportRow reportRow) {
        ReportRow dest = null;
        String id = reportRow.getReportRowId();
        boolean isTrue = true;

        ReportRow parent = reportRowService.getById(reportRow.getParentId());
        if (parent != null) {
            reportRow.setParentName(parent.getReportTitle());
        }
        if (StringUtils.equals(reportRow.getParentId(), "0")) {
            reportRow.setParentId(null);
        }

        if (StringUtils.isNotEmpty(id)) {
            dest = reportRowService.getById(id);
            BeanUtils.copyBeanProp(dest, reportRow);
            dest.setUpdateTime(new Date());
            isTrue = reportRowService.updateById(dest);
        } else {
            dest = reportRow;
            dest.setTemplateId(null);
            dest.setCreateTime(new Date());
            if (dest.getOrderNum() == 0) {
                int order = reportRowService.getLastOrderByParentId(parent.getReportRowId());
                dest.setOrderNum(order + 1);
            }
            isTrue = reportRowService.save(dest);
        }
        return toAjax(isTrue);
    }

    @DeleteMapping("delete/{rowIds}")
    public AjaxResult removeEntity(@PathVariable String[] rowIds) {
        return toAjax(reportRowService.deleteReportByIds(rowIds));
    }
}
