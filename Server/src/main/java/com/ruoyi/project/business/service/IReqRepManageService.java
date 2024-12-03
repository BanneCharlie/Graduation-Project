package com.ruoyi.project.business.service;

import com.ruoyi.project.business.domain.ReqRepManage;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;

import java.util.List;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_28 _ 11:51__星期五
 * @package: com.ruoyi.project.business.service
 * @JDK-Version : 1.8
 */

public interface IReqRepManageService extends IBaseService<ReqRepManage> {

    public List<ReqRepManage> getList(ReqRepManage reqRepManage,String beginTime,String endTime);

    boolean deleteBeanByIds(String[] rowIds);

    List<Object> selectReqContractViewRowIds();
}
