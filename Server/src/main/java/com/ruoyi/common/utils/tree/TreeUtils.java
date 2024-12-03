package com.ruoyi.common.utils.tree;

import com.ruoyi.common.utils.tree.vo.TreeNode;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author niminui
 * @date 2021/6/3 15:53
 */
@Component
public class TreeUtils {

    @Resource
    private ISysDeptService iSysDeptService;

    @Resource
    private ISysUserService iSysUserService;

    /**
     * 根据部门列表生成部门-用户的树形结构
     * @param deptList 部门列表
     * @return
     */
    public List<TreeNode> setDeptWithEmployeeTree(List<SysDept> deptList) {
        List<SysDept> deptTrees = iSysDeptService.buildDeptTree(deptList);
        return setDeptUser(deptTrees);
    }

    /**
     * 通过已构造好的部门树形结构 构造部门-用户的树形结构
     * @param trees 已构造好的部门树形结构
     * @return
     */
    private List<TreeNode> setDeptUser(List<SysDept> trees) {
        List<TreeNode> retList = new ArrayList<>();
        for (SysDept dept : trees) {
            List<SysDept> children = dept.getChildren();
            TreeNode node = new TreeNode(
                    String.valueOf(dept.getDeptId()),
                    String.valueOf(dept.getParentId()),
                    dept.getDeptName()
            );
            if (children != null && children.size() != 0) {
                node.setChildren(setDeptUser(children));
            } else {
                node.setChildren(setUsers(dept.getDeptId()));
            }
            retList.add(node);
        }
        return retList;
    }

    /**
     * 通过部门 ID 构造用户的树形结构，无子元素
     * @param deptId 部门ID
     * @return
     */
    private List<TreeNode> setUsers(Long deptId) {
        List<TreeNode> userNode = new ArrayList<>();
        List<SysUser> users = iSysUserService.getUsersByDeptId(deptId);
        for (SysUser user : users) {
            TreeNode node = new TreeNode(user.getUserName(), String.valueOf(deptId), user.getNickName());
            userNode.add(node);
        }
        return userNode;
    }

}
