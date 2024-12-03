package com.ruoyi.project.template.commons.enums;

import java.util.Arrays;

/**
 * ---> 附件数据类型
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-06-12 10:58:07
 */
public enum AttachDataType {

    /** 检验报告附件 */
    REPORT_ATTACH(1),
    ;

    final int dataType;

    AttachDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDataType() {
        return dataType;
    }

    public static boolean judgeHasEnumType(String checkContent){
        return Arrays.stream(values())
                .map(AttachDataType::getDataType)
                .anyMatch(v -> v.equals(Integer.parseInt(checkContent)));
    }

}
