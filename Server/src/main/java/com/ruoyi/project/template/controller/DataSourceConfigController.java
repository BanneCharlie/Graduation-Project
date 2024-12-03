package com.ruoyi.project.template.controller;


import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.template.domain.DataSourceConfig;
import com.ruoyi.project.template.service.DataSourceConfigService;
import com.ruoyi.project.template.util.JdbcUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author niminui
 * @date 2020/11/20 15:57
 */
@Api(tags = "数据源配置Controller")
@RestController
@RequestMapping("data-source")
public class DataSourceConfigController extends BaseController {

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @GetMapping(value = "getOne/{rowId}")
    public AjaxResult getInfo(@PathVariable String rowId)
    {
        DataSourceConfig info = dataSourceConfigService.getById(rowId);
        info.setJdbcPwd(null);
        info.setJdbcUserName(null);
        return AjaxResult.success(info);
    }

    @GetMapping("list")
    public TableDataInfo getList(DataSourceConfig dataSourceConfig) {
        startPage();
        List<DataSourceConfig> list = dataSourceConfigService.getList(dataSourceConfig);
        list.forEach(item -> {
            item.setJdbcUserName(null);
            item.setJdbcPwd(null);
        });
        return getDataTable(list);
    }


    @GetMapping(value = "getAll")
    public AjaxResult getAll() {
        return AjaxResult.success(dataSourceConfigService.list());
    }

    @GetMapping(value = "get-schema/{dataSourceId}")
    public AjaxResult getSchema(@PathVariable String dataSourceId) {
        DataSourceConfig data = dataSourceConfigService.getById(dataSourceId);
        JdbcUtil.setInfo(data.getJdbcUrl(), data.getJdbcUserName(), data.getJdbcPwd(), data.getJdbcDriverType());
        List<String> schemas = JdbcUtil.getTablesInSchema("", data.getSchemaName());
        return AjaxResult.success(schemas);
}

    @GetMapping(value = "getField")
    public AjaxResult getField() throws Exception {
//        DataSourceConfig data = dataSourceConfigService.getById(dataSourceId);
//        JdbcUtil.setInfo(data.getJdbcUrl(), data.getJdbcUserName(), data.getJdbcPwd(), data.getJdbcDriverType());
//        List<ColumnInfoBean> fields = JdbcUtil.getColumnInfoesInTable(tableName);
//        return AjaxResult.success(fields);

        return AjaxResult.success(dataSourceConfigService.getReqRepFields());
    }

    @Log(title = "数据源管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "save")
    public AjaxResult save(@RequestBody DataSourceConfig dataSource) {
        DataSourceConfig dest = null;
        String id = dataSource.getDataSourceId();
        boolean isTrue;

        if (StringUtils.isNotEmpty(id)) {
            dest = dataSourceConfigService.getById(id);
            BeanUtils.copyBeanProp(dest, dataSource);
            dest.setUpdateTime(new Date());
            isTrue = dataSourceConfigService.updateById(dest);
        } else {
            dest = dataSource;
            dest.setDataSourceId(null);
            dest.setCreateTime(new Date());
            isTrue = dataSourceConfigService.save(dest);
        }
        return toAjax(isTrue);

    }

    @Log(title = "数据源管理", businessType = BusinessType.DELETE)
    @DeleteMapping("delete/{rowIds}")
    public AjaxResult remove(@PathVariable String[] rowIds)
    {
        return toAjax(dataSourceConfigService.deleteConfigByIds(rowIds));
    }

    @PostMapping(value = "update-password")
    public AjaxResult updatePassword(@RequestBody Map<String, Object> map) {
        String rowId = StringUtils.getString(map.get("rowId"));
        String originalPwd = StringUtils.getString(map.get("originalPwd"));
        String newPwd = StringUtils.getString(map.get("newPwd"));
        DataSourceConfig bean = dataSourceConfigService.getById(rowId);

        if (StringUtils.equals(bean.getJdbcPwd(), originalPwd)) {
            bean.setJdbcPwd(newPwd);
            dataSourceConfigService.updateById(bean);
            return AjaxResult.success("数据源密码修改成功");
        } else {
            return AjaxResult.error("原始密码错误");
        }
    }
}
