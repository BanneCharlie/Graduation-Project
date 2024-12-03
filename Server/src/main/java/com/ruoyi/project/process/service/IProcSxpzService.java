package com.ruoyi.project.process.service;

import com.ruoyi.project.process.domian.ProcSxpz;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;

import java.util.List;

/**
 * @author niminui
 * @date 2021/5/24 11:58
 */
public interface IProcSxpzService extends IBaseService<ProcSxpz> {

    public List<ProcSxpz> getList(String sxId, String sxZfType);

}
