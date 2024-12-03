package com.ruoyi.project.template.util.part;

import com.ruoyi.project.template.util.TemplateHtmlUtils;
import lombok.Builder;
import lombok.Data;

/**
 * --->
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-08-01 11:36:56
 */
@Data
@Builder
public class HtmlAttribute {

    private String attributeName;

    private String attributeValue;


    public static HtmlAttribute resolveHtmlNodeOfAttribute(HtmlNode htmlNode){
        if (htmlNode == TemplateHtmlUtils.EMPTY_NODE){
            return null;
        }
        return HtmlAttribute.builder()
                .attributeName("class")
                .attributeValue(TemplateHtmlUtils.TARGET_TAG_CLASS_NAME_MAPPING.get(htmlNode.getLocateContent()))
                .build();
    }

    @Override
    public String toString() {
        return attributeName + "=\"" + attributeValue + "\"  ";
    }
}
