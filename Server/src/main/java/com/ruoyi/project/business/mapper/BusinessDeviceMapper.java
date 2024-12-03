package com.ruoyi.project.business.mapper;

import com.ruoyi.project.business.domain.BusinessDevice;
import com.ruoyi.project.business.vo.DeviceVo;
import com.ruoyi.project.business.vo.ReqCheckVo;
import com.ruoyi.project.template.commons.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author niminui
 * @date 2021/6/10 14:13
 */
public interface BusinessDeviceMapper extends SuperMapper<BusinessDevice> {

    public List<BusinessDevice> selectByContractIdUser(@Param("contractId") String contractId, @Param("receivingUserId") String receivingUserId);

    public void updateReadByCheckId(String rowId);

    public List<DeviceVo> getTaskReceive(@Param("deviceVo") DeviceVo deviceVo, @Param("userId") String userId);

    public int getTaskReceiveCount(String username);

    public List<DeviceVo> getTaskPending(@Param("deviceVo") DeviceVo deviceVo, @Param("username") String username);

    public List<ReqCheckVo> getTasksProcessed(@Param("reqCheckVo") ReqCheckVo reqCheckVo, @Param("username") String username);

    public List<BusinessDevice> selectByContractId(String contractId);
}
