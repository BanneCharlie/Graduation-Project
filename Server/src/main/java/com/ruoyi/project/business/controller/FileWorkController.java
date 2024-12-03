package com.ruoyi.project.business.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.domain.TreeSelect;
import com.ruoyi.project.business.domain.FileWork;
import com.ruoyi.project.business.service.IFileWorkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niminui
 * @date 2021/6/11 10:32
 */
@RestController
@RequestMapping("file-work")
public class FileWorkController {

    @Resource
    private IFileWorkService iFileWorkService;

    /**
     * 获取作业指导书类型及类型下所有文件的树形结构
     * @return 返回树形结构
     */
    @GetMapping("tree")
    public AjaxResult getFileWorkTree() {
        List<TreeSelect> tree = iFileWorkService.getFileWorkTree();
        return AjaxResult.success(tree);
    }

}
