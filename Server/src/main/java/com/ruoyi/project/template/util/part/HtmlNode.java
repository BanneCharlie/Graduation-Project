package com.ruoyi.project.template.util.part;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ---> Html Node 节点信息
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-07-27 15:17:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HtmlNode {

    /** 元内容数据信息 */
    private transient String metaContentData;
    /** 找到的目标HtmlNode标签体 */
    private String locateHtmlNode;

    /** 搜索内容起始位置索引 */
    private int locateBeginIndex;
    /** 搜索内容结束位置索引 */
    private int locateEndIndex;
    /** 搜索内容 */
    private String locateContent;

    /** 当前HtmlNode节点开始位置索引 */
    private int tagBeginIndex;
    /** 当前HtmlNode节点结束位置索引 */
    private int tagEndIndex;
    /** HtmlNode表示 的标签名称 */
    private String tagName;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
