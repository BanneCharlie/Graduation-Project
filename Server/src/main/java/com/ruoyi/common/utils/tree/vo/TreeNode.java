package com.ruoyi.common.utils.tree.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author niminui
 * @date 2021/6/3 15:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {

    /**
     * 结点ID
     */
    private String id;

    /**
     * 父节点Id
     */
    private String pid;

    /**
     * 显示名称
     */
    private String label;

    /**
     * 子元素
     */
    private List<TreeNode> children;

    public TreeNode(String id, String pid, String label) {
        this.id = id;
        this.pid = pid;
        this.label = label;
    }
}
