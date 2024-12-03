package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.TreeSelect;
import com.ruoyi.project.business.domain.FileWork;
import com.ruoyi.project.business.mapper.FileWorkMapper;
import com.ruoyi.project.business.service.IFileWorkService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author niminui
 * @date 2021/6/11 10:26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IFileWorkServiceImpl extends BaseServiceImpl<FileWorkMapper, FileWork> implements IFileWorkService {

    @Resource
    private FileWorkMapper fileWorkMapper;

    /**
     * 获取作业指导书类型及类型下所有文件的树形结构
     * @return 返回树形结构
     */
    @Override
    public List<TreeSelect> getFileWorkTree() {
        List<TreeSelect> treeSelects = new ArrayList<>();
        //查询祖先node
        List<FileWork> ancestors = getNodeByParentId("0", "");
        for (FileWork ancestor : ancestors) {
            TreeSelect node = new TreeSelect();
            node.setId(Long.parseLong(ancestor.getRowId()));
            node.setLabel(ancestor.getFileName());
            node.setChildren(initTree(ancestor));
            treeSelects.add(node);
        }
        return treeSelects;
    }

    /**
     * 根据祖先节点（根节点）查询其所有子节点，并且递归查询子节点下的所有节点
     * @param node 节点
     * @return
     */
    private List<TreeSelect> initTree(FileWork node) {
        List<TreeSelect> treeSelects = new ArrayList<>();
        if (node.getDataType() == 1) {
            List<FileWork> nodes = getNodeByParentId(node.getRowId(), "");
            for (FileWork bean : nodes) {
                TreeSelect cNode = new TreeSelect();
                cNode.setId(Long.parseLong(bean.getRowId()));
                cNode.setLabel(bean.getFileName());
                cNode.setChildren(initTree(bean));
                treeSelects.add(cNode);
            }
        }

        return treeSelects;
    }

    /**
     * 根据父ID及status查询FileWork
     * @param parentId 父ID
     * @param status status
     * @return
     */
    private List<FileWork> getNodeByParentId(String parentId, String status) {
        QueryWrapper<FileWork> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FileWork::getParentId, parentId)
                .eq(StringUtils.isNotEmpty(status), FileWork::getStatus, status)
                .orderByAsc(FileWork::getOrderNum);
        return fileWorkMapper.selectList(queryWrapper);
    }
}
