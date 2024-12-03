package com.ruoyi.project.business.service;

import com.ruoyi.project.business.domain.BusinessDevice;
import com.ruoyi.project.business.vo.DeviceVo;
import com.ruoyi.project.business.vo.ReqCheckVo;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;

import java.util.List;

/**
 * @author niminui
 * @date 2021/6/10 14:13
 */
public interface IBusinessDeviceService extends IBaseService<BusinessDevice> {

    /**
     * 根据合同id查询当前用户已领用的设备
     * @param contractId 合同编号
     * @return
     */
    public List<BusinessDevice> selectByContractIdUser(String contractId);

    public void updateReadByCheckId(String rowId);

    /**
     * 获取任务池所有任务任务
     * @param deviceVo 查询参数
     */
    public List<DeviceVo> getTaskReceive(DeviceVo deviceVo);

    /**
     * 获取待领取任务总数
     * @return
     */
    public int getTaskReceiveCount();

    /**
     * 领取任务
     * @param ids 要领取任务的rowId（设备rowId）
     * @return true为成功，false为失败
     */
    public boolean receiveTask(String[] ids);

    /**
     * 归还任务
     * @param ids 要归还任务的rowId（设备rowId）
     * @return true为成功，false为失败
     */
    public boolean revertTask(String[] ids);

    /**
     * 查询待处理任务 is_read = 1
     * @param deviceVo 查询参数
     * @return
     */
    public List<DeviceVo> getTaskPending(DeviceVo deviceVo);

    /**
     * 查询待已处理任务 is_read = 0
     * @param deviceVo 查询参数
     * @return
     */
    public List<ReqCheckVo> getTasksProcessed(ReqCheckVo deviceVo);

    public List<BusinessDevice> selectByContractId(String contractId);
}
