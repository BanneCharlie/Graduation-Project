package com.ruoyi.project.business.service;

import com.ruoyi.project.business.domain.ProRepManage;
import com.ruoyi.project.business.domain.ReqRepManage;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;

import java.util.List;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_28 _ 13:59__星期五
 * @package: com.ruoyi.project.business.service
 * @JDK-Version : 1.8
 */
public interface IProRepManageService extends IBaseService<ProRepManage> {
    public List<ProRepManage> getList(ProRepManage proRepManage);

    boolean deleteBeanByIds(String[] rowIds);
}
