package com.ruoyi.project.business.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.business.domain.BusinessDevice;
import com.ruoyi.project.business.service.IBusinessDeviceService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 合同申请设备
 * @author niminui
 * @date 2021/6/10 14:15
 */
@RestController
@RequestMapping("contract-device")
public class BusinessDeviceController extends BaseController {

    @Resource
    private IBusinessDeviceService iBusinessDeviceService;

    @Resource
    private ISysUserService iSysUserService;

    @GetMapping(value = "list/{contractId}")
    public AjaxResult getAllList(@PathVariable String contractId) {
        List<BusinessDevice> list = iBusinessDeviceService.selectByContractId(contractId);
        return AjaxResult.success(list);
    }

    /**
     * 根据rowId查询唯一设备信息
     * @param rowId 设备Id
     * @return 设备信息
     */
    @GetMapping(value = "get-device/{rowId}")
    public AjaxResult getDeviceById(@PathVariable String rowId) {
        return AjaxResult.success(iBusinessDeviceService.getById(rowId));
    }

    /**
     * 保存或修改设备列表
     * @param deviceList 设备列表
     * @return true成功，false失败
     */
    @PostMapping(value = "save-list")
    public AjaxResult formSave(@RequestBody List<BusinessDevice> deviceList) {
        boolean flag;
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        if (deviceList == null || deviceList.isEmpty()) {
            flag = false;
        } else {
            for (BusinessDevice device : deviceList) {
                device.setIsRead(1); //设置为未开始检验申请
                device.setIsReceiving(1); //设置为未领用
                device.setIsHandle(1); //设置为未处理该设备（未处理报告）
                device.setIsGenericReport(1);
                device.setCreateTime(new Date());
                device.setFromUserId(user.getUserName());
                device.setFromUserName(user.getNickName());
            }
            flag = iBusinessDeviceService.saveOrUpdateBatch(deviceList);
        }
        return toAjax(flag);
    }

    /**
     * 根据合同id查询当前用户已领用的设备
     * @param contractId 合同编号
     * @return
     */
    @GetMapping(value = "all-device/{contractId}")
    public AjaxResult getListByContractId(@PathVariable String contractId) {
        List<BusinessDevice> list = iBusinessDeviceService.selectByContractIdUser(contractId);
        return AjaxResult.success(list);
    }

    @PostMapping("set-handle/{deviceRowId}/{reqCheckId}")
    public AjaxResult setDeviceHandle(@PathVariable String deviceRowId, @PathVariable String reqCheckId) {
        LambdaUpdateWrapper<BusinessDevice> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(BusinessDevice::getRowId, deviceRowId)
                .set(BusinessDevice::getIsHandle, 0)
                .set(BusinessDevice::getReqCheckId, reqCheckId);
        return AjaxResult.success(iBusinessDeviceService.update(queryWrapper));
    }
}
