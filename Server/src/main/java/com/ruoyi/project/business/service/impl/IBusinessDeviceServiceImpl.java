package com.ruoyi.project.business.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.business.domain.BusinessDevice;
import com.ruoyi.project.business.mapper.BusinessDeviceMapper;
import com.ruoyi.project.business.service.IBusinessDeviceService;
import com.ruoyi.project.business.vo.DeviceVo;
import com.ruoyi.project.business.vo.ReqCheckVo;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author niminui
 * @date 2021/6/10 14:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IBusinessDeviceServiceImpl extends BaseServiceImpl<BusinessDeviceMapper, BusinessDevice> implements IBusinessDeviceService {

    @Resource
    private BusinessDeviceMapper businessDeviceMapper;

    @Resource
    private ISysUserService iSysUserService;

    /**
     * 根据合同id查询当前用户已领用的设备
     * @param contractId 合同编号
     * @return
     */
    @Override
    public List<BusinessDevice> selectByContractIdUser(String contractId) {
        return businessDeviceMapper.selectByContractIdUser(contractId, SecurityUtils.getUsername());
    }

    @Override
    public void updateReadByCheckId(String rowId) {
        businessDeviceMapper.updateReadByCheckId(rowId);
    }

    /**
     * 获取待领取任务
     * @param deviceVo
     */
    @Override
    public List<DeviceVo> getTaskReceive(DeviceVo deviceVo) {
        return businessDeviceMapper.getTaskReceive(deviceVo, String.valueOf(SecurityUtils.getLoginUser().getUser().getUserId()));
    }

    /**
     * 获取待领取任务总数
     * @return
     */
    @Override
    public int getTaskReceiveCount() {
        return businessDeviceMapper.getTaskReceiveCount(String.valueOf(SecurityUtils.getLoginUser().getUser().getUserId()));
    }

    /**
     * 领取任务
     * @param ids 要领取任务的rowId（设备rowId）
     * @return true为成功，false为失败
     */
    @Override
    public boolean receiveTask(String[] ids) {
        boolean flag = true;
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        List<BusinessDevice> list = businessDeviceMapper.selectBatchIds(new ArrayList<>(Arrays.asList(ids)));

        for (BusinessDevice device : list) {
            if (device.getIsReceiving() == 0) {
                flag = false;
                break;
            }
            device.setIsReceiving(0);
            device.setReceivingUserId(String.valueOf(user.getUserId()));
            device.setReceivingUserName(user.getNickName());
            device.setUpdateTime(new Date());
            businessDeviceMapper.updateById(device);
        }

        return flag;
    }

    /**
     * 归还任务
     * @param ids 要归还任务的rowId（设备rowId）
     * @return true为成功，false为失败
     */
    @Override
    public boolean revertTask(String[] ids) {
        boolean flag = true;
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());
        List<BusinessDevice> list = businessDeviceMapper.selectBatchIds(new ArrayList<>(Arrays.asList(ids)));

        for (BusinessDevice device : list) {
            if (device.getIsReceiving() ==  1) {
                flag = false;
                break;
            }
            device.setIsReceiving(1);
            device.setReceivingUserId("");
            device.setReceivingUserName("");
            device.setUpdateTime(null);
            businessDeviceMapper.updateById(device);
        }

        return flag;
    }

    @Override
    public List<DeviceVo> getTaskPending(DeviceVo deviceVo) {
        return businessDeviceMapper.getTaskPending(deviceVo, String.valueOf(SecurityUtils.getLoginUser().getUser().getUserId()));
    }

    @Override
    public List<ReqCheckVo> getTasksProcessed(ReqCheckVo reqCheckVo) {
        return businessDeviceMapper.getTasksProcessed(reqCheckVo, String.valueOf(SecurityUtils.getLoginUser().getUser().getUserId()));
    }

    @Override
    public List<BusinessDevice> selectByContractId(String contractId) {
        return businessDeviceMapper.selectByContractId(contractId);

    }
}
