package com.ruoyi.project.business.service;

import com.ruoyi.framework.web.domain.TreeSelect;
import com.ruoyi.project.business.domain.FileWork;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;

import java.util.List;

/**
 * @author niminui
 * @date 2021/6/11 10:25
 */
public interface IFileWorkService extends IBaseService<FileWork> {

    /**
     * 获取作业指导书类型及类型下所有文件的树形结构
     * @return 返回树形结构
     */
    public List<TreeSelect> getFileWorkTree();

}
